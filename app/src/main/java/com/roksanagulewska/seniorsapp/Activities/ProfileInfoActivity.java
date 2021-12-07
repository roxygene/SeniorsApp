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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileInfoActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 99;
    public static final int CAMERA_PERMISSION_CODE = 100; //camera permission code
    public static final int GALLERY_REQUEST_CODE = 101;


    private Uri contentUri;
    String imageUri;
    String imageUrl;
    DataBaseHelper dbHelper = new DataBaseHelper();
    User user;
    String currentPhotoPath;
    String fileName;

    ImageView mainPicture;
    Button cameraBtn, galleryBtn, confirmBtn;
    EditText descriptionEditTxt;

    String userAge;
    String userMinAge;
    String userMaxAge;

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
                askCameraPermission();
            } // prośba o dostęp do aparatu
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE); //otwarcie galerii
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionEditTxt.getText().toString().trim(); //pobranie wpisanego przez użytkownika opisu

                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Description is required", Toast.LENGTH_SHORT).show();
                } else {
                    if (description.length() > 250) {
                        Toast.makeText(getApplicationContext(), "Description is too long.", Toast.LENGTH_SHORT).show();
                    } else {
                        userAge = Integer.toString(age);
                        userMinAge = Integer.toString(minAge);
                        userMaxAge = Integer.toString(maxAge);
                        user = new User(dbHelper.getCurrentUserId(), email, password, name, userAge, sex, localisation, preferredSex, description, fileName, imageUri, userMinAge, userMaxAge);
                        //utworzenie obiektu user z wczytanymi z bundle'a i wprowadzonymi w tej aktywności wartościami pól
                        dbHelper.addUserToDB(user).addOnSuccessListener(success-> //wywołanie metody dodającej użytkownika do bazy ze sprawdzeniem czy się udało
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
                }

            }
        });
    }

    //zapytanie o pozwolenie na dostęp do aparatu
    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
                dispatchTakePictureIntent();
        }
    }

    //sprawdzenie pozwolenia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(currentPhotoPath);
                mainPicture.setImageURI(Uri.fromFile(file));
                Log.d("CAMX", "Absolute URL of Image is " + Uri.fromFile(file));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                fileName = file.getName();
                uploadImageToFirebase(file.getName(), contentUri);
            }
        }else if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "image_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("CAMX", "onActivityResult: Gallery Image Uri: " + imageFileName);
                mainPicture.setImageURI(contentUri);
                fileName = imageFileName;
                uploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }

    //pozyskanie rozszerzenia pliku
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    //utworzenie pliku obrazu
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "image_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, //prefix
                ".jpg",// suffix
                storageDir // directory

        );

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
                        "com.roksanagulewska.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE); //REQUEST_TAKE_PHOTO
            }
        }
    }

    public void uploadImageToFirebase(String fileName, Uri contentUri) {
        StorageReference imageStorageReference = dbHelper.getStorageReference().child("Pictures").child(fileName);

            imageStorageReference.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("CAMX", "onSuccess: Uploaded image URL is " + uri.toString());
                            imageUri = uri.toString();
                            imageUrl = imageStorageReference.getDownloadUrl().toString();
                            Log.d("CAMX", "onSuccess: Uploaded image URL2 is " + imageUrl);
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT);
                }
            });



    }
}