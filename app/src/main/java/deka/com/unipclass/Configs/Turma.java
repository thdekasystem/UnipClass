package deka.com.unipclass.Configs;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 4/15/18.
 */

public class Turma {
    private int idTurmas;
    private int Salas_idSalas;
    private int Cursos_idCursos;
    private int Semestre;
    private String Periodo;
    public Turma(int idTurmas, int Salas_idSalas, int Cursos_idCursos, int Semestre, String Periodo)
    {
        this.idTurmas = idTurmas;
        this.Salas_idSalas   = Salas_idSalas;
        this.Cursos_idCursos = Cursos_idCursos;
        this.Semestre        = Semestre;
        this.Periodo         = Periodo;
    }

    public void atualizarTurma(Context context)
    {
        try
        {
            SQLiteDatabase database = context.openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE,null);

            String query = "INSERT INTO Turmas (idTurmas, Cursos_idCursos, Salas_idSalas, Periodo, Semestre) ";
            query  += "VALUES("+this.idTurmas+", "+this.Cursos_idCursos+", "+this.Salas_idSalas+", '"+this.Periodo+"', "+this.Semestre+" )";
            database.execSQL(query);
           // Toast.makeText(context, "Turma adcionada", Toast.LENGTH_SHORT).show();
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
