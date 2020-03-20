package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Navigate extends AppCompatActivity {
    public Button upload;
    public Button download;
    public Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        upload= findViewById(R.id.button3);
        download=findViewById(R.id.button4);
        logout=findViewById(R.id.button5);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u=new Intent(Navigate.this,uploadactivity.class);
                startActivity(u);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent d=new Intent(Navigate.this,downloadactivity.class);
                startActivity(d);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log=new Intent(Navigate.this,Loginactivity.class);
                startActivity(log);
            }
        });
    }
}
