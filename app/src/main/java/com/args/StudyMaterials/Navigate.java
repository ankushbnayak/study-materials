package com.args.StudyMaterials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.internal.DialogRedirect;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Navigate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        NotesAdapter.OnItemClickListener {


    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    //for navigation drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    //for loading recycler view
    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Notes> mUploads;
    ProgressBar progressBar;


    //for adding download functionality
    FirebaseStorage fstr;
    StorageReference storageReference1;
    StorageReference ref;
    FirebaseDatabase fdb;
    String download_filename="";


    //for adding serch functionality
    EditText search_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);


        search_bar = findViewById(R.id.navigate_search);
        mDrawerLayout =(DrawerLayout)findViewById(R.id.drawer);

        mToggle = new ActionBarDrawerToggle(Navigate.this,mDrawerLayout,//toolbar,
                R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationview = (NavigationView) findViewById(R.id.navigation_view);
        navigationview.setNavigationItemSelectedListener(this);

        notes_load();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }
    private void filter(String text)
    {
        //ArrayList<Upload> filterList = new ArrayList<Upload>();
        try {
            ArrayList<Notes> filterList = new ArrayList<>();
            for (Notes item: mUploads){
                if(item.getNotes_name().toLowerCase().contains(text.toLowerCase()))
                {
                    filterList.add(item);
                }
            }
            mAdapter.filterLists(filterList);

        }
        catch (Exception e)
        {
            //Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show();
        }


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
            Intent download = new Intent(Navigate.this,Navigate.class);
            startActivity(download);
            finish();
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
            new AlertDialog.Builder(this)
                    .setTitle("Sign out!")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.signOut();
                            Intent signout = new Intent(Navigate.this,ChooseLogin.class);
                            startActivity(signout);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

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


    private void notes_load()
    {
        mRecyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.home_progressbar);

        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notes");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));

        progressBar.setVisibility(View.VISIBLE);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this clear functionality avoids duplication of data
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    Notes upload = postSnapshot.getValue(Notes.class);
                    mUploads.add(upload);
                }


                //mAdapter.notifyDataSetChanged();
                mAdapter = new NotesAdapter(Navigate.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(Navigate.this);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Navigate.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }


        });
    }

    @Override
    public void OnItemClick(final String filename, final String Download_name)
    {

        //Toast.makeText(this, ""+filename, Toast.LENGTH_SHORT).show();

        storageReference1=fstr.getInstance().getReference("Uploads");
        ref=storageReference1.child(filename);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfiles(Navigate.this,Download_name,".pdf",DIRECTORY_DOWNLOADS,url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public  void downloadfiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url)
    {
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);
        downloadManager.enqueue(request);
        Toast.makeText(context, "Notes has been downloaded successfully", Toast.LENGTH_SHORT).show();
    }
}
