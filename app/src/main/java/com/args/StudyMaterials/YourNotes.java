package com.args.StudyMaterials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class YourNotes extends AppCompatActivity implements NotesAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Notes> mUploads;
    ProgressBar progressBar;

    //for download functionality
    FirebaseStorage fstr;
    StorageReference storageReference1;
    StorageReference ref;
    FirebaseDatabase fdb;
    String download_filename="";


    //for accessing user id
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    LinearLayout no_notes_added;

    //for search functionality
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_notes);

        mRecyclerView = findViewById(R.id.your_notes_recycler_view);
        progressBar = findViewById(R.id.your_notes_progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String user_id = firebaseAuth.getUid();


        no_notes_added = findViewById(R.id.no_notes_linear_layout);
        no_notes_added.setVisibility(View.GONE);

        search = findViewById(R.id.yournotes_search);


        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(user_id);
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
                mAdapter = new NotesAdapter(YourNotes.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(YourNotes.this);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);

                if(mAdapter.getItemCount()!=0){
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    no_notes_added.setVisibility(View.VISIBLE);
                    Toast.makeText(YourNotes.this, "NO items", Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(YourNotes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }


        });

        search.addTextChangedListener(new TextWatcher() {
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

    //for search edittext
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


    @Override
    public void OnItemClick(String filename, final String download_name)
    {

        storageReference1=fstr.getInstance().getReference("Uploads");
        ref=storageReference1.child(filename);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfiles(YourNotes.this,download_name,".pdf",DIRECTORY_DOWNLOADS,url);

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
