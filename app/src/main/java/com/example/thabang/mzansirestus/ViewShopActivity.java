package com.example.thabang.mzansirestus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thabang.mzansirestus.pojo.Shop;

public class ViewShopActivity extends AppCompatActivity {
    private ImageView imageSwitcher;
    private LinearLayout layout;
    private ImageButton btnAddress,btnTelephone,btnWebsite,btnDirections;
    private TextView txtRatingl, txtDescription,txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop);


        imageSwitcher = findViewById(R.id.shop_image_switch);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtDescription = (TextView) findViewById(R.id.txt_hotel_desc);
        layout = (LinearLayout) findViewById(R.id.linearDetails);
        btnAddress = (ImageButton) findViewById(R.id.btn_place);
        btnTelephone = (ImageButton) findViewById(R.id.btn_call);
        txtInfo = (TextView) findViewById(R.id.hotel_info);
        btnDirections =(ImageButton) findViewById(R.id.btn_direction);

        Intent intent = getIntent();
        final Shop shop = (Shop) intent.getSerializableExtra("shop");
        setTitle(shop.getTradingName());
        Glide.with(getApplicationContext()).load(shop.getSdownloadMenu()).into(imageSwitcher);
        layout.setVisibility(View.VISIBLE);
        txtInfo.setText(shop.getStreetAddress() + "\n" + shop.getSuburb() + "\n" + shop.getCity());
        
        txtDescription.setText(shop.getAbout());

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                txtInfo.setText(shop.getStreetAddress() + "\n" + shop.getSuburb() + "\n" + shop.getCity());

                txtInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }


        });


        btnTelephone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                txtInfo.setText(shop.getCellphone());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ shop.getCellphone()));
                startActivity(intent);

            }
        });



        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shop.getStreetAddress() + "\n" + shop.getSuburb() + "\n" + shop.getCity());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}
