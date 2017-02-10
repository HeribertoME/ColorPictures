package com.hmelizarraraz.colorpictures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void takePhoto(View view) {
        Toast.makeText(this, "Foto", Toast.LENGTH_SHORT).show();
    }

    public void takeVideo(View view) {
        Toast.makeText(this, "Video", Toast.LENGTH_SHORT).show();
    }

    public void showPhotos(View view) {
        Toast.makeText(this, "Galeria fotos", Toast.LENGTH_SHORT).show();
    }

    public void showVideos(View view) {
        Toast.makeText(this, "Galeria videos", Toast.LENGTH_SHORT).show();
    }
}
