package deka.com.unipclass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import  deka.com.unipclass.Configs.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class CarregamentoActivity extends AppCompatActivity {
    RequestQueue requestQueueCursos;
    RequestQueue requestQueueSalas;
    RequestQueue requestQueueTurmas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregamento);

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestQueueCursos = Volley.newRequestQueue(this);
        requestQueueSalas = Volley.newRequestQueue(this);
        requestQueueTurmas = Volley.newRequestQueue(this);

        //region CRIAR DB

        // criar db se não existir
        SQLiteDatabase database = openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE, null);
        try
        {
            database.execSQL("CREATE TABLE IF NOT EXISTS Cursos ( idCursos INTEGER  NOT NULL PRIMARY KEY,Nome VARCHAR(45) NOT NULL UNIQUE, Semestres INTEGER DEFAULT NULL)");
            database.execSQL("CREATE TABLE IF NOT EXISTS Salas ( idSalas INTEGER NOT NULL PRIMARY KEY, Nome VARCHAR(5) NOT NULL UNIQUE)");
            database.execSQL("CREATE TABLE IF NOT EXISTS Turmas ( idTurmas INTEGER NOT NULL PRIMARY KEY, Cursos_idCursos INTEGER NOT NULL  , Salas_idSalas INTEGER NOT NULL, Periodo VARCHAR(50) DEFAULT NULL, Semestre INTEGER DEFAULT NULL, FOREIGN KEY(Cursos_idCursos) REFERENCES Cursos(idCursos), FOREIGN KEY(Salas_idSalas) REFERENCES Salas(Salas_idSalas) )");
            //Toast.makeText(getApplicationContext(), "Salas foram atualizadas", Toast.LENGTH_SHORT).show();
        }
        catch(android.database.sqlite.SQLiteException e)
        {
            //Toast.makeText(getApplicationContext(), "erro: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        //endregion


        //region ATUALIZAR DB
        if(isOnline(getApplicationContext()))
        {
            //deletar tudo antes de atualizar
            database.execSQL("DELETE FROM Cursos");
            database.execSQL("DELETE FROM Salas");
            database.execSQL("DELETE FROM Turmas");

            //atualizar
            recuperarCursos();
            recuperarSalas();
            recuperarTurmas();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Sem net", Toast.LENGTH_SHORT).show();
        }

        //endregion

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (CarregamentoActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }
        }, 1100);


    }

    //region VERIFICAR SE TEM ACESSO A INTERNET
    public boolean isOnline(Context context)
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        for (NetworkInfo ni : ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo()) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI") && ni.isConnected()) {
                haveConnectedWifi = true;
            }

            if (ni.getTypeName().equalsIgnoreCase("MOBILE") && ni.isConnected()) {
                haveConnectedMobile = true;
            }
        }
        if (haveConnectedWifi || haveConnectedMobile) {

            return true;
        }
        return false;
    }
    //endregion


    //region METODOS PARA  CHAMAR JSON E ATUALIZAR AS TABELAS
    public void recuperarCursos()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://unipclass.dekasystem.com.br/webservice.php?tabela=cursos",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Cursos");
                            for (int i = 0; i < jsonArray.length(); i ++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String nomeCurso = jsonObject.getString("Nome");
                                int semestresCurso = jsonObject.getInt("Semestres");
                                int idCursos       = jsonObject.getInt("idCursos");
                                Curso curso = new Curso(idCursos, nomeCurso,semestresCurso);
                                curso.atualizarCurso(getApplicationContext());
                            }
                           // Toast.makeText(getApplicationContext(), "Tabela Cursos recuperada",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueueCursos.add(jsonObjectRequest);
    }
    public void recuperarSalas()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://unipclass.dekasystem.com.br/webservice.php?tabela=salas",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Salas");
                            for (int i = 0; i < jsonArray.length(); i ++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String nomeSala = jsonObject.getString("Nome");
                                int    idSalas  = jsonObject.getInt("idSalas");
                                Sala sala = new Sala(idSalas, nomeSala);
                                sala.atualizarSala(getApplicationContext());
                            }
                            //Toast.makeText(getApplicationContext(), "Tabela Salas recuperada",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueueSalas.add(jsonObjectRequest);
    }
    public void recuperarTurmas()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://unipclass.dekasystem.com.br/webservice.php?tabela=turmas",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Turmas");
                            for (int i = 0; i < jsonArray.length(); i ++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int  idTurmas = jsonObject.getInt("idTurmas");
                                int Cursos_idCursos = jsonObject.getInt("Cursos_idCursos");
                                int    Salas_idSalas  = jsonObject.getInt("Salas_idSalas");
                                String Periodo  = jsonObject.getString("Periodo");
                                int Semestre = jsonObject.getInt("Semestre");
                                Turma turma = new Turma(idTurmas, Salas_idSalas, Cursos_idCursos,  Semestre, Periodo);
                                turma.atualizarTurma(getApplicationContext());
                            }
                           // Toast.makeText(getApplicationContext(), "Tabela Turmas recuperada",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueueTurmas.add(jsonObjectRequest);
    }
    //endregion
}
