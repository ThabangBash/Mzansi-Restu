package com.example.thabang.mzansirestus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VarificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varification);
    }

    public void btnSubmit(View view){
        Intent intent = new Intent(VarificationActivity.this,DetailsActivity.class);
        startActivity(intent);
    }
}
