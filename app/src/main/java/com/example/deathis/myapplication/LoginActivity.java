package com.example.deathis.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextLogin, editTextPass;
    private Button btnLogin, btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextLogin = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPass);
        btnLogin = findViewById(R.id.buttonLogin);
        btnReg = findViewById(R.id.buttonReg);


        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);


        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this,
                    MapsActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLogin) {
            int i = 0;
            if (TextUtils.isEmpty(editTextLogin.getText())) {
                i += 1;
                editTextLogin.setError("Це поле не може бути пустим!");
            }
            if (TextUtils.isEmpty(editTextPass.getText())) {
                i += 1;
                editTextPass.setError("Це поле не може бути пустим!");
            }
            if (i == 0) {
                    Login();
            }
        } else if (v.getId() == R.id.buttonReg) {
            Register();
        }
    }

    private void Register() {
        startActivity(new Intent(LoginActivity.this, RegActivity.class));
        finish();
    }

    private void Login() {
        mAuth.signInWithEmailAndPassword(editTextLogin.getText().toString(),
                editTextPass.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Успішний вхід!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this,
                                    MapsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Виникла помилка! Попробуйте будь ласка пізніше",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}