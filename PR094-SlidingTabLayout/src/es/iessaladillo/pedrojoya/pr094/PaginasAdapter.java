package es.iessaladillo.pedrojoya.pr094;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

// Adaptador para el paginador de fragmentos.
public class PaginasAdapter extends FragmentPagerAdapter {

    // Constantes.
    private int NUM_PAGINAS = 5;

    // Variables.
    Context context;
    private int[] colores;

    // Constructor. Recibe el gestor de fragmentos.
    public PaginasAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        colores = new int[] { context.getResources().getColor(R.color.color1),
                context.getResources().getColor(R.color.color2),
                context.getResources().getColor(R.color.color3),
                context.getResources().getColor(R.color.color4),
                context.getResources().getColor(R.color.color5) };
    }

    // Retorna el fragmento correspondiente a una página. Recibe el número
    // de página.
    @Override
    public Fragment getItem(int position) {
        // Se crea el fragmento y se le pasa como argumento el n�mero de
        // pesta�a para que lo escriba en su TextView.
        Fragment frgPagina = PaginaFragment.newInstance(position + 1,
                colores[position]);
        return frgPagina;
    }

    // Retorna el número de páginas.
    @Override
    public int getCount() {
        return NUM_PAGINAS;
    }

    // Retorna el título de una página. Recibe el número de página.
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(R.string.tab) + " "
                + (position + 1);
    }
}
