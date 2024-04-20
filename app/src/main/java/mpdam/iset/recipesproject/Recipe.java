package mpdam.iset.recipesproject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String image ;
    private String title ;
    private String description ;
    private int calories ;
    private int totalTime ;
    private ArrayList ingredients ;



    public Recipe(String title, String description, String image) {
        this.image=image ;
        this.title=title;
        this.description=description ;
    }
    public Recipe(String image, String title, String description, int calories, int totalTime, ArrayList ingredients) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.calories = calories;
        this.totalTime = totalTime;
        this.ingredients = ingredients;
    }
// getter and setters


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public ArrayList getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList ingredients) {
        this.ingredients = ingredients;
    }
}
