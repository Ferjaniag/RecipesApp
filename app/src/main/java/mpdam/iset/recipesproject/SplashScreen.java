package mpdam.iset.recipesproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private View circle1, circle2, circle3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );




        TextView backgroundImage = findViewById(R.id.SplashScreentext2);
        TextView backgroundText1 = findViewById(R.id.SplashScreentext1);
Button button= findViewById(R.id.SplashScreenButton) ;

        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        backgroundImage.startAnimation(slideAnimation);
        backgroundText1.startAnimation(slideAnimation);


button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
});
 /*       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);   */


    }
}