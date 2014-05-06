package es.iessaladillo.pedrojoya.PR004;

import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    // Al crear la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getVistas();
    }

    // Obtiene e inicializa las vistas.
    private void getVistas() {
        // La actividad responder� al pulsar sobre cualquier bot�n.
        ((Button) findViewById(R.id.btnNavegar)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnBuscar)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnLlamar)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnMarcar)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnMostrarMapa)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnBuscarMapa)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnMostrarContactos))
                .setOnClickListener(this);
    }

    // Al hacer click sobre alg�n bot�n.
    @Override
    public void onClick(View v) {
        Intent intent;
        // Dependiendo del bot�n pulsado.
        switch (v.getId()) {
        case R.id.btnNavegar:
            // Acci�n--> VER. Uri--> URL.
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.genbeta.com"));
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_hay_navegador,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.btnBuscar:
            // Acci�n--> BUSCAR EN INTERNET. Extra -> T�rmino de consulta.
            intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, "IES Saladillo");
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_hay_buscador,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.btnLlamar:
            // Acci�n--> LLAMAR. Uri--> tel:num.
            intent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:(+34)123456789"));
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_se_puede_llamar,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.btnMarcar:
            // Acci�n--> MARCAR. Uri--> tel:num.
            intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:(+34)12345789"));
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_hay_dial, Toast.LENGTH_SHORT)
                        .show();
            }
            break;
        case R.id.btnMostrarMapa:
            // Acci�n--> VER. Uri--> geo:latitud,longitud?z=zoom.
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:36.1121,-5.44347?z=19"));
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_hay_aplicaci_n_de_mapas,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.btnBuscarMapa:
            // Acci�n--> VER. Uri--> geo:latitud,longitud?q=consulta.
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=duque de rivas, Algeciras"));
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_hay_aplicaci_n_de_mapas,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.btnMostrarContactos:
            // Acci�n--> VER. Uri--> Acceder� al proveedor de contenidos de la
            // aplicaci�n de contactos.
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("content://contacts/people/"));
            if (estaDisponible(this, intent)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.no_hay_gestor_de_contactos,
                        Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    // Retorna si hay alguna actividad que pueda recibir el intent.
    public boolean estaDisponible(Context ctx, Intent intent) {
        final PackageManager gestorPaquetes = ctx.getPackageManager();
        List<ResolveInfo> listaApps = gestorPaquetes.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return listaApps.size() > 0;
    }
}