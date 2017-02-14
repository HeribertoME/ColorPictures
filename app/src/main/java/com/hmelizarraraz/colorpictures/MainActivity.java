package com.hmelizarraraz.colorpictures;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_WRITE_PERMISSION = 11;
    private static final int VIDEO_WRITE_PERMISSION = 12;
    private Uri mediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // Manejar la información
            if (requestCode == Constants.PETICION_FOTO) {
                // Ver la foto
                Intent intent = new Intent(this, ImageActivity.class);
                intent.setData(mediaUri);
                startActivity(intent);
            }

            if (requestCode == Constants.PETICION_VIDEO) {
                // Reproducir video
                Intent intent = new Intent(Intent.ACTION_VIEW, mediaUri);
                intent.setDataAndType(mediaUri, "video/*");
                startActivity(intent);
            }

            if (requestCode == Constants.PETICION_GALERIA_FOTOS) {
                // Mostrar la galeria de fotos
                Intent intent = new Intent(this, ImageActivity.class);
                intent.setData(data.getData());
                startActivity(intent);
            }

            if (requestCode == Constants.PETICION_GALERIA_VIDEOS) {
                // Mostrar  la galeria de videos
                Intent intent = new Intent(this, VideoActivity.class);
                intent.setData(data.getData());
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, "Ocurrio un error! :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void takePhoto(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    mostarExplicacion(Constants.PETICION_FOTO);

                } else {

                    // Se solicita el permiso
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_WRITE_PERMISSION);
                }

            } else {

                // No es necesario el permiso
                crearMedio(Constants.PETICION_FOTO);

            }

        } else {
            crearMedio(Constants.PETICION_FOTO);
        }

    }

    private void mostarExplicacion(final int tipoPeticion) {

        new AlertDialog.Builder(this)
                .setTitle("Se necesita el permiso")
                .setMessage("Se necesita el permiso para poder guardar las fotos y video")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Pedir permiso
                        if (tipoPeticion == Constants.PETICION_FOTO){
                            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_WRITE_PERMISSION);
                        } else if (tipoPeticion == Constants.PETICION_VIDEO){
                            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, VIDEO_WRITE_PERMISSION);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, ":(", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Se iniciara la camara de fotos
            if (requestCode == CAMERA_WRITE_PERMISSION) {

                crearMedio(Constants.PETICION_FOTO);

            }

            // Se iniciara la camara de videos
            if (requestCode == VIDEO_WRITE_PERMISSION) {

                crearMedio(Constants.PETICION_VIDEO);

            }


        }

    }

    public void takeVideo(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    mostarExplicacion(Constants.PETICION_VIDEO);

                } else {

                    // Se solicita el permiso
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, VIDEO_WRITE_PERMISSION);
                }

            } else {

                // No es necesario el permiso
                crearMedio(Constants.PETICION_VIDEO);

            }

        } else {
            crearMedio(Constants.PETICION_VIDEO);
        }

    }

    private void crearMedio(int tipoPeticion) {

        try {

            if (tipoPeticion == Constants.PETICION_FOTO)
                mediaUri = crearArchivoMedio(Constants.MEDIA_FOTO);
            else if (tipoPeticion == Constants.PETICION_VIDEO)
                mediaUri = crearArchivoMedio(Constants.MEDIA_VIDEO);
            else
                throw new IllegalArgumentException();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mediaUri == null) {
            Toast.makeText(this, "Hubo un problema al crear el medio", Toast.LENGTH_SHORT).show();
        } else {
            iniciarCamara(mediaUri, tipoPeticion);
        }
    }

    private void iniciarCamara(Uri mediaUri, int tipoPeticion) {

        Intent intent;

        if (tipoPeticion == Constants.PETICION_VIDEO) {
            intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.MAX_DURATION);
            startActivityForResult(intent, tipoPeticion);

        } else if (tipoPeticion == Constants.PETICION_FOTO){
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            startActivityForResult(intent, tipoPeticion);
        } else {
            throw new IllegalArgumentException();
        }

    }

    public void showPhotos(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.PETICION_GALERIA_FOTOS);
    }

    public void showVideos(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, Constants.PETICION_GALERIA_VIDEOS);
    }

    /**
     * Método para crear un archivo tipo jpg o mp4
     * @param tipoMedio foto: Creara foto. video: Creara video
     * @return Uri
     * @throws IOException
     */
    private Uri crearArchivoMedio(int tipoMedio) throws IOException {

        if (!almacenamientoExternoDisponible())
            return null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreArchivo;
        File archivo;

        if (tipoMedio == Constants.MEDIA_FOTO) {

            nombreArchivo = "IMG_" + timeStamp + "_";

            File directorioAlmacenamiento = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            archivo = File.createTempFile(nombreArchivo, ".jpg", directorioAlmacenamiento);

            Log.d("TAG", archivo.getAbsolutePath());

            MediaScannerConnection.scanFile(this, new String[] { archivo.getPath() }, new String[] { "image/jpeg", "video/mp4"}, null);

            return Uri.fromFile(archivo);

        } else if (tipoMedio == Constants.MEDIA_VIDEO){

            nombreArchivo = "MOV_" + timeStamp + "_";

            File directorioAlmacenamiento = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

            archivo = File.createTempFile(nombreArchivo, ".mp4", directorioAlmacenamiento);

            Log.d("TAG", archivo.getAbsolutePath());

            MediaScannerConnection.scanFile(this, new String[] { archivo.getPath() }, new String[] { "image/jpeg", "video/mp4"}, null);

            return Uri.fromFile(archivo);

        } else {
            return null;
        }

    }

    private boolean almacenamientoExternoDisponible() {

        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
