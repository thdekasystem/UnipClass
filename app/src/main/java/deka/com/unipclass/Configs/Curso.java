package deka.com.unipclass.Configs;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import static android.database.sqlite.SQLiteDatabase.*;
import deka.com.unipclass.*;
import static android.content.Context.*;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.Toast;

public class Curso {
    private int idCursos;
    private String nome;
    private int semestes;
    public Curso(int idCursos, String nome, int semestes)
    {
        this.idCursos = idCursos;
        this.nome = nome;
        this.semestes = semestes;
    }

    public void atualizarCurso(Context context)
    {
        try
        {
            SQLiteDatabase database = context.openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE,null);
            String query = "INSERT INTO Cursos(idCursos, Nome, Semestres) VALUES("+this.idCursos+" , '"+ this.nome+"', "+this.semestes+" ) ";
            database.execSQL(query);
            //Toast.makeText(context, "Curso adcionado", Toast.LENGTH_SHORT).show();
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Erro: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
