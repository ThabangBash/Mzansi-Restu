package com.example.thabang.mzansirestus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thabang.mzansirestus.pojo.Person;
import com.example.thabang.mzansirestus.pojo.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopOwnerActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_shop_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        navProfileImage = hView.findViewById(R.id.imageView_business);
        navBackground = hView.findViewById(R.id.nav_background);
        navUsername = hView.findViewById(R.id.textView_business_owner);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("profile").child(uid);
        uploadRef = firebaseDatabase.getReference().child("profile pictures").child(uid).child("imageUrl");
        shopsRef = firebaseDatabase.getReference().getRoot().child("shops");
        shopsRefOwer = firebaseDatabase.getReference().getRoot().child("shops").child(uid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        shopsRefOwer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                if (shop != null) {
                    navUsername.setText(shop.getTradingName());
                    Glide.with(getApplicationContext()).load(shop.getSdownloadLLogol()).into(navProfileImage);
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

        if (id == R.id.nav_owner_profile) {
            Intent intent = new Intent(ShopOwnerActivity.this,OwnerProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_owner_business) {
            Intent intent = new Intent(ShopOwnerActivity.this,UpdateBusiProfActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_owner_about) {

        } else if (id == R.id.nav_owner_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ShopOwnerActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
