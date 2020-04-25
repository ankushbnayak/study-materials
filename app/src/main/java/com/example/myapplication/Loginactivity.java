package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginactivity extends AppCompatActivity {
    public EditText email;
    public  EditText pass;
    public Button login;
    public FirebaseAuth mfire;
    FirebaseUser user;
    public TextView signup;
    public TextView forgot;

    //for progressbar
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        email=findViewById(R.id.editText2);
        pass=findViewById(R.id.editText3);
        signup=findViewById(R.id.textView3);
        forgot=findViewById(R.id.textView2);
        login =findViewById(R.id.button);
        mfire=FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.login_progress);
        progressBar.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String emailid=email.getText().toString();
                String password=pass.getText().toString();
                if(emailid.isEmpty())
                {
                    email.setError("Email is empty");
                    email.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
                else if(password.isEmpty())
                {
                    pass.setError("Password is empty");
                    pass.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
                else if(!(emailid.isEmpty()&&password.isEmpty()))
                {

                    mfire.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(Loginactivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Loginactivity.this,Navigate.class);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }
                        else if(!(task.isSuccessful()))
                        {
                            Toast.makeText(Loginactivity.this,"Login not successfull",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });}



            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Loginactivity.this,Signupactivity.class);
                startActivity(i);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(Loginactivity.this,forgetPassword.class);
                startActivity(i2);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mfire.getCurrentUser();
        if(user != null)
        {
            Intent sign= new Intent(Loginactivity.this,Navigate.class);
            startActivity(sign);
            finish();

        }
    }
}
