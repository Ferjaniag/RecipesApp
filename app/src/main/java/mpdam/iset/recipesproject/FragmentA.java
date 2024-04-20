package mpdam.iset.recipesproject;



import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FragmentA extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
FrameLayout frameLayoutA ;
    private MapView mapView;
    private GoogleMap map;
    private SearchView searchView;

    String apiKey ;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Button showTrackButton;
    private Marker searchedPlaceMarker;
    private LatLng searchedPlace;
    private LatLng userLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        // Use application context to access resources
        Context context = getActivity().getApplicationContext();

        // Securely access API key from resource
        try {
            apiKey = context.getString(R.string.google_maps_key);
            Log.i("api key",apiKey) ;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            // Handle missing API key gracefully (e.g., show an error message)
            return view;
        }


        showTrackButton = view.findViewById(R.id.showTrackButton);
        showTrackButton.setVisibility(View.GONE); // Hide the button initially
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Ensure map is ready before use

        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        // Initialize the requestPermissionLauncher
        // Request location permission if not granted yet
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is granted, get current location
            getCurrentLocation();
        }

        // Initialize the searchView
        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search based on query
                // Example:
                String search= searchView.getQuery().toString();
                geoLocate(search);
                showTrackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTrack();
                    }
                });      Log.i("text search","search :"+query) ;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search suggestions or partial input
                if (newText.equals("")) {
                    getCurrentLocation();
                }
                return false;
            }
        });

        return view;
    }



    // Method to get current location
    private void getCurrentLocation() {
        // Check if the app has permission to access the location
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, handle this case gracefully (e.g., show a message to the user)
            Log.d("FragmentA", "Location permission not granted");
            // Request location permission if not granted yet
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return; // Return early as we can't proceed without permission
        }

        // Permission is granted, proceed to get the current location
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng currentLatLng = new LatLng(latitude, longitude);

                userLocation=currentLatLng ;
                map.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            } else {
                // Handle case where location is null
                Log.d("FragmentA", "Current location is null");
            }
        }).addOnFailureListener(getActivity(), e -> {
            // Handle failure to get location
            Log.e("FragmentA", "Failed to get location: " + e.getMessage());
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Enable the My Location layer if the permission is granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("FragmentA", "Location permission granted");
            // Enable My Location layer
            map.setMyLocationEnabled(true);
        } else {
            Log.d("FragmentA", "Location permission not granted");
            // Request location permission if not granted yet
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Set API key at application level (only once)
        MapsInitializer.initialize(getContext());
        if (map != null) {
            try {
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

            } catch (Resources.NotFoundException e) {
                // Handle style loading failure
            }

        }

        // Set initial map settings, markers, etc.
    }

    // Method to handle search queries
    private void geoLocate(String query) {
        Geocoder geocoder = new Geocoder(requireContext());
        List<Address> addressList;
        try {
            // Define a bounding box for Tunisia
            double countrySouthLat = 30.2326;
            double countryNorthLat = 37.3441;
            double countryWestLng = 7.5241;
            double countryEastLng = 11.5981;

            addressList = geocoder.getFromLocationName(query, 1, countrySouthLat, countryWestLng, countryNorthLat, countryEastLng);

            Log.i("address list: ", "list " + addressList);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);

                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                LatLng location = new LatLng(latitude, longitude);
                searchedPlace = new LatLng(latitude, longitude);

                // Add a marker at the searched location
                if (searchedPlaceMarker != null) {
                    searchedPlaceMarker.remove(); // Remove previous marker if exists
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                searchedPlaceMarker = map.addMarker(new MarkerOptions().position(searchedPlace).title(query));
                // Make the button visible
                showTrackButton.setVisibility(View.VISIBLE);

                // Optionally, add a marker at the searched location
                map.clear();
                map.addMarker(new MarkerOptions().position(location).title(query));
            } else {
                // Handle case where no location matches the query
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            // Handle exception if geocoding fails
            e.printStackTrace();
            Toast.makeText(requireContext(), "Geocoding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showTrack() {
        // Check if the user's current location and the searched place are available
        if (userLocation != null && searchedPlace != null) {
            // Draw a track between the user's current location and the searched place
            drawTrack(userLocation, searchedPlace);
        }
    }
    private void drawTrack(LatLng startPoint, LatLng endPoint) {

        Log.i("ena fi draw","draw") ;
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(startPoint, endPoint)
                .color(Color.BLUE)
                .width(5);
        map.addPolyline(polylineOptions);


    }









}