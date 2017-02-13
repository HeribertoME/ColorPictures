package com.hmelizarraraz.colorpictures;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView vvVideo = (VideoView) findViewById(R.id.vvVideo);

        Intent intent = getIntent();

        Uri uriVideo = intent.getData();

        vvVideo.setVideoURI(uriVideo);
        vvVideo.start();
    }
}
