package mpdam.iset.recipesproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsRecipe extends AppCompatActivity {

    ImageView recipe_img ;
    TextView recipe_title,recipe_cal, recipe_prep, ingrd_list ;

    ArrayList<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_recipe);

        recipe_img=findViewById(R.id.recipe_image) ;
        recipe_title=findViewById(R.id.recipe_title) ;
        recipe_cal=findViewById(R.id.recipe_calories) ;
        recipe_prep=findViewById(R.id.recipe_preparation_time) ;
        ingrd_list=findViewById(R.id.ingredients_list) ;

        Bundle bundle=getIntent().getExtras() ;


        if (bundle != null) {
            recipe_title.setText(bundle.getString("Title"));
           recipe_cal.setText(String.valueOf(bundle.getInt("Calories"))+" Calory");

           // Integer cal= bundle.getInt("Calories") ;
           // Log.i("calories fromdetails","msg from details "+cal) ;
            Picasso.get().load(bundle.getString("Image"))
                    .into(recipe_img);


            // Animation properties (adjust as needed)
            long animationDuration = 2000; // 2 seconds
            float rotationAngle = 360f; // Full rotation
            float scaleFactor = 1.1f; // Scale factor for zoom effect



// Create ObjectAnimator for zoom animation
            ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(recipe_img, "scaleX", 1f, scaleFactor);
            scaleXAnim.setDuration(animationDuration / 2); // Zoom in for half the duration
            scaleXAnim.setRepeatCount(1); // Repeat once
            scaleXAnim.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation on repeat

            ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(recipe_img, "scaleY", 1f, scaleFactor);
            scaleYAnim.setDuration(animationDuration / 2); // Zoom in for half the duration
            scaleYAnim.setRepeatCount(1); // Repeat once
            scaleYAnim.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation on repeat

// Create an AnimatorSet to play both animations together
            AnimatorSet animatorSet = new AnimatorSet();

            animatorSet.start();






            recipe_prep.setText(String.valueOf(bundle.getInt("Time"))+" Minutes");

            ingredients = bundle.getStringArrayList("Ingredients");


            for (int i=0; i< ingredients.size();i++) {
                ingrd_list.setText(ingrd_list.getText()+"\n • "+ ingredients.get(i));
                Log.i("Ingredients : ","\n "+ ingredients.get(i)) ;
            }



        }



    }


}