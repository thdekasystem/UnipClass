package deka.com.unipclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalActivity extends AppCompatActivity {
    Button btnLabs;
    Button btnLocalizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnLabs = findViewById(R.id.btnLabsId);
        btnLocalizar = findViewById(R.id.btnLocalizarId);
        btnLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, LaboratoriosActivity.class);
                startActivity(intent);
            }
        });
        btnLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
