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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e4;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();

        e4=findViewById(R.id.editText4);
        dialog=new ProgressDialog(this);
    }

    public void passwordActivity(View view) {
        dialog.setMessage("checking email");
        dialog.show();
        //check if exists ??
        auth.fetchProvidersForEmail(e4.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            boolean check = !task.getResult().getProviders().isEmpty();
                            if (!check)
                            {   dialog.dismiss();
                                //email doesn't exists so you can create a new user
                                Intent i = new Intent(RegisterActivity.this, PasswordActivity.class);
                                i.putExtra("email", e4.getText().toString());
                                startActivity(i);
                                finish();
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "email is already registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
