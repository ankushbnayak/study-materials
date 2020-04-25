package com.example.myapplication;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Notes> muploads;
    private OnItemClickListener mListener;

    public NotesAdapter(Context context, List<Notes> uploads) {
        mContext = context;
        muploads = uploads;

    }

    //this is the adapter for the news activity
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.notes_item, parent, false);
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
                Toast.makeText(mContext, "Downloading...", Toast.LENGTH_SHORT).show();
                mListener.OnItemClick(notes_name);
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


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName_branch = itemView.findViewById(R.id.branch_name_of_notes);
            textViewName_notes_name = itemView.findViewById(R.id.notes_name_of_notes);
            download = itemView.findViewById(R.id.download_notes_from_url);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            //mListener . OnItemClick(title,news,time,image_uri);
        }
    }

    public interface OnItemClickListener {
        //,int release_date
        void OnItemClick(String filename);
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
