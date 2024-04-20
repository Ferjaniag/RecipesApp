package mpdam.iset.recipesproject;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentC extends Fragment implements SensorEventListener {

    FrameLayout frameLayoutC ;

    TextView frameTitleC,recipeTitle,recipeDescription;
    Button prepareButton ;
    CardView recipeCard;

    private RecyclerView recipeList;
    private RecipeAdapter adapter;
    String url="https://api.edamam.com/search?q=dessert&app_id=dd36f90c&app_key=0146a33d993f5593b28c0ba17ac3e449" ;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_c, container, false);

        frameLayoutC = view.findViewById(R.id.frameLayoutC) ;
        // Initialize views after inflating the layout
        recipeList = view.findViewById(R.id.recipe_list);
        frameTitleC = view.findViewById(R.id.frameTitleC) ;

        View CardLayout = inflater.inflate(R.layout.recipe_card, container, false);
        recipeCard=CardLayout.findViewById(R.id.recCard) ;
        recipeTitle=CardLayout.findViewById(R.id.recipe_title) ;
        recipeDescription=CardLayout.findViewById(R.id.recipe_description) ;
        prepareButton=CardLayout.findViewById(R.id.prepare_button) ;

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Register Sensor Listener
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        adapter = new RecipeAdapter(new ArrayList<>(),view.getContext());
        recipeList.setAdapter(adapter);
        recipeList.setLayoutManager(new GridLayoutManager(view.getContext(), 2));




        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)  {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray hits = jsonObject.getJSONArray("hits");
                    List<Recipe> recipes = new ArrayList<>();
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject hit = hits.getJSONObject(i);
                        Recipe recipe = parseRecipe(hit);
                        recipes.add(recipe);
                    }

                    Log.i("MyApp", "Fetched recipes: " + recipes.size());
                    // Prepare recipe data (replace with your actual data source)

                    adapter.setRecipes(recipes); // Update adapter with new data
                    adapter.notifyDataSetChanged(); // Notify adapter of changes
                    Log.i("MyApp", "Adapter data size: " + adapter.getItemCount());

                    Log.i("MyApp", "RecyclerView layout manager set: " + recipeList.getLayoutManager());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest) ;







        return view ;
    }


    private Recipe parseRecipe(JSONObject hit) throws JSONException {
        JSONObject recipeObject = hit.getJSONObject("recipe");
        String label = recipeObject.getString("label"); // Adjust property names as needed
        String image = recipeObject.getString("image");
        String description = recipeObject.getString("label");
        int calories = recipeObject.getInt("calories");
        int totalTime = recipeObject.getInt("totalTime"); // Check if exists
        JSONArray ingredientsArray = recipeObject.getJSONArray("ingredients");
        ArrayList<String> ingredients = new ArrayList<>();
        for (int j = 0; j < ingredientsArray.length(); j++) {
            JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
            String text = ingredientObject.getString("text");
            ingredients.add(text);
        }
        return new Recipe(image, label, description, calories, totalTime, ingredients);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];

            // Define thresholds for low and high light
            float LOW_LIGHT_THRESHOLD = 1000;
            float HIGH_LIGHT_THRESHOLD = 1500;
            // Adjust UI brightness based on light value
            if (isAdded()) {
                if (lightValue < LOW_LIGHT_THRESHOLD) {
                    // Set low brightness for UI elements
                    frameLayoutC.setBackgroundColor(requireContext().getResources().getColor(R.color.green_light));
                    frameTitleC.setTextColor(requireContext().getResources().getColor(R.color.green));
                    recipeCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.rose_light));
                    recipeTitle.setTextColor(requireContext().getResources().getColor(R.color.rose));
                    prepareButton.setBackgroundColor(requireContext().getResources().getColor(R.color.rose));
                } else if (lightValue > HIGH_LIGHT_THRESHOLD) {
                    // Set high brightness for UI elements
                    frameTitleC.setTextColor(requireContext().getResources().getColor(R.color.green_LightHight));
                    frameLayoutC.setBackgroundColor(requireContext().getResources().getColor(R.color.green_light_LightHigh));

                    recipeCard.setCardBackgroundColor(requireContext().getResources().getColor(R.color.rose_light_LightHigh));
                    recipeTitle.setTextColor(requireContext().getResources().getColor(R.color.rose_LightHigh));
                    prepareButton.setBackgroundColor(requireContext().getResources().getColor(R.color.rose_LightHigh));
                    recipeDescription.setTextColor(requireContext().getResources().getColor(R.color.white));
                }
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Update views with translated text here
        updateViews();
    }

    private void updateViews() {
        // Update text views, buttons, etc. with translated text
        frameTitleC.setText(getString(R.string.fragment_title_de));
    }
}