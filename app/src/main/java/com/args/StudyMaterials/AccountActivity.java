package com.args.StudyMaterials;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {

    ImageView user_image;
    TextView user_name_display,user_email_account;
    Button signout;
    FirebaseAuth mfirebaseAuth;
    FirebaseUser mfirebaseuser;

    Button about, contact,rate,share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //creating ids
        user_name_display = findViewById(R.id.user_name_display);
        user_email_account = findViewById(R.id.user_email_display);
        user_image = findViewById(R.id.user_image_display);
        signout = findViewById(R.id.sign_out_button);

        // for the support part
        about = findViewById(R.id.account_section_about);
        contact = findViewById(R.id.account_section_contact);
        share = findViewById(R.id.share_yoth_of_gsb);
        rate = findViewById(R.id.rate_yoth_of_gsb);




        //instance creating
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseuser = mfirebaseAuth.getCurrentUser();
        Uri user_image_url = mfirebaseuser.getPhotoUrl();
        String user_name = mfirebaseuser.getDisplayName();
        String user_id = mfirebaseuser.getEmail();
        user_email_account.setText(user_id);
        user_name_display.setText(user_name);

        Picasso.get()
                .load(user_image_url)
                .centerCrop()
                .fit()
                .into(user_image);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Sign out of your account!")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mfirebaseAuth.signOut();
                                Intent signout_intent = new Intent(AccountActivity.this,ChooseLogin.class);
                                startActivity(signout_intent);
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });

        //when the about button in support part is clicked
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about_section = new Intent(AccountActivity.this,WebActivity.class);
                String url_about ="https://hosting-for-others.web.app/about(notes-kernal).html";
                about_section.putExtra("website",url_about);
                startActivity(about_section);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent feedback_section = new Intent(AccountActivity.this,WebActivity.class);
                String url = "https://hosting-for-others.web.app/about(notes-kernal).html";
                feedback_section.putExtra("website",url);
                startActivity(feedback_section);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.args.StudyMaterials";
                Intent rate_us = new Intent(Intent.ACTION_VIEW);
                rate_us.setData(Uri.parse(url));
                startActivity(rate_us);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareapp = new Intent(Intent.ACTION_SEND);
                shareapp.setType("text/plain");
                String body = "Check this app out! its the NotesKernal app \nDownload from the playstore now\n\nhttps://play.google.com/store/apps/details?id=com.args.StudyMaterials";
                String sub = "Download the NOTES KERNAL";
                shareapp.putExtra(Intent.EXTRA_SUBJECT,sub);
                shareapp.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(shareapp, "Share Using"));
            }
        });
    }
}
