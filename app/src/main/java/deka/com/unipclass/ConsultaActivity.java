package deka.com.unipclass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import deka.com.unipclass.Configs.Curso;

public class ConsultaActivity extends AppCompatActivity {
    Spinner spnSala;
    Spinner spnPeriodo;
    String resposta;
    Button btnBuscarCurso;
    TextView consultaTurma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        btnBuscarCurso = findViewById(R.id.btnBuscarCursoId);
        consultaTurma  = findViewById(R.id.txtConsultaTurmaId);

        //region CRIAR SPINNER SALAS e PERIODOS
        spnSala = findViewById(R.id.spnSalaId2);
        spnPeriodo = findViewById(R.id.spnPeriodoId);
        try
        {
            ArrayList<String> arraySalas = new ArrayList<String>();
            ArrayAdapter<String> adapterSalas = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arraySalas);
            SQLiteDatabase database = openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE, null);
            String query = "SELECT DISTINCT Nome FROM Salas INNER JOIN Turmas ON Salas.idSalas = Turmas.Salas_idSalas";
            Cursor cursor = database.rawQuery(query, null);
            int colunaSala = cursor.getColumnIndex("Nome");
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                adapterSalas.add(cursor.getString(colunaSala));
                cursor.moveToNext();
            }
            adapterSalas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnSala.setAdapter(adapterSalas);
            cursor.close();
        }
        catch (Exception e)
        {

        }
        ArrayList<String> arrayPeriodos = new ArrayList<String>();
        ArrayAdapter<String> adapterPeriodos = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arrayPeriodos);
        adapterPeriodos.add("matutino");
        adapterPeriodos.add("noturno");
        spnPeriodo.setAdapter(adapterPeriodos);
        //endregion

        //region CONSULTA TURMA
            consultaTurma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        //endregion

        //region REALIZAR BUSCA E ENVIAR
            btnBuscarCurso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    try
                    {

                        SQLiteDatabase database = openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE, null);
                        String query = "SELECT * FROM Salas WHERE Nome = '"+ spnSala.getSelectedItem().toString()+"'";
                        Cursor cursorSala = database.rawQuery(query, null);
                        cursorSala.moveToFirst();
                        int columnSala = cursorSala.getColumnIndex("idSalas");
                        int idSala = cursorSala.getInt(columnSala);
                        query = "SELECT * FROM Turmas WHERE Salas_idSalas = "+idSala + " and Periodo = '"+spnPeriodo.getSelectedItem().toString()+"'";
                        Cursor cursorIdCursos = database.rawQuery(query, null);
                        cursorIdCursos.moveToFirst();
                        int columnCurso = cursorIdCursos.getColumnIndex("Cursos_idCursos");
                        int idCursos  = cursorIdCursos.getInt(columnCurso);
                        cursorIdCursos.close();
                        query = "SELECT * FROM Cursos WHERE idCursos = " + idCursos;
                        Cursor cursorCurso = database.rawQuery(query, null);
                        cursorCurso.moveToFirst();
                        int columnNome = cursorCurso.getColumnIndex("Nome");
                        resposta = "Na Sala "+ spnSala.getSelectedItem().toString() +" no periodo "+spnPeriodo.getSelectedItem().toString()+" tem a turma de " + cursorCurso.getString(columnNome);

                        prefs.edit().putString("resposta", resposta).commit();

                    }
                    catch (Exception e)
                    {
                        resposta = "Não há turma nessa Sala durante o periodo " + spnPeriodo.getSelectedItem().toString();
                        prefs.edit().putString("resposta", resposta).commit();

                    }

                    Intent intent = new Intent(getApplicationContext(), RespostaSalaActivity.class);
                    startActivity(intent);

                }
            });
        //endregion
    }
}
