package com.example.ms.gps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1, e2;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1=findViewById(R.id.editText);
        e2=findViewById(R.id.editText2);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }

    public void Login(View view) {
        dialog.setMessage("Please wait!!");
        dialog.show();
        auth.signInWithEmailAndPassword(e1.getText().toString(), e2.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                           // Toast.makeText(getApplicationContext(), "user logged in successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
//                            if (user.isEmailVerified()){
                                Intent i = new Intent(LoginActivity.this, UserLocationMainActivity.class);
                                startActivity(i);
                                finish();
//                            }
//                            else {
//                                 Toast.makeText(getApplicationContext(), "email is not verified yet", Toast.LENGTH_SHORT).show();
//                                 }


                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}
