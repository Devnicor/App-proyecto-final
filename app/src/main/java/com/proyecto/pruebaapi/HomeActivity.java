package com.proyecto.pruebaapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE = 2;
    private ImageView imageView;
    private Uri photoURI;
    private File currentPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageView = findViewById(R.id.view_image);
        Button btnTakePhoto = findViewById(R.id.btn_take_photo);
        Button btnUploadPhoto = findViewById(R.id.btn_load_photo); // Botón para subir imagen
        Button btnMaps = findViewById(R.id.btn_maps);
        Button btnIA = findViewById(R.id.btn_IA);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(); // Abrir la galería
            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(intent); }
        });

        btnIA.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent); }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                currentPhotoFile = createImageFile(); // Crear un archivo de imagen
            } catch (IOException ex) {
                // Manejar el error
            }
            if (currentPhotoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.camara_java.fileprovider", // Asegúrate de tener esto en tu AndroidManifest.xml
                        currentPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // La imagen se ha guardado en currentPhotoFile
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoFile.getAbsolutePath());
            imageView.setImageBitmap(imageBitmap); // Muestra la imagen en el ImageView
            // Aquí puedes agregar lógica adicional, como enviar la imagen a tu API
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage); // Muestra la imagen seleccionada
            // Aquí también puedes agregar lógica para manejar la imagen seleccionada
        }
    }
}

