package com.example.utacommunityapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.utacommunityapp.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView userPhoto;
    static int PreqCode = 1;
    static int REQUSTCODE = 1;
    Uri pickedImage;
    DatabaseReference data;
    private EditText userPassword, userPassword2, userEmail,userName,userNetId;
    private ProgressBar loading;
    private Button register;
    member member;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialzing

        userName = findViewById(R.id.registerName);
        userNetId = findViewById(R.id.registerNetid);
        userEmail = findViewById(R.id.registerEmail);
        userPassword = findViewById(R.id.registerPassword);
        userPassword2 = findViewById(R.id.registerConfirm);
        register = findViewById(R.id.registerBtn);
        loading = findViewById(R.id.progressBar3);
        loading.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        data= FirebaseDatabase.getInstance().getReference("User");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                register.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmpass = userPassword2.getText().toString();
                final String netID = userNetId.getText().toString();
                member = new member();

                if(email.isEmpty()|| name.isEmpty()|| password.isEmpty()|| confirmpass.isEmpty()|| netID.isEmpty() )
                {
                    //Something is wrong and I'm going to display error
                    showMessage("Please Check Your Entries!");
                    register.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    return;

                }
                else if (netID.length() < 10)
                {
                    showMessage("Invalid Student ID !");
                    register.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }
                else if(password.equals(confirmpass) == false)
                {
                    showMessage("Password and retype does not match !");
                    register.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }
                else if(password.length()<10||( confirmpass.length()<10))
                {
                    showMessage("Password must be at least 10 characters !");
                    register.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }
                else
                {
                    //Success and now user account can be created
                    //CreateUser method will try to create the user if it's valid
                    member.setName(name);
                    member.setEmail(email);
                    member.setNetID(netID);
                    member.setPassword(password);
                    member.setConfirmpass(confirmpass);
                    data.push().setValue(member);
                    CreateUser(email,password);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);


                }

            }
        });

        userPhoto = findViewById(R.id.registerUserPhoto);

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 21) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();

                }

            }
        });
    }

    private void CreateUser(final String email, String password) {
        //this method will create the user account with the specified name, netID,email and password

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //Success in creating user account
                            showMessage("Account Has Been Created");
                            //After the user account has been created we need to update their info and profile pic
                            updateUser(email,pickedImage,mAuth.getCurrentUser());
                        }
                        else
                        {
                            //Failure in creating user account
                            showMessage("Failure In Creating Account"+task.getException().getMessage());
                            register.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
    private void updateUser(final String names, Uri pickedImage, final FirebaseUser currentUser)
    {
        //uploading user's photo to firebase storage and get it's url
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("User's_Photo");
        final StorageReference image = storage.child(pickedImage.getLastPathSegment());
        image.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image upload was a success

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                    //Uri contains user image url

                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                .setDisplayName(names)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profile)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            //user information has been update was a success
                                            showMessage("Registration Complete");
                                            updateInterface();
                                        }
                                    }
                                });

                    }
                });

            }

            private void updateInterface() {
               Intent home = new Intent(getApplicationContext(),LoginActivity.class);
               startActivity(home);
               finish();

            }
        });

       

    }
    //Showing user error
    private void showMessage(String verify) {
        Toast.makeText(getApplicationContext(), verify, Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //TODO: Open gallery to add user photo
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, REQUSTCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this, "Please accept this required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PreqCode);

            }
        } else {

            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUSTCODE && data != null)
        {
            //user has successfully picked an image
            //TODO: we need to save its referecne to a Uri
            pickedImage = data.getData();
            userPhoto.setImageURI(pickedImage);


        }
    }
}