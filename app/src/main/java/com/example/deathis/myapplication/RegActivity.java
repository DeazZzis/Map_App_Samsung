package com.example.deathis.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegActivity extends AppCompatActivity {

    private EditText edFN, edSN, edEM, edPASS;
    private Button btn_sing_up;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        btn_sing_up = findViewById(R.id.buttonSingUp);
        edFN = findViewById(R.id.editTextFirstName);
        edSN = findViewById(R.id.editTextSecondName);
        edEM = findViewById(R.id.editTextEmail);
        edPASS = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference();

        btn_sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if (TextUtils.isEmpty(edFN.getText())) {
                    i += 1;
                    edFN.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edSN.getText())) {
                    i += 1;
                    edSN.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edEM.getText())) {
                    i += 1;
                    edEM.setError("Це поле не може бути пустим!");
                }
                if (TextUtils.isEmpty(edPASS.getText())) {
                    i += 1;
                    edPASS.setError("Це поле не може бути пустим!");
                }
                if (i == 0) {
                    Registration();
                }
            }
        });
    }

    private void Registration() {
        mAuth.createUserWithEmailAndPassword(
                edEM.getText().toString(),
                edPASS.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Реєстрація успішна!",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Реєстрація провалиласяс! Попробуйте ще раз пізніше",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

