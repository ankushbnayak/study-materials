package com.args.StudyMaterials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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

public class Signupactivity extends AppCompatActivity {
    public EditText email1;
    public EditText password1;
    public Button signup;
    public FirebaseAuth mfire1;
    TextView go_login;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        email1=findViewById(R.id.editText4);
        password1=findViewById(R.id.editText5);
        go_login = findViewById(R.id.jump_login);
        signup=findViewById(R.id.button2);

        progressBar = findViewById(R.id.signup_progress);
        progressBar.setVisibility(View.GONE);
        mfire1=FirebaseAuth.getInstance();
         signup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 progressBar.setVisibility(View.VISIBLE);
                 String e=email1.getText().toString();
                 String p=password1.getText().toString();
                 if(e.isEmpty())
                 {
                     progressBar.setVisibility(View.GONE);
                     email1.setError("Email is empty");
                     email1.requestFocus();
                 }
                 else if(p.isEmpty())
                 {
                     progressBar.setVisibility(View.GONE);
                     password1.setError("Password is empty");
                     password1.requestFocus();
                 }
                 else if(!(e.isEmpty()&&p.isEmpty()))
                 {
                     mfire1.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(Signupactivity.this,"Sign up successfull",Toast.LENGTH_SHORT).show();
                                 Intent intent= new Intent(Signupactivity.this,Navigate.class);
                                 startActivity(intent);
                                 progressBar.setVisibility(View.GONE);
                                 finish();
                             }
                             else if(!(task.isSuccessful()))
                             {
                                 Toast.makeText(Signupactivity.this,"Sign up unsuccessfull",Toast.LENGTH_SHORT).show();
                                 progressBar.setVisibility(View.GONE);
                             }
                         }
                     });
                 }
             }
         });


         go_login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                Intent log = new Intent(Signupactivity.this,Loginactivity.class);
                startActivity(log);
             }
         });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
