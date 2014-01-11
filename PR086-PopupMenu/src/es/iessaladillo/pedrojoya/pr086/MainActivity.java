package es.iessaladillo.pedrojoya.pr086;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarLista();
    }

    private void cargarLista() {
        ArrayList<Alumno> alumnos = new ArrayList<Alumno>();
        alumnos.add(new Alumno("Baldomero", "La casa de Baldomero",
                "956956956", "2� CFGS DAM"));
        alumnos.add(new Alumno("Germ�n Gin�s", "La casa de Germ�n",
                "678678678", "1� CFGS DAM"));
        AlumnosAdapter adaptador = new AlumnosAdapter(this, alumnos);
        ListView lstAlumnos = (ListView) findViewById(R.id.lstAlumnos);
        lstAlumnos.setAdapter(adaptador);
    }

}
