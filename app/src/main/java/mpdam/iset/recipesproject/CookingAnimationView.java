package mpdam.iset.recipesproject;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CookingAnimationView extends View {
    private Paint potPaint;
    private Paint ingredientPaint;
    private int potWidth = 200; // Largeur du pot
    private int potHeight = 150; // Hauteur du pot
    private int potX, potY; // Coordonnées du pot
    private List<Ingredient> ingredients; // Liste des ingrédients
    private Random random;

    public CookingAnimationView(Context context) {
        super(context);
        init();
    }

    public CookingAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CookingAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        potPaint = new Paint();
        potPaint.setColor(Color.RED); // Couleur du pot (vous pouvez la modifier)

        ingredientPaint = new Paint();
        ingredientPaint.setColor(getResources().getColor(R.color.green)); // Couleur des ingrédients (vous pouvez la modifier)

        ingredients = new ArrayList<>();
        random = new Random();
        // Initialiser la position du pot
        potX = (getWidth() - potWidth) / 2;
        potY = getHeight() - potHeight - 50; // Ajuster selon votre mise en page
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Dessiner le pot
        canvas.drawRect(potX, potY, potX + potWidth, potY + potHeight, potPaint);

        // Ajouter de nouveaux ingrédients aléatoirement
        if (random.nextFloat() < 0.1) { // Changer le seuil pour ajuster la fréquence d'apparition des ingrédients
            int x = potX + random.nextInt(potWidth);
            int y = potY + potHeight / 2;
            Ingredient ingredient = new Ingredient(x, y);
            ingredients.add(ingredient);
        }

        // Dessiner et mettre à jour la position de chaque ingrédient
        List<Ingredient> removedIngredients = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            canvas.drawCircle(ingredient.getX(), ingredient.getY(), ingredient.getSize() / 2, ingredientPaint);
            ingredient.fall();
            if (ingredient.getY() > getHeight()) {
                removedIngredients.add(ingredient);
            }
        }

        // Supprimer les ingrédients sortis de l'écran
        ingredients.removeAll(removedIngredients);

        // Redessiner la vue après un court délai pour l'animation
        postInvalidateDelayed(30); // Ajuster le délai pour une animation plus fluide
    }

    // Classe interne pour représenter un ingrédient
    private class Ingredient {
        private int x;
        private int y;
        private int size;
        private int speed;

        public Ingredient(int x, int y) {
            this.x = x;
            this.y = y;
            this.size = 50; // Taille de l'ingrédient
            this.speed = random.nextInt(10) + 5; // Vitesse de chute aléatoire
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }

        public void fall() {
            y += speed;
        }
    }
}