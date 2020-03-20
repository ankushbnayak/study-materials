package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginactivity extends AppCompatActivity {
    public EditText email;
    public  EditText pass;
    public Button login;
    public FirebaseAuth mfire;
    public TextView signup;
    public TextView forgot;

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid=email.getText().toString();
                String password=pass.getText().toString();
                if(emailid.isEmpty())
                {
                    email.setError("Email is empty");
                    email.requestFocus();
                }
                else if(password.isEmpty())
                {
                    pass.setError("Password is empty");
                    pass.requestFocus();
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
                        }
                        else if(!(task.isSuccessful()))
                        {
                            Toast.makeText(Loginactivity.this,"Login not successfull",Toast.LENGTH_SHORT).show();
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
}
