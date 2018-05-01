package deka.com.unipclass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Spinner spinnerCampus;
    Spinner spinnerCurso;
    Spinner spinnerPeriodo;
    Spinner spinnerSemestre;
    String periodoSelecionado;
    String CursoSelecionado;
    int SemestreSelecionado;
    Button btnBuscar;
    TextView txtConsutalSala;
    Context mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerCampus = findViewById(R.id.spnCampusID);
        spinnerCurso = findViewById(R.id.spnCurso);
        spinnerPeriodo = findViewById(R.id.spnPeriodo);
        spinnerSemestre = findViewById(R.id.spnSemestre);
        btnBuscar = findViewById(R.id.btnBuscarId);
        txtConsutalSala = findViewById(R.id.txtConsultaSalaId);
        mainContext = this;
        txtConsutalSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConsultaActivity.class);
                startActivity(intent);
            }
        });


        //region CRIAR SPINNERS
        ArrayList<String> arrayCampus  = new ArrayList<String>();
        arrayCampus.add("Sorocaba");
        ArrayAdapter<String> adapterCampus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayCampus);
        adapterCampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampus.setAdapter(adapterCampus);

        ArrayList<String> arrayPeriodo = new ArrayList<String>();
        arrayPeriodo.add("matutino");
        arrayPeriodo.add("noturno");
        ArrayAdapter<String> adapterPeriodo = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arrayPeriodo);
        adapterPeriodo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(adapterPeriodo);

        spinnerPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                periodoSelecionado = adapterView.getItemAtPosition(i).toString();
                try
                {
                    SQLiteDatabase database = openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE, null);
                    ArrayList<String> arrayCursos = new ArrayList<String>();
                    ArrayAdapter<String> adapterCursos = new ArrayAdapter<String>(mainContext,android.R.layout.simple_spinner_dropdown_item, arrayCursos);
                    String queryCurso = "SELECT DISTINCT Nome FROM Cursos INNER JOIN Turmas ON Cursos.idCursos = Turmas.Cursos_idCursos  ";
                    Cursor cursorCurso = database.rawQuery(queryCurso, null);
                    int colunaCursoNome = cursorCurso.getColumnIndex("Nome");

                    cursorCurso.moveToFirst();
                    while (!cursorCurso.isAfterLast())
                    {
                        adapterCursos.add(cursorCurso.getString(colunaCursoNome));
                        // /Toast.makeText(getApplicationContext(), "curso: " + String.valueOf(valor), Toast.LENGTH_SHORT).show();
                        cursorCurso.moveToNext();
                    }
                    adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCurso.setAdapter(adapterCursos);
                    cursorCurso.close();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CursoSelecionado = adapterView.getItemAtPosition(i).toString();
                try
                {
                    SQLiteDatabase database = openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE, null);
                    String query = "SELECT * FROM Cursos WHERE Nome = '"+CursoSelecionado+"' ";
                    Cursor cursor = database.rawQuery(query, null);
                    int colunaSemestre = cursor.getColumnIndex("Semestres");
                    cursor.moveToFirst();
                    int numeroSemestre = cursor.getInt(colunaSemestre);
                    cursor.moveToNext();
                    cursor.close();
                    ArrayList<Integer> arraySemestes = new ArrayList<Integer>();
                    ArrayAdapter<Integer> adapterSemestres = new ArrayAdapter<Integer>(mainContext,android.R.layout.simple_spinner_dropdown_item, arraySemestes);
                    for (int j = 1; j <= numeroSemestre; j++)
                    {
                        adapterSemestres.add(j);
                    }
                    adapterSemestres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSemestre.setAdapter(adapterSemestres);
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //endregion

        //region  OPTER RESPOSTA E ENVIAR
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String resposta;
                try
                {
                    CursoSelecionado = spinnerCurso.getSelectedItem().toString();
                    periodoSelecionado = spinnerPeriodo.getSelectedItem().toString();
                    SemestreSelecionado = Integer.valueOf(spinnerSemestre.getSelectedItem().toString());
                    SQLiteDatabase database = openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE, null);
                    String query = "SELECT * FROM Cursos WHERE Nome = '"+CursoSelecionado+"' ";
                    Cursor cursor = database.rawQuery(query, null);
                    int colunCursoId = cursor.getColumnIndex("idCursos");
                    cursor.moveToFirst();
                    int cursoId = cursor.getInt(colunCursoId);
                    cursor.close();
                    query = "SELECT * FROM Turmas WHERE Cursos_idCursos = "+cursoId+" and Periodo = '"+periodoSelecionado+"' and Semestre = "+SemestreSelecionado +" ";
                    Cursor cursorTurma = database.rawQuery(query, null);
                    int colunmSalaId = cursorTurma.getColumnIndex("Salas_idSalas");
                    cursorTurma.moveToFirst();
                    int salaId = cursorTurma.getInt(colunmSalaId);
                    cursorTurma.moveToNext();
                    cursorTurma.close();
                    query = "SELECT * FROM Salas WHERE idSalas = "+salaId+"";
                    Cursor cursorSala = database.rawQuery(query, null);
                    int colunmSala = cursorSala.getColumnIndex("Nome");
                    cursorSala.moveToFirst();
                    String salaEscolhida = cursorSala.getString(colunmSala);
                    cursorSala.moveToNext();
                    cursorSala.close();
                    String bloco = salaEscolhida.split("")[1];
                    String andar = salaEscolhida.split("")[2];
                    resposta = "Sua sala é " + salaEscolhida + ", está no bloco " + bloco + " e no " + andar + "º andar";
                 }catch (Exception e)
                {
                    resposta ="Ainda não foi cadastrada a sala dessa turma";
                    //Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                prefs.edit().putString("resposta", resposta).commit();
                Intent intent = new Intent(getApplicationContext(), RespostaSalaActivity.class);
                startActivity(intent);
            }
        });
        //endregion
    }
}
