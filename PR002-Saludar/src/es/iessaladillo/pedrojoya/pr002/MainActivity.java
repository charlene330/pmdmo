package es.iessaladillo.pedrojoya.pr002;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
        OnCheckedChangeListener {

    // Vistas.
    private CheckBox chkEducado;
    private Button btnSaludar;
    private EditText txtNombre;

    // Cuando se crea la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Se llama al m�todo onCreate de la actividad padre.
        super.onCreate(savedInstanceState);
        // Se establece el layout que se usar� para la actividad.
        this.setContentView(R.layout.activity_main);
        // Se obtiene la referencia y se inicializan las vistas.
        getVistas();
    }

    // Obtiene la referencia e inicializa las vistas.
    private void getVistas() {
        // Se obtiene la referencia a las vistas.
        chkEducado = (CheckBox) this.findViewById(R.id.chkEducado);
        btnSaludar = (Button) this.findViewById(R.id.btnSaludar);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        // El checkbox apracer� inicialmente chequeado.
        chkEducado.setChecked(true);
        // La actividad actuar� con listener cuando se haga click sobre el
        // bot�n.
        btnSaludar.setOnClickListener(this);
        // La actividad actuar� como listener cuando cambie el estado del
        // checkbox.
        chkEducado.setOnCheckedChangeListener(this);
    }

    // Cuando se hace click sobre alg�n bot�n.
    @Override
    public void onClick(View v) {
        // Dependiendo del bot�n pulsado.
        switch (v.getId()) {
        case R.id.btnSaludar:
            btnSaludarOnClick();
            break;
        }
    }

    // Cuando se hace click sobre btnSaludar.
    private void btnSaludarOnClick() {
        // Se crea el mensaje a mostrar.
        String mensaje = getString(R.string.buenos_dias);
        if (chkEducado.isChecked()) {
            mensaje = mensaje + " " + getString(R.string.tenga_usted);
        }
        mensaje += " " + txtNombre.getText();
        // Se muestra el mensaje en un Toast.
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    // Cuando se cambia de estado del checkbox.
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Se crea el mensaje.
        String mensaje = getString(R.string.modo_educado) + " ";
        if (isChecked) {
            mensaje += getString(R.string.on);
        } else {
            mensaje += getString(R.string.off);
        }
        // Se muestra en un Toast.
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}