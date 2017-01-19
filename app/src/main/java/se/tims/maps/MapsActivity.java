package se.tims.maps;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap map;
    private UiSettings mapSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
		// init map
        map = googleMap;
        mapSettings = map.getUiSettings();

        GoogleMapOptions options = new GoogleMapOptions();

        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setScrollGesturesEnabled(true);

		// load marker if it exists
		Intent intent = getIntent();
		Parcelable parcelableMarker = intent.getParcelableExtra("MARKER");
		if (parcelableMarker != null)
		{
			zoomTo((LatLng) parcelableMarker);
		}
    }

    //Searches a location
    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFadress);
        String location = location_tf.getText().toString();
        if(location != null && !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList = null;
            Address address = null;
            try {
                addressList = geocoder.getFromLocationName(location , 32);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int results = addressList.size();
            if (results == 0)
            {
                Toast.makeText(this, getResources().getText(R.string.No_Result), Toast.LENGTH_SHORT).show();
                return;
            }
            if(results == 1)
            {
                address = addressList.get(0);
            }
            else // more than 1 result
            {
                Intent intent = new Intent(this, SearchResultActivity.class);
                intent.putParcelableArrayListExtra("SEARCH_RESULT", (ArrayList) addressList);
                startActivity(intent);
                return;
            }

            Log.d(getClass().getName(), address.toString());
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            zoomTo(latLng);
        }
    }

	public void zoomTo(LatLng latLng)
	{
		map.clear();
		map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,4f));
	}
}
