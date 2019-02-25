package com.example.ms.gps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText e3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        e3 = findViewById(R.id.editText3);

        Intent i = getIntent();
        if (i!=null){
            email = i.getStringExtra("email");
        }
    }

    public void namePicActivity(View view) {
        if (e3.getText().toString().length() > 6){
            Intent intent = new Intent(PasswordActivity.this, NameActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", e3.getText().toString());
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "password length should be more than 6 characters", Toast.LENGTH_SHORT).show();
        }
    }
}
