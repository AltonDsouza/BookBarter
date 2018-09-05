package com.example.alton.myapplication.LoginModule;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alton.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnReset,back;
    private EditText editTextEmail;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        firebaseAuth= FirebaseAuth.getInstance();

        editTextEmail=(EditText)findViewById(R.id.email);

        btnReset=(Button)findViewById(R.id.buttonReset);
        back=(Button)findViewById(R.id.buttonLogin);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        btnReset.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==findViewById(R.id.buttonReset)){
            String email=editTextEmail.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextEmail.setError("Please enter valid email");
                return;
            }
         progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ResetActivity.this, "We have sent you instructions to reset your password", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(ResetActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }
        if(v==findViewById(R.id.buttonLogin)){
            finish();
        }
    }
}
