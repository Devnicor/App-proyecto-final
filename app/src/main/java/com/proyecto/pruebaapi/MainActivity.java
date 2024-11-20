package com.proyecto.pruebaapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;
    private TextView textViewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        Button btnHome = findViewById(R.id.btn_back);
        textViewResponse = findViewById(R.id.textViewResponse);

        // Cargar la imagen desde los recursos
        loadImageFromResources();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApiWithImage();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent); }
        });
    }

    private void loadImageFromResources() {
        // Cargar la imagen desde los recursos
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        imageView.setImageBitmap(bitmap);
    }

    private File createImageFile(Bitmap bitmap) {
        File file = new File(getCacheDir(), "temp_image.jpg"); // Crea un archivo temporal
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // Guardar el bitmap en el archivo
        } catch (IOException e) {
            Log.e("MainActivity", "Error al crear el archivo", e);
        }
        return file;
    }

    private void callApiWithImage() {
        Log.d("MainActivity", "Llamada a la API iniciada");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        File file = createImageFile(bitmap);

        // Crear el RequestBody y el MultipartBody.Part para la imagen
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/jpeg"));
        MultipartBody.Part image = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        // Realizar la llamada a la API
        BacheFinderApi api = BacheFinderClient.getApi();
        Call<PredictionResponse> call = api.predict(image);
        call.enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                Log.d("MainActivity", "Respuesta de la API recibida");
                if (response.isSuccessful()) {
                    // Manejar la respuesta exitosa
                    PredictionResponse predictionResponse = response.body();

                    // Asegúrate de que PredictionResponse tenga un método para obtener la predicción
                    if (predictionResponse != null) {
                        Log.d("MainActivity", "Respuesta exitosa: " + predictionResponse.getPrediction());
                        textViewResponse.setText(predictionResponse.getPrediction());
                    } else {
                        Log.e("MainActivity", "La respuesta es nula");
                    }
                } else {
                    Log.e("MainActivity", "Error en la respuesta: " + response.code());
                    try {
                        // Log de la respuesta del cuerpo en caso de error
                        String errorBody = response.errorBody().string();
                        Log.e("MainActivity", "Cuerpo de error: " + errorBody);
                        textViewResponse.setText("Error en la respuesta: " + response.code());
                    } catch (IOException e) {
                        Log.e("MainActivity", "Error al leer el cuerpo de error", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                Log.e("MainActivity", "Error en la llamada a la API", t);
                textViewResponse.setText("Error en la llamada a la API: " + t.getMessage());
            }
        });
    }
}