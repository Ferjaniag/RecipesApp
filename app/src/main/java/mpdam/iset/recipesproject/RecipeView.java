package mpdam.iset.recipesproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RecipeView extends View {

    private Bitmap recipeImage;
    private String recipeTitle = "Recipe Title";
    private String recipeDescription = "Brief description of the recipe.";

    // Animation variables (replace with your specific animation logic)
    private float animationX = 0f;
    private final float ANIMATION_SPEED = 5f;

    // Implement setters for recipe data (if needed)

    public RecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw recipe image (if available)
        if (recipeImage != null) {
            canvas.drawBitmap(recipeImage, (getWidth() - recipeImage.getWidth()) / 2f, 0f, null);
        }

        // Draw recipe title
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextSize(50f); // Adjust text size as needed
        titlePaint.setColor(Color.parseColor("#FF007A")); // Rose color
        titlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(recipeTitle, getWidth() / 2f, (titlePaint.getTextSize() + 50), titlePaint); // Adjust positioning

        // Draw recipe description
        Paint descriptionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        descriptionPaint.setTextSize(20f); // Adjust text size as needed
        descriptionPaint.setColor(Color.BLACK);
        String[] descriptionLines = recipeDescription.split("\n");
        float yPosition = titlePaint.getTextSize() + 100f; // Adjust positioning
        for (String line : descriptionLines) {
            canvas.drawText(line, getWidth() / 2f, yPosition, descriptionPaint);
            yPosition += descriptionPaint.getTextSize() * 1.2f; // Line spacing
        }

        // Update animation logic (replace with your animation)
        animationX += ANIMATION_SPEED;
        if (animationX > getWidth()) {
            animationX = 0f;
        }

        // Draw animation element (replace with your desired animation)
        Paint animationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animationPaint.setColor(Color.BLUE);
        canvas.drawRect(animationX, getHeight() - 50f, animationX + 100f, getHeight(), animationPaint);

        invalidate(); // Invalidate to trigger redraw for animation
    }
}


