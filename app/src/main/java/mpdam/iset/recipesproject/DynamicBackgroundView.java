package mpdam.iset.recipesproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DynamicBackgroundView extends View {
    private Paint paint;
    private float radius;
    private int backgroundColor;

    public DynamicBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        radius = 0;
        backgroundColor = getResources().getColor(R.color.gr2); // Set your desired background color
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor); // Set the background color

        // Draw dynamic visual effects using canvas
        paint.setColor(getResources().getColor(R.color.gr)); // Set the color for the visual effect
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint); // Example: drawing a dynamic circle

        // Update the radius for animation
        radius += 5;
        if (radius > getWidth()) {
            radius = 0;
        }

        // Request a redraw
        invalidate();
    }
}

