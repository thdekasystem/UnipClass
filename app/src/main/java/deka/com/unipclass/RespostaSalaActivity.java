package deka.com.unipclass;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RespostaSalaActivity extends AppCompatActivity {
    TextView txtResposta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta_sala);

        txtResposta = findViewById(R.id.txtResposstaId);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String value = prefs.getString("resposta", null);
        txtResposta.setText(value);

    }
}
