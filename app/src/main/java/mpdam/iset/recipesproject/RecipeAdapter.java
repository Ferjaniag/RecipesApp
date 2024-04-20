package mpdam.iset.recipesproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private List<Recipe> recipes;
    private Context mcontext ;

    public RecipeAdapter(List<Recipe> recipes, Context mcontext) {

        this.recipes = recipes;
        this.mcontext= mcontext;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    public void setRecipes(List<Recipe> newRecipes) {
        recipes.clear(); // Clear existing data
        recipes.addAll(newRecipes); // Add new data
        notifyDataSetChanged(); // Inform RecyclerView about changes
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        Log.d("MyApp", "Bound recipe " + recipe.getTitle() + " to position: " + position);
        holder.title.setText(recipe.getTitle());
        holder.description.setText(recipe.getDescription());
      //  holder.image.setImageURI(recipe.getImage());  // Assuming image resource ID
        Picasso.get().load(recipe.getImage())
                .into(holder.image);
        holder.prepareButton.setOnClickListener(v -> {
            Intent intent= new Intent(mcontext,DetailsRecipe.class) ;
            intent.putExtra("Image",recipes.get(holder.getAdapterPosition()).getImage()) ;
            intent.putExtra("Title",recipes.get(holder.getAdapterPosition()).getTitle()) ;

            intent.putExtra("Desc",recipes.get(holder.getAdapterPosition()).getDescription());
intent.putExtra("Calories",recipes.get(holder.getAdapterPosition()).getCalories());
Log.i("calories from adapter","msg : "+recipes.get(holder.getAdapterPosition()).getCalories()) ;
            intent.putExtra("Time",recipes.get(holder.getAdapterPosition()).getTotalTime());
            intent.putExtra("Ingredients",recipes.get(holder.getAdapterPosition()).getIngredients());
            mcontext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        ImageView image;
        Button prepareButton;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            description = itemView.findViewById(R.id.recipe_description);
            image = itemView.findViewById(R.id.recipe_image);
            prepareButton = itemView.findViewById(R.id.prepare_button);
        }
    }
}
