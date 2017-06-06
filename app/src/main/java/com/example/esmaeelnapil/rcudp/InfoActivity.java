package com.example.esmaeelnapil.rcudp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class InfoActivity extends AppCompatActivity {

    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_info);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            back =(ImageButton)findViewById(R.id.imageButton9);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent mainone = new Intent();
                    mainone.setClass(InfoActivity.this, MainActivity.class);
                    mainone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainone);
                    */
                    finish();

                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {Snackbar.make(view, "Call for more info", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            /////////////////////////////////////////////////////////////////////////////////////////////////////












            //////////////////////////////////////////////////////////////////////////////////////////////////////
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
