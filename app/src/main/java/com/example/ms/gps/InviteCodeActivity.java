package com.example.ms.gps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    String name, email, password, date, isSharing, code, lat, lng;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog dialog;
    String userId;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_images;");

        Intent i = getIntent();
        if (i!=null){
            name = i.getStringExtra("name");
            email = i.getStringExtra("email");
            password = i.getStringExtra("password");
            date = i.getStringExtra("date");
            isSharing = i.getStringExtra("isSharing");
            code = i.getStringExtra("code");
            imageUri = i.getParcelableExtra("imageUri");
            }
         t1.setText(code);
    }

    public void registerUser(View view) {
        dialog.setMessage("Please wait!! while we are creating an account for you");
        dialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = auth.getCurrentUser();
                            // insert values in realtime database
                            CreateUser createUser = new CreateUser(name, password, email, code, "false", "na", "na", "na", user.getUid());
                            user = auth.getCurrentUser();
                            userId = user.getUid();

                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                // save image to firebase storage
                                                StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                                sr.putFile(imageUri)
                                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    String download_path = task.getResult().getDownloadUrl().toString();
                                                                    reference.child(user.getUid()).child("imageUrl").setValue(download_path)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        dialog.dismiss();
                                                                                        Intent i = new Intent(InviteCodeActivity.this, UserLocationMainActivity.class);
                                                                                        Toast.makeText(getApplicationContext(), "code is "+code, Toast.LENGTH_SHORT).show();
                                                                                        i.putExtra("email", email);
                                                                                        i.putExtra("password", password);
                                                                                        i.putExtra("name", name);
                                                                                        i.putExtra("isSharing", isSharing);
                                                                                        i.putExtra("code", code);
                                                                                        i.putExtra("imageUri", imageUri);
                                                                                        startActivity(i);
                                                                                        finish();

                                                                                    }
                                                                                    else {
                                                                                        dialog.dismiss();
                                                                                        Toast.makeText(getApplicationContext(), "An error occurred while creating account", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                }
                                            else {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "could not insert value in database", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
    public void sendVerificationEmail(){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "email sent for verification", Toast.LENGTH_SHORT).show();
                    finish();
                    auth.signOut();
                }
                else {
                    Toast.makeText(getApplicationContext(), "cannot send email", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
