package com.example.androidmapsv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements
		OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener,
		OnMarkerDragListener, OnInfoWindowClickListener, LocationListener,
		LocationSource {

	// Contantes.
	private static final int RQS_GooglePlayServices = 1;

	// Variables miembro.
	private TextView lblPosicion;
	private GoogleMap mapa;
	private Polyline linea;
	private PolylineOptions configLinea;
	private boolean desdeMarcador = false;
	private Criteria criteriosLocalizacion;
	private LocationManager localizador;

	private OnLocationChangedListener listenerLocalizador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getVistas();
		// Establezco los criterios para la localizaci�n.
		criteriosLocalizacion = new Criteria();
		criteriosLocalizacion.setAccuracy(Criteria.ACCURACY_FINE);
		// Creo un localizador para que me informe del cambio de posici�n.
		localizador = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	private void getVistas() {
		lblPosicion = (TextView) this.findViewById(R.id.lblPosicion);
		// Obtengo la referencia al mapa.
		FragmentManager gestorFragmentos = getSupportFragmentManager();
		SupportMapFragment fragmentoMapa = (SupportMapFragment) gestorFragmentos
				.findFragmentById(R.id.map);
		mapa = fragmentoMapa.getMap();
		// Coloco el mapa en mi posici�n actual.
		mapa.setMyLocationEnabled(true);
		// Establezco el tipo de mapa que se mostrar�.
		mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// Establezco la configuraci�n de la interfaz del mapa.
		mapa.getUiSettings().setZoomControlsEnabled(true);
		mapa.getUiSettings().setCompassEnabled(true);
		mapa.getUiSettings().setMyLocationButtonEnabled(true);
		mapa.getUiSettings().setRotateGesturesEnabled(true);
		mapa.getUiSettings().setScrollGesturesEnabled(true);
		mapa.getUiSettings().setTiltGesturesEnabled(true);
		mapa.getUiSettings().setZoomGesturesEnabled(true);
		// A�ado la capa de tr�fico.
		mapa.setTrafficEnabled(true);
		// Hago que sea la propia actividad la que responda cuando se pulse
		// sobre el mapa, cuando se haga click largo, cuando se pulse sobre un
		// marcador, cuando se desplace un marcador y cuando se pulse sobre la
		// ventana de informaci�n de un marcador.
		mapa.setOnMapClickListener(this);
		mapa.setOnMapLongClickListener(this);
		mapa.setOnMarkerClickListener(this);
		mapa.setOnMarkerDragListener(this);
		mapa.setOnInfoWindowClickListener(this);
		// Creo y establezco el adaptador que debe utilizarse para la info de
		// los marcadores.
		mapa.setInfoWindowAdapter(new AdaptadorInfo());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.mnuLegal:
				// Obtengo el texto de la licencia.
				String licencia = GooglePlayServicesUtil
						.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
				// Muestro el di�logo.
				AlertDialog.Builder dlgLicencia = new AlertDialog.Builder(
						MainActivity.this);
				dlgLicencia.setTitle(R.string.nota_legal);
				dlgLicencia.setMessage(licencia);
				dlgLicencia.setNeutralButton(R.string.aceptar, null);
				dlgLicencia.show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Compruebo que GooglePlayServices est� instalado en el dispositivo y
		// si no lo est� muestro un di�logo est�ndar recomendado por Google.
		int resultado = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (resultado == ConnectionResult.SERVICE_MISSING
				|| resultado == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
				|| resultado == ConnectionResult.SERVICE_DISABLED) {
			Dialog dlgSinGooglePlayServices = GooglePlayServicesUtil
					.getErrorDialog(resultado, this, RQS_GooglePlayServices);
			dlgSinGooglePlayServices.show();
		}
		else if (resultado == ConnectionResult.SUCCESS) {
			// Me registro para que me informen de cambios en la localizaci�n,
			// indicando los criterio y el procedimiento que debe ejecutarse.
			localizador.requestLocationUpdates(0L, // tiempo m�nimo
					0.0f, // distancia m�nima
					criteriosLocalizacion, // criterios
					this, // Qui�n recibir� el callback de cambio de posici�n.
					null); // bucle
			// Indico que la propia actividad ser� el origen de localizaci�n
			// para mapa, ya que es la propia actividad la que implementa el
			// m�todo callback que ser� llamado cuando cambie la posici�n.
			mapa.setLocationSource(this);
		}
	}

	@Override
	protected void onPause() {
		// Desvinculo el origen de localizaci�n del mapa.
		mapa.setLocationSource(null);
		// Indico que no se env�en m�s cambios de posici�n al localizador.
		localizador.removeUpdates(this);
		super.onPause();
	}

	public void onMapClick(LatLng punto) {
		// Muestro d�nde se ha pulsado
		lblPosicion.setText(punto.toString());
		// Muevo la c�mara a una nueva actualizaci�n situada en el punto
		// pulsado.
		mapa.animateCamera(CameraUpdateFactory.newLatLng(punto));
		// Indico que no se ha pulsado sobre un marcador.
		desdeMarcador = false;
	}

	public void onMapLongClick(LatLng punto) {
		// A�ado donde se ha pulsado un nuevo marcador azul desplazable.
		lblPosicion.setText(this.getString(R.string.nuevo_marcador)
				+ punto.toString());
		MarkerOptions marcador = new MarkerOptions();
		marcador.position(punto);
		marcador.title(punto.toString());
		marcador.snippet(getString(R.string.snippet));
		marcador.draggable(true);
		// Para el color del marcador.
		BitmapDescriptor descriptor = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
		marcador.icon(descriptor);
		mapa.addMarker(marcador);
		// Indico que no se ha pulsado sobre un marcador.
		desdeMarcador = false;
	}

	public boolean onMarkerClick(Marker marcador) {
		// Si anteriormente se ha pulsado en otro marcador dibujo una l�nea
		// entre ambos marcadores.
		if (desdeMarcador) {
			// Libero los recursos del objeto l�nea.
			if (linea != null) {
				linea.remove();
				linea = null;
			}
			// A�ado a la configuraci�n de la linea la posici�n del marcador
			// sobre el que se ha pulsado.
			configLinea.add(marcador.getPosition());
			// Hago que la l�nea sea azul
			configLinea.color(Color.BLUE);
			// A�ado al mapa la l�nea
			linea = mapa.addPolyline(configLinea);
		}
		// Si no se ha pulsado antes en otro marcador.
		else {
			// Si ya hab�a un objeto l�nea, libero sus recursos.
			if (linea != null) {
				linea.remove();
				linea = null;
			}
			// Creo el objeto configuraci�n de l�nea y le a�ado la posici�n del
			// marcador donde se ha pulsado.
			configLinea = new PolylineOptions();
			configLinea.add(marcador.getPosition());
			// Indico que se ha pulsado sobre un marcador.
			desdeMarcador = true;
		}
		// Muestro la ventana de informaci�n del marcador.
		marcador.showInfoWindow();
		// Me muevo a esa posici�n con un nivel de zoom de 19, apuntado al este
		// (90�) y con un �ngulo de 50�.
		CameraPosition posicionCamara = new CameraPosition.Builder()
				.target(marcador.getPosition()).zoom(19).bearing(90).tilt(50)
				.build();
		mapa.animateCamera(CameraUpdateFactory
				.newCameraPosition(posicionCamara));
		return true;
	}

	public void onMarkerDragStart(Marker marcador) {
		// Muestro qu� marcador se est� desplazando.
		lblPosicion.setText(getString(R.string.desplazando_marcador)
				+ marcador.getId());
	}

	public void onMarkerDrag(Marker marcador) {
		// Muestro por d�nde va el marcador que estoy desplazando.
		lblPosicion.setText(getString(R.string.marcador) + marcador.getId()
				+ getString(R.string.en) + marcador.getPosition().toString());
	}

	public void onMarkerDragEnd(Marker marcador) {
		// Muestro qu� marcador ha finalizado su desplazamiento.
		lblPosicion.setText(getString(R.string.desplazamiento_de_marcador)
				+ marcador.getId() + getString(R.string.finalizada));
	}

	public void onInfoWindowClick(Marker marcador) {
		// Hago como que visito la web asociada al marcador.
		Toast.makeText(this, "Visitando la web asociada al marcador...",
				Toast.LENGTH_LONG).show();
	}

	// Cuando cambia la posici�n.
	public void onLocationChanged(Location posicion) {
		if (listenerLocalizador != null) {
			listenerLocalizador.onLocationChanged(posicion);
			// Obtengo la latitud y longitud de la posici�n.
			double lat = posicion.getLatitude();
			double lon = posicion.getLongitude();
			// Lo muestro en el TextView.
			lblPosicion.setText("lat: " + lat + "\n" + "lon: " + lon);
			// Muevo el mapa a mi posici�n actual cambiando el zoom a 14 (tama�o
			// Algeciras) y tardando 1000ms en el movimiento.
			LatLng coordenadas = new LatLng(lat, lon);
			mapa.animateCamera(
					CameraUpdateFactory.newLatLngZoom(coordenadas, 14.0f),
					1000, null);
		}
	}

	public void onProviderDisabled(String arg0) {
	}

	public void onProviderEnabled(String arg0) {
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	public void activate(OnLocationChangedListener listener) {
		listenerLocalizador = listener;
	}

	public void deactivate() {
		listenerLocalizador = null;
	}

	public void btnTodosOnClick(View v) {
		// Posiciones de las farmacias.
		final LatLng farmacia1 = new LatLng(36.1062496, -5.4427819);
		final LatLng farmacia2 = new LatLng(36.11781, -5.45694);
		final LatLng farmacia3 = new LatLng(36.09934, -5.45034);
		// A�ado los marcadores al mapa
		mapa.addMarker(new MarkerOptions().position(farmacia1).title(
				"Farmacia1"));
		mapa.addMarker(new MarkerOptions().position(farmacia2).title(
				"Farmacia2"));
		mapa.addMarker(new MarkerOptions().position(farmacia3).title(
				"Farmacia3"));
		// Creo los limites en los que debe posicionarse el mapa para que se
		// muestren todas las farmacias.
		LatLngBounds limites = new LatLngBounds.Builder().include(farmacia1)
				.include(farmacia2).include(farmacia3).build();
		// Muevo la c�mara para que se coloce en una posici�n que incluya todas
		// las farmacias (dejando un padding de 50px)
		mapa.moveCamera(CameraUpdateFactory.newLatLngBounds(limites, 50));
	}

	// Clase adaptadora para la info de los marcadores.
	private class AdaptadorInfo implements InfoWindowAdapter {

		// Variables miembro.
		private View vistaDatos;

		// Constructor
		AdaptadorInfo() {
			// Inflo el layout y obtengo la vista de datos.
			this.vistaDatos = getLayoutInflater().inflate(R.layout.info_mapa,
					null);
		}

		// Cuando se va a mostrar la ventana de informaci�n sobre el marcador.
		public View getInfoContents(Marker marcador) {
			// Obtengo las vistas correspondientes de la vistaDatos.
			TextView lblTitulo = (TextView) vistaDatos
					.findViewById(R.id.lblTitulo);
			TextView lblInfo = (TextView) vistaDatos.findViewById(R.id.lblInfo);
			// Le asigno los datos que deben mostrar.
			lblTitulo.setText(marcador.getTitle());
			lblInfo.setText(marcador.getSnippet());
			// Retorno la vista de datos.
			return vistaDatos;
		}

		// Si retorna null llama a getInfoContents, y si �ste retorna null,
		// llama a la ventana de info por defecto.
		public View getInfoWindow(Marker arg0) {
			return null;
		}

	}

}
