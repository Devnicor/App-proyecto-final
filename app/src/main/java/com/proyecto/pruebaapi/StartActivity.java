package com.proyecto.pruebaapi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import java.util.Timer;
import java.util.TimerTask;
public class StartActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // Iniciar el temporizador
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}
