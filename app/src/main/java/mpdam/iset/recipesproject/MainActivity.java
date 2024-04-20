package mpdam.iset.recipesproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {



    private SensorManager sensorManager;
    private Sensor lightSensor,sensorRot;

    FrameLayout frameLayout ;
    TabLayout tabLayout;
    TextView titleRecipe,frameTitle ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








        setDefaultLanguage("en");

// Get SensorManager and Light Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorRot = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR); // Most common

        sensorManager.registerListener(this, sensorRot, SensorManager.SENSOR_DELAY_GAME); // Adjust delay as needed

        // Check if Light Sensor is available
        if (lightSensor == null) {
            Log.d("MainActivity", "Light sensor not available");
            return;
        }

        // Register Sensor Listener
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        frameLayout = findViewById(R.id.frameLayout) ;
        tabLayout=findViewById(R.id.tabLayout) ;



        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FragmentA())
                .addToBackStack(null)
                .commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment=null ;

                switch (tab.getPosition()) {
                    case 0 :
                        fragment = new FragmentA() ;
                        break ;
                    case 1 :
                        fragment = new FragmentB() ;
                        break ;
                    case 2 :
                        fragment = new FragmentC() ;
                        break ;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Rotaion

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            // Extract desired orientation values (e.g., azimuth, pitch, roll)
            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);

          //  Log.i("orientation hroziontal", orientation.toString()) ;
            // Use these values to update your UI or game logic
        }


        // Light

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];

            // Define thresholds for low and high light
            float LOW_LIGHT_THRESHOLD = 1000;
            float HIGH_LIGHT_THRESHOLD = 1500;
            // Adjust UI brightness based on light value
            if (lightValue < LOW_LIGHT_THRESHOLD) {

                tabLayout.setBackgroundColor(getResources().getColor(R.color.green));
            } else if (lightValue > HIGH_LIGHT_THRESHOLD) {
                // Set high brightness for UI elements
                tabLayout.setBackgroundColor(getResources().getColor(R.color.green_LightHight));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister Sensor Listeners to prevent memory leaks
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.FR) {
            // Handle settings action
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            setAppLocale("fr"); // Change language to French
            recreate(); // Refresh activity to apply changes
            return true;

        }
        else if (id == R.id.ES ){
            setAppLocale("es"); // Change language to Spanish
            recreate(); // Refresh activity to apply changes
            return true;
        }
        else if (id == R.id.IT ){
            setAppLocale("it"); // Change language to Spanish
          // Refresh activity to apply changes
            return true;
        } else if (id == R.id.DE ){
            setAppLocale("de"); // Change language to Spanish
            recreate(); // Refresh activity to apply changes

            return true;
        }
        recreate();
        return super.onOptionsItemSelected(item);
    }

    private void setAppLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        // Save selected language preference for future sessions if needed
        // You can use SharedPreferences or any other persistence mechanism
    }

    private void setDefaultLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }






}