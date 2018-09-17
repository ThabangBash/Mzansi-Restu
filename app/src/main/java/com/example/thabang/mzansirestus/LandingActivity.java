package com.example.thabang.mzansirestus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thabang.mzansirestus.pojo.Person;
import com.example.thabang.mzansirestus.pojo.Shop;
import com.example.thabang.mzansirestus.pojo.UserImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference databaseReference,uploadRef,shopsRef,shopsRefOwer;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;

    private ImageView navProfileImage;
    private LinearLayout navBackground;
    private TextView navUsername;
    private ListView lvShops;

    private ArrayList<Shop> listShops;
    private ShopAdapter shopAdapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();0659351363
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
         navProfileImage = hView.findViewById(R.id.image_nav_header);
         navBackground = hView.findViewById(R.id.nav_background);
         navUsername = hView.findViewById(R.id.textView_nav_username);
         lvShops = findViewById(R.id.lvListRooms);

        navBackground.setBackgroundResource(R.drawable.background);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("profile").child(uid);
        uploadRef = firebaseDatabase.getReference().child("profile pictures").child(uid).child("imageUrl");
        shopsRef = firebaseDatabase.getReference().getRoot().child("shops");
        shopsRefOwer = firebaseDatabase.getReference().getRoot().child("shops").child(uid);

        listShops = new ArrayList<>();


        shopsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("Ygritte", snapshot.toString());
                    Shop shop = snapshot.getValue(Shop.class);
                    listShops.add(shop);
                    //list_of_rooms_id.add(snapshot.getKey());
                }
                shopAdapter = new ShopAdapter(getBaseContext(),listShops);
                shopAdapter.notifyDataSetChanged();
                lvShops.setAdapter(shopAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lvShops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shop viewShop = listShops.get(position);
                Intent intent = new Intent(LandingActivity.this,ViewShopActivity.class);
                intent.putExtra("shop",viewShop);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person value = dataSnapshot.getValue(Person.class);
                if (value != null) {
                    navUsername.setText(value.getUsername());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        uploadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserImage value = dataSnapshot.getValue(UserImage.class);
                if (value != null) {
                    Glide.with(getApplicationContext()).load(value.getDownloadLink()).into(navProfileImage);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(LandingActivity.this,DetailsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            Intent intent = new Intent(LandingActivity.this,ShopRegistrationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(LandingActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
