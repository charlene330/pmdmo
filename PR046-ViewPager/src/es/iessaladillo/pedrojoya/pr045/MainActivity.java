package es.iessaladillo.pedrojoya.pr045;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Constantes.
    private static int NUM_PAGINAS = 5;

    // Variables miembro.
    private Context contexto;
    private ViewPager vpPaginas;
    private PaginasAdapter adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contexto = this;
        // Establece el adaptador para el paginador.
        adaptador = new PaginasAdapter();
        vpPaginas = (ViewPager) findViewById(R.id.vpPaginas);
        vpPaginas.setAdapter(adaptador);
        // Establece la p�gina inicial a mostrar.
        vpPaginas.setCurrentItem(2);
    }

    // Adaptador de p�ginas para el ViewPager.
    private class PaginasAdapter extends PagerAdapter {

        // Retorna el n�mero de p�ginas.
        @Override
        public int getCount() {
            return NUM_PAGINAS;
        }

        // Retorna la vista correspondiente a la p�gina que se debe mostar.
        // Recibe la ViewPager y la posici�n de la p�gina a mostrar.
        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            // Obtiene la p�gina.
            View vistaPagina = LayoutInflater.from(contexto).inflate(
                    R.layout.pagina, null);
            // Obtiene el TextView y escribe el n�mero de p�gina.
            TextView lblPagina = (TextView) vistaPagina
                    .findViewById(R.id.lblPagina);
            lblPagina.setText(position + "");
            // Agrega la p�gina al ViewPager.
            collection.addView(vistaPagina, 0);
            // Retorna la vista correspondiente a la p�gina.
            return vistaPagina;
        }

        // Quita del ViewPager la vista correspondiente a la p�gina.
        // Recibe el ViewPager, la posici�n de la p�gina y la vista-p�gina.
        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        // Retorna si una vista-p�gina corresponde al objeto retornado por
        // instantiateItem().
        // Recibe la vista-p�gina y el objeto.
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        // Se llama cuando se inicia un cambio de p�gina.
        @Override
        public void startUpdate(ViewGroup arg0) {
        }

        // Se llama cuando el cambio de p�gina se ha completado.
        @Override
        public void finishUpdate(ViewGroup arg0) {
        }

        // Salva el estado.
        @Override
        public Parcelable saveState() {
            return null;
        }

        // Restaura el estado.
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

    }

}