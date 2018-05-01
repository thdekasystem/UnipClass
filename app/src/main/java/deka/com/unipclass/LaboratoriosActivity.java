package deka.com.unipclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaboratoriosActivity extends AppCompatActivity {
    Button btnLabs;
    Button btnOdonto;
    Button btnFisio;
    Button btnBiblio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratorios);
        btnLabs = findViewById(R.id.btnLabs);
        btnOdonto = findViewById(R.id.btnOdonto);
        btnFisio = findViewById(R.id.btnFisio);
        btnBiblio = findViewById(R.id.btnBiblioteca);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String resposta;

        btnLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString("resposta", "Os laboratórios de Informática estão no 1º andar do Bloco E e F").commit();
                Intent intent = new Intent(getApplicationContext(), RespostaSalaActivity.class);
                startActivity(intent);
            }
        });
        btnOdonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefs.edit().putString("resposta", "Os laboratórios de Odoltologia estão no terreo do Bloco A e B").commit();
                Intent intent = new Intent(getApplicationContext(), RespostaSalaActivity.class);
                startActivity(intent);
            }
        });
        btnFisio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString("resposta", "Os laboratórios de Fisioterapia  estão no 1º Subsolo do Bloco A e c").commit();
                Intent intent = new Intent(getApplicationContext(), RespostaSalaActivity.class);
                startActivity(intent);
            }
        });
        btnBiblio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString("resposta", "A biblioteca esta  no 2º Subsolo do Bloco H").commit();
                Intent intent = new Intent(getApplicationContext(), RespostaSalaActivity.class);
                startActivity(intent);
            }
        });
    }
}
