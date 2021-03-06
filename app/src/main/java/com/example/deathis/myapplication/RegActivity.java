package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deathis.myapplication.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {

    private EditText edFN, edSN, edEM, edPASS, edNK;
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
        edNK = findViewById(R.id.editTextNik);

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
                if (TextUtils.isEmpty(edNK.getText())) {
                    i += 1;
                    edNK.setError("Це поле не може бути пустим!");
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
                            User user = new User();
                            user.setEmail(edEM.getText().toString());
                            user.setFirst_name(edFN.getText().toString());
                            user.setPass(edPASS.getText().toString());
                            user.setSecond_name(edSN.getText().toString());
                            user.setUid(mAuth.getUid());
                            user.setNik(edNK.getText().toString());
                            user.setImageURL("def");
                            user.setStatus("offline");

                            myRef.child("users").child(mAuth.getUid()).setValue(user);
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

