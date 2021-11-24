package com.roksanagulewska.seniorsapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.UploadImage;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileInfoActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 99;
    public static final int CAMERA_PERM_CODE = 100; //camera permission code
    //public static final int GALLERY_REQUEST_CODE = 101;


    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    DataBaseHelper dbHelper = new DataBaseHelper();
    User user;
    String currentPhotoPath;
    Bitmap bitmap;
    String syy = "ddd";

    ImageView mainPicture;
    Button cameraBtn, galleryBtn, confirmBtn;
    EditText descriptionEditTxt;
    //String whichButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        mainPicture = findViewById(R.id.mainPictureImageView);
        cameraBtn = findViewById(R.id.cameraButton);
        galleryBtn = findViewById(R.id.galleryButton);
        confirmBtn = findViewById(R.id.confirmButton);
        descriptionEditTxt = findViewById(R.id.descriptionEditText);

        Intent prefIntent = getIntent(); //intencja która wywołała tą aktywność
        Bundle prefBundle = prefIntent.getExtras(); // przypisanie bundla który przyszedł z intencją
        String email = prefBundle.getString("email");
        String password = prefBundle.getString("password");
        String name = prefBundle.getString("name");
        String localisation = prefBundle.getString("localisation");
        int age = prefBundle.getInt("age");
        String sex = prefBundle.getString("sex");
        String preferredSex = prefBundle.getString("preferredSex");
        int minAge = prefBundle.getInt("minAge");
        int maxAge = prefBundle.getInt("maxAge");

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //whichButtonClicked = "camera";
                askCameraPermission();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //whichButtonClicked = "gallery";
                askCameraPermission();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionEditTxt.getText().toString().trim();

                user = new User(dbHelper.getCurrentUserId(), email, password, name, age, sex, localisation, preferredSex, description, syy, minAge, maxAge);
                dbHelper.addUserToDB(user).addOnSuccessListener(success->
                {
                    Toast.makeText(getApplicationContext(), "User added to database!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(error->
                {
                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

                Intent navIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(navIntent);
                finish();

            }
        });
    }

    //zapytanie o pozwolenie na dostęp do aparatu
    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            //if (whichButtonClicked == "camera")
                openCamera();
//            else if (whichButtonClicked == "gallery") {
//                dispatchTakePictureIntent();
//            }
        }
    }

    //sprawdzenie pozwolenia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //if (whichButtonClicked == "camera")
                    openCamera();
                //} else if (whichButtonClicked == "gallery") {
                    //dispatchTakePictureIntent();
                //}
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            bitmap =  (Bitmap) data.getExtras().get("data");
            mainPicture.setImageBitmap(bitmap);
       // } else if (requestCode == GALLERY_INTENT_CODE) {
         //   if (resultCode == Activity.RESULT_OK) {
           // File file = new File(currentPhotoPath);
            //mainPicture.setImageURI(Uri.fromFile(file));
        }
    }

/*
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, //prefix
                ".jpg",// suffix
                storageDir // directory

        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_INTENT_CODE);
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mainPicture.setImageBitmap(bitmap);
        }
    }

    */


    /*
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

                String description = descriptionEditTxt.getText().toString().trim();

                user = new User(dbHelper.getCurrentUserId(), email, password, name, age, sex, localisation, preferredSex, description, null, minAge, maxAge);
                dbHelper.addUserToDB(user).addOnSuccessListener(success->
                {
                    Toast.makeText(getApplicationContext(), "User added to database!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(error->
                {
                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                });


                Intent pInfoIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                Bundle pInfoBundle = new Bundle();
                pInfoBundle.putString("email", email);
                pInfoBundle.putString("password", password);
                pInfoBundle.putString("name", name);
                pInfoBundle.putString("localisation", localisation);
                pInfoBundle.putInt("age", age);
                pInfoBundle.putString("sex", sex);
                pInfoIntent.putExtras(pInfoBundle);
                startActivity(pInfoIntent);
                finish();

            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_INTENT_CODE);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void uploadImage() {
        if (imageUri != null) {
            //utworzenie nazwy pliku w bazie danych
            StorageReference fileReference = storageReference.child("Images/" + System.currentTimeMillis()
            + "." +getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 5000); //opóźnia ładowanie proressbara o 5000ms
                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            UploadImage uploadImage = new UploadImage(taskSnapshot.getStorage().getDownloadUrl().toString());
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(uploadImage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        //jeśli będę chciała dodać progresBar to tu
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(mainPicture);

        }
    }
    */
}