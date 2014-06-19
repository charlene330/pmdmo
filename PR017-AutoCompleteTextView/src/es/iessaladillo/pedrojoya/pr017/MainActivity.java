package es.iessaladillo.pedrojoya.pr017;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    // Vistas.
    private AutoCompleteTextView txtConcepto;

    // Al crear la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se obtienen e inicializan las vistas.
        getVistas();
    }

    // Obtiene e inicializa las vistas de la actividad.
    private void getVistas() {
        txtConcepto = (AutoCompleteTextView) findViewById(R.id.txtConcepto);
        txtConcepto.setAdapter(new ConceptosAdapter(this, getDatos()));
        ((Button) findViewById(R.id.btnMostrar)).setOnClickListener(this);
    }

    // Construye y retorna el ArrayList de conceptos.
    private ArrayList<Concepto> getDatos() {
        ArrayList<Concepto> conceptos = new ArrayList<Concepto>();
        conceptos.add(new Concepto(R.drawable.animal, "Animal", "Animal"));
        conceptos.add(new Concepto(R.drawable.bridge, "Bridge", "Puente"));
        conceptos.add(new Concepto(R.drawable.flag, "Flag", "Bandera"));
        conceptos.add(new Concepto(R.drawable.food, "Food", "Comida"));
        conceptos.add(new Concepto(R.drawable.fruit, "Fruit", "Fruta"));
        conceptos.add(new Concepto(R.drawable.glass, "Glass", "Vaso"));
        conceptos.add(new Concepto(R.drawable.plant, "Plant", "Planta"));
        conceptos.add(new Concepto(R.drawable.science, "Science", "Ciencia"));
        conceptos.add(new Concepto(R.drawable.sea, "Sea", "Mar"));
        return conceptos;
    }

    // Al hacer click sobre btnTraducir.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnMostrar:
            Toast.makeText(this, txtConcepto.getText().toString(),
                    Toast.LENGTH_SHORT).show();
            break;
        }
    }

    // Clase privada para adaptador de conceptos
    // Extendemos de ArrayAdapter porque �ste hereda de ListAdapter
    // y adem�s implementa la intefaz Filterable,
    // como requiere el AutoCompleteTextView.
    private class ConceptosAdapter extends ArrayAdapter<Concepto> {

        // Variables miembro.
        private ArrayList<Concepto> mConceptos;
        private ArrayList<Concepto> mSugerencias;
        private LayoutInflater mInflador;

        // Clase privada para contenedor de vistas.
        private class ContenedorVistas {
            // Variables miembro.
            ImageView imgFoto;
            TextView lblEnglish;
        }

        @SuppressWarnings("unchecked")
        public ConceptosAdapter(Context contexto, ArrayList<Concepto> conceptos) {
            // Se llama al constructor del padre. El segundo par�metro no es
            // usado. El tercer par�metro corresponde al resId del TextView que
            // contendr� el texto.
            super(contexto, R.layout.activity_main_item, R.id.lblEnglish,
                    conceptos);
            // Se obtiene el inflador de layouts.
            mInflador = LayoutInflater.from(contexto);
            // Se crea una copia del ArrayList de datos, ya que se ver� afectado
            // por el filtro.
            mConceptos = (ArrayList<Concepto>) conceptos.clone();
            // El ArrayList de sugerencias est� inicialmente vac�o.
            mSugerencias = new ArrayList<Concepto>();
        }

        // Retorna el objeto Filter que va a filtrar el adaptador.
        @Override
        public Filter getFilter() {
            // Se retorna un nuevo filtro subclase de Filter (inline).
            return new Filter() {
                // Se ejecuta cuando se debe filtrar. Recibe la cadena ya
                // introducida.
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // Se crea el objeto resultado del filtro, inicialmente
                    // vac�o.
                    FilterResults filterResults = new FilterResults();
                    // Si ya se ha introducido texto.
                    if (constraint != null) {
                        // Se vac�a el ArrayList de sugerencias.
                        mSugerencias.clear();
                        // Se comprueba concepto a concepto para incluirlo o no
                        // en las sugerencias.
                        // OJO se comprueba en el ArrayList con TODOS los
                        // conceptos.
                        for (Concepto concepto : mConceptos) {
                            // Si comienza igual se a�ade a las sugerencias.
                            if (concepto
                                    .getEnglish()
                                    .toLowerCase(Locale.getDefault())
                                    .contains(
                                            constraint.toString().toLowerCase(
                                                    Locale.getDefault()))) {
                                mSugerencias.add(concepto);
                            }
                        }
                        // Se establecen las sugerencias como resultado del
                        // filtro.
                        filterResults.values = mSugerencias;
                        filterResults.count = mSugerencias.size();
                    }
                    // Se retorna el resultado del filtrado.
                    return filterResults;
                }

                // Se encarga de publicar el resultado seleccionado en el
                // widget.
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                        FilterResults results) {
                    // Si hay sugerencias se a�aden los resultados filtrados
                    // como �nicos datos del adaptador.
                    if (results != null && results.count > 0) {
                        clear();
                        for (Concepto concepto : (ArrayList<Concepto>) results.values) {
                            add(concepto);
                        }
                        // Se fuerza el repintado.
                        notifyDataSetChanged();
                    }
                }

                // Retorna la cadena que debe escribirse en el widget. Recibe el
                // objeto seleccionado
                @Override
                public CharSequence convertResultToString(Object resultValue) {
                    // Se obtiene el concepto y se retorna la cadena.
                    Concepto concepto = (Concepto) resultValue;
                    return concepto.getEnglish();
                }

            };
        }

        // Retorna la vista a mostrar para un elemento dado.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContenedorVistas contenedor;
            // Si no se puede reciclar.
            if (convertView == null) {
                // Inflo el layout.
                convertView = mInflador.inflate(R.layout.activity_main_item,
                        parent, false);
                // Se obtienen las vistas y se almacenan en el contenedor.
                contenedor = new ContenedorVistas();
                contenedor.imgFoto = (ImageView) convertView
                        .findViewById(R.id.imgFoto);
                contenedor.lblEnglish = (TextView) convertView
                        .findViewById(R.id.lblEnglish);
                // Se almacena el contenedor en el tag de la vista.
                convertView.setTag(contenedor);
            } else {
                // Si se puede reciclar, se obtiene el contenedor desde el tag.
                contenedor = (ContenedorVistas) convertView.getTag();
            }
            // Se escriben los datos en las vistas.
            // OJO! usar getItem del Adaptador para obtener el album
            // correspondiente ya que puede que del array de datos original
            // algunos hayan sido filtrados, por lo que s�lo debemos tener en
            // cuenta los incluidos en el adaptador.
            Concepto concepto = getItem(position);
            contenedor.imgFoto.setImageResource(concepto.getFotoResId());
            contenedor.lblEnglish.setText(concepto.getEnglish());
            // Se retorna la vista.
            return convertView;
        }
    }

}
