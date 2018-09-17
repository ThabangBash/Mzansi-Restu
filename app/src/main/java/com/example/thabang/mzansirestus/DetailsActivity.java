package com.example.thabang.mzansirestus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thabang.mzansirestus.pojo.Person;
import com.example.thabang.mzansirestus.pojo.UserImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class DetailsActivity extends AppCompatActivity {
    private TextInputEditText txtUsername;
    private TextView txtcellphone,txtEmail;
    private ImageView imgProfilePic;

    ProgressDialog pd;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,uploadRef;
    private StorageReference mStorageRef;

    private String uid;
    private Uri imageUri;
    private String sDownloadUri;

    int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        txtUsername = findViewById(R.id.text_imput_username);
        txtcellphone = findViewById(R.id.text_view_call);
        txtEmail = findViewById(R.id.text_view_email);
        imgProfilePic = findViewById(R.id.ImageView_user_pic);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile pictures").child(uid);
        databaseReference = firebaseDatabase.getReference().child("profile").child(uid);
        uploadRef = firebaseDatabase.getReference().child("profile pictures").child(uid).child("imageUrl");

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

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
                Person person = dataSnapshot.getValue(Person.class);
                if (person != null) {

                    txtUsername.setText(person.getUsername());
                    txtcellphone.setText(person.getCellphone());
                    txtEmail.setText(person.getEmail());

                }

                //Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        uploadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserImage userImage = dataSnapshot.getValue(UserImage.class);
                if(userImage != null){
                    Glide.with(getApplicationContext()).load(userImage.getDownloadLink()).into(imgProfilePic);
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
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                pd.show();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //uploading the image
                UploadTask uploadTask = mStorageRef.putFile(imageUri);
                final UserImage image = new UserImage();
                image.setUserID(uid);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        sDownloadUri = downloadUrl.toString();
                        image.setDownloadLink(sDownloadUri);
                        uploadRef.setValue(image);
                        pd.dismiss();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUrl)
                                .build();


                        /*if (user != null) {
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Profile Updated ", "User profile updated.");
                                            }
                                        }
                                    });
                        }

                        Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        */
                        imgProfilePic.setImageBitmap(selectedImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(DetailsActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    }
                });



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(DetailsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(DetailsActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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

            updateProfile(getPerson());

            Intent intent = new Intent(DetailsActivity.this,LandingActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void updateProfile(Person person){
        pd.show();
        databaseReference.setValue(person).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               pd.dismiss();
            }
        });
    }

    private Person getPerson(){
        Person user = new Person();

        user.setUid(uid);
        user.setUsername(txtUsername.getText().toString());
        user.setEmail(txtEmail.getText().toString());
        user.setCellphone(txtcellphone.getText().toString());

        return user;
    }
}
