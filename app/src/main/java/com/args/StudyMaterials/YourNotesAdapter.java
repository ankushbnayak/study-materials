package com.args.StudyMaterials;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class YourNotesAdapter extends RecyclerView.Adapter<YourNotesAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Notes> muploads;
    private OnItemClickListener mListener;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    public YourNotesAdapter(Context context, List<Notes> uploads) {
        mContext = context;
        muploads = uploads;

    }

    //this is the adapter for the news activity
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.your_notes_item, parent, false);
        return new ImageViewHolder(v);

    }


    //this part helps us to show the images in the recycler view...
    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final Notes uploadCusrrent = muploads.get(position);
        holder.textViewName_branch.setText(uploadCusrrent.getNotes_branch());
        holder.textViewName_notes_name.setText(uploadCusrrent.getNotes_name());

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes_name = uploadCusrrent.getNotes_url();
                String file_name = uploadCusrrent.getNotes_name();
                Toast.makeText(mContext, "Downloading...", Toast.LENGTH_SHORT).show();
                mListener.OnItemClick(notes_name,file_name);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String notes_name = uploadCusrrent.getNotes_url();
                firebaseAuth=FirebaseAuth.getInstance();
                firebaseUser=firebaseAuth.getCurrentUser();
                String user_id=firebaseUser.getUid();
                final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(user_id);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String notes_name = uploadCusrrent.getNotes_url();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Notes notes=ds.getValue(Notes.class);
                            if(notes_name==notes.getNotes_url()){
                                String dref1=ds.getKey();
                                databaseReference.child(dref1).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("notes");
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String notes_name = uploadCusrrent.getNotes_url();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            Notes notes1=ds.getValue(Notes.class);
                            if(notes_name==notes1.getNotes_url()){
                                String dref=ds.getKey();
                                databaseReference1.child(dref).removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }


    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName_branch, textViewName_notes_name;
        ImageView download;
        ImageView delete;


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName_branch = itemView.findViewById(R.id.branch_name_of_notes);
            textViewName_notes_name = itemView.findViewById(R.id.notes_name_of_notes);
            download = itemView.findViewById(R.id.download_notes_from_url);
            delete=itemView.findViewById(R.id.delete_notes_from_url);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            //mListener . OnItemClick(title,news,time,image_uri);
        }
    }

    public interface OnItemClickListener {
        //,int release_date
        void OnItemClick(String filename,String download_name);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }

    public void filterLists(ArrayList<Notes> filterList)
    {
        try {
            muploads = filterList;
            notifyDataSetChanged();

        }catch (Exception e)
        {
            Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();
        }

    }
}
