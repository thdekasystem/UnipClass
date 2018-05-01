package deka.com.unipclass.Configs;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 4/15/18.
 */

public class Sala {
    private int idSalas;
    private String nome;
    public  Sala(int idSalas, String nome)
    {
        this.idSalas = idSalas;
        this.nome = nome;
    }
    public void atualizarSala(Context context)
    {
        try
        {
            SQLiteDatabase database = context.openOrCreateDatabase("dekasyst_Unip", MODE_PRIVATE,null);

            String query = "INSERT INTO Salas(idSalas, Nome) VALUES("+ this.idSalas+", '"+this.nome+"')";
            database.execSQL(query);
           // Toast.makeText(context, "Sala adcionada", Toast.LENGTH_SHORT).show();
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Erro: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
