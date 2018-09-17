package com.example.thabang.mzansirestus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thabang.mzansirestus.pojo.Shop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateBusiProfActivity extends AppCompatActivity {
    private ImageView imLogoView;
    private Button btnUploudMenu;
    private FloatingActionButton btnUploadLogo;
    private TextInputEditText editTradingName, editAbout, editStreet, editSuburb, editCity, editCell, editEmail;

    ProgressDialog pd;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, uploadRef,uploadRefPDF;
    private StorageReference mStorageRefShops, mStorageRefMenus;

    private String uid;
    private Uri imageUri,pdfUri;
    private String sDownloadUri;
    private String link,linkPDF;
    private Shop shop;

    int RESULT_LOAD_IMG = 1;
    int RESULT_LOAD_PDF = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_busi_prof);

        imLogoView = findViewById(R.id.ImageView_business_logo);
        btnUploadLogo = findViewById(R.id.floatingActionButton_business_upload_logo);
        btnUploudMenu = findViewById(R.id.button_business_upload_menu);
        editTradingName = findViewById(R.id.textInput_business_trading_name);
        editAbout = findViewById(R.id.textInput_business_about);
        editStreet = findViewById(R.id.textInput_business_street_address);
        editSuburb = findViewById(R.id.textInput_business_suburb);
        editCity = findViewById(R.id.textInput_business_city);
        editCell = findViewById(R.id.textInput_business_cell);
        editEmail = findViewById(R.id.textInput_business_emailField);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mStorageRefShops = FirebaseStorage.getInstance().getReference().child("shops").child(uid).child("logo");
        mStorageRefMenus = FirebaseStorage.getInstance().getReference().child("shops").child(uid).child("menu");
        databaseReference = firebaseDatabase.getReference().child("shops").child(uid);
        uploadRef = firebaseDatabase.getReference().child("shops").child(uid);
        uploadRefPDF = firebaseDatabase.getReference().child("shops").child(uid);

        btnUploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            }
        });

        btnUploudMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_PDF);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                shop = dataSnapshot.getValue(Shop.class);
                if (shop != null) {

                    editTradingName.setText(shop.getTradingName());
                    editAbout.setText(shop.getAbout());
                    editStreet.setText(shop.getStreetAddress());
                    editSuburb.setText(shop.getSuburb());
                    editCity.setText(shop.getCity());
                    editCell.setText(shop.getCellphone());
                    editEmail.setText(shop.getEmail());
                    Glide.with(getApplicationContext()).load(shop.getSdownloadLLogol()).into(imLogoView);
                    link = shop.getSdownloadLLogol();
                    linkPDF = shop.getSdownloadMenu();
                }

                //Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMG) {
            try {
                imageUri = data.getData();
                pd.show();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //uploading the image
                UploadTask uploadTask = mStorageRefShops.putFile(imageUri);
                shop.setOwnerID(uid);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        sDownloadUri = downloadUrl.toString();
                        shop.setSdownloadLLogol(sDownloadUri);
                        shop.setSdownloadMenu(linkPDF);
                        uploadRef.setValue(shop);
                        pd.dismiss();

                        imLogoView.setImageBitmap(selectedImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UpdateBusiProfActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UpdateBusiProfActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_PDF) {
            pdfUri = data.getData();
            pd.show();
            //uploading the image
            UploadTask uploadTask = mStorageRefMenus.putFile(pdfUri);
            shop.setOwnerID(uid);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    linkPDF = downloadUrl.toString();
                    shop.setSdownloadMenu(linkPDF);
                    shop.setSdownloadLLogol(link);
                    uploadRefPDF.setValue(shop);
                    pd.dismiss();
                    Toast.makeText(UpdateBusiProfActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(UpdateBusiProfActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(UpdateBusiProfActivity.this, "You haven't picked a file", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_profile_done) {

            updateShop(getShop());

            Intent intent = new Intent(UpdateBusiProfActivity.this, ShopOwnerActivity.class);
            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }

    private void updateShop(Shop shop) {
        pd.show();
        databaseReference.setValue(shop).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
            }
        });
    }

    private Shop getShop() {
        shop = new Shop();

        shop.setOwnerID(uid);
        shop.setTradingName(editTradingName.getText().toString());
        shop.setAbout(editAbout.getText().toString());
        shop.setStreetAddress(editStreet.getText().toString());
        shop.setSuburb(editSuburb.getText().toString());
        shop.setCity(editCity.getText().toString());
        shop.setCellphone(editCell.getText().toString());
        shop.setEmail(editEmail.getText().toString());
        shop.setSdownloadLLogol(link);
        shop.setSdownloadMenu(linkPDF);

        return shop;
    }
}
