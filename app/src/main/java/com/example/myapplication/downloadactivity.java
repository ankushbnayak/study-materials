package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class downloadactivity extends AppCompatActivity {
    Button download;
    EditText editText;
    FirebaseStorage fstr;
    StorageReference storageReference1;
    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadactivity);
        download=findViewById(R.id.button8);
        editText=findViewById(R.id.editText6);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();

            }
        });
    }
    public void download()
    {
        storageReference1=fstr.getInstance().getReference("Uploads");
        ref=storageReference1.child(editText.getText().toString());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfiles(downloadactivity.this,editText.getText().toString(),".pdf",DIRECTORY_DOWNLOADS,url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public  void downloadfiles(Context context,String fileName,String fileExtension,String destinationDirectory,String url)
    {
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);
        downloadManager.enqueue(request);
    }
}
