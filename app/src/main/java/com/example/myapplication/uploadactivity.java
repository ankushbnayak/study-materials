package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadactivity extends AppCompatActivity {
   public  Button choose;
   public Button upload;
   public TextView text;
   public FirebaseStorage fstore;
   public FirebaseDatabase fdb;

   ProgressDialog pd;
   Uri pdfuri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadactivity);
        fstore=FirebaseStorage.getInstance();
        fdb=FirebaseDatabase.getInstance();
        choose=findViewById(R.id.button6);
        upload=findViewById(R.id.button7);
        text=findViewById(R.id.textView5);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(uploadactivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                  selectpdf();
                }
                else
                {
                    ActivityCompat.requestPermissions(uploadactivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfuri!=null)
                {
                    uploadfile(pdfuri);
                }
                else
                {
                    Toast.makeText(uploadactivity.this,"Select a file",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void uploadfile(Uri pdfuri)
    {
        pd=new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("Uploading file....");
        pd.setProgress(0);
        pd.show();
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference=fstore.getReference();//get path where file will be stored in firebase
        storageReference.child("Uploads").child(fileName).putFile(pdfuri)//make sub directory to store the files
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(); //PROBLEM
                DatabaseReference databaseReference=fdb.getReference();//path to root
                databaseReference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(uploadactivity.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                        }
                        else if(!(task.isSuccessful()))
                        {
                            Toast.makeText(uploadactivity.this,"File upload unsuccessful2",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(uploadactivity.this,"File upload unsuccessful",Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                pd.setProgress(currentProgress);



            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectpdf();
        }
        else
        {
            Toast.makeText(uploadactivity.this,"Provide permission",Toast.LENGTH_SHORT).show();
        }
    }

    private void selectpdf()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            pdfuri=data.getData();
            text.setText(pdfuri.toString());

        }
        else
        {
            Toast.makeText(uploadactivity.this,"Please select a file",Toast.LENGTH_SHORT).show();
        }

    }
}