package com.hmelizarraraz.colorpictures;

import android.content.Intent;
import android.provider.MediaStore;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // Manejar la información
        } else {
            Toast.makeText(this, "Ocurrio un error! :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void takePhoto(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.PETICION_FOTO);


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
