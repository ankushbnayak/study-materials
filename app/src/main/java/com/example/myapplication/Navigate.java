package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Navigate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Button upload;
    public Button download;
    public Button logout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    //for navigation drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        upload= findViewById(R.id.button3);
        download=findViewById(R.id.button4);
        logout=findViewById(R.id.button5);


        mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer);

        mToggle = new ActionBarDrawerToggle(Navigate.this,mDrawerLayout,//toolbar,
                R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationview = (NavigationView) findViewById(R.id.navigation_view);
        navigationview.setNavigationItemSelectedListener(this);

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


    //when the navigation drawer is clicked and this is used for toggling the event
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id = menuItem.getItemId();
        if(id == R.id.download_notes)
        {
            Intent download = new Intent(Navigate.this,downloadactivity.class);
            startActivity(download);
            return true;
        }
        else if(id == R.id.upload_notes)
        {
            Intent upload = new Intent(Navigate.this,uploadactivity.class);
            startActivity(upload);
            return true;
        }
        else if(id == R.id.about_us)
        {
            //add your own intent or anything else
            return true;
        }

        else if(id == R.id.rate_us)
        {
            //add your own intent or feature but remember to change in the menu
            return true;
        }
        else if(id == R.id.sign_out)
        {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Intent signout = new Intent(Navigate.this,Loginactivity.class);
            startActivity(signout);
            finish();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null)
        {
            //incase if the user is not signed in
            Intent notsignedin = new Intent(Navigate.this,Loginactivity.class);
            startActivity(notsignedin);
            finish();
        }
    }

}
