package com.avinashdavid.trivialtrivia.UI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avinashdavid.trivialtrivia.R;
import com.avinashdavid.trivialtrivia.data.QuizDBContract;

import java.io.IOException;

public class ActivityWelcomePage extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        if (Build.VERSION.SDK_INT>=21) {
            setupWindowAnimations();
        }
//        Boolean isUserLoggedIn = false;

        username = getIntent().getStringExtra("username");
        if(username == null) {
            username = "Guest";
            (findViewById(R.id.card_view_socialize)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.textview_logout)).setText("Register");
            ((TextView)findViewById(R.id.textview_logout_subtitle)).setText("Let's Do it");
        }

        ((TextView)findViewById(R.id.toolbarText)).setText(username);

        ImageView imageView = (ImageView)findViewById(R.id.imageview_welcome_page);
        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;

        ((ImageView)findViewById(R.id.imageview_start)).setImageDrawable(getRoundedDrawable("start.png"));
        ((ImageView)findViewById(R.id.imageview_statistics)).setImageDrawable(getRoundedDrawable("statistics.jpg"));
        ((ImageView)findViewById(R.id.imageview_instructions)).setImageDrawable(getRoundedDrawable("instructions.png"));
        ((ImageView)findViewById(R.id.imageview_acknowledgments)).setImageDrawable(getRoundedDrawable("ack.jpg"));
        ((ImageView)findViewById(R.id.imageview_socialize)).setImageDrawable(getRoundedDrawable("socialize.png"));
        ((ImageView)findViewById(R.id.imageview_logout)).setImageDrawable(getRoundedDrawable("logout.png"));

        CardView startCardview = (CardView)findViewById(R.id.card_view_start_game);
        startCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ActivityWelcomePage.this, ActivityQuiz.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                Intent intent = new Intent(ActivityWelcomePage.this, SelectDifficultyLevel.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        CardView statsCardView = (CardView)findViewById(R.id.card_view_statistics);
        statsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = getContentResolver().query(QuizDBContract.QuizEntry.CONTENT_URI, new String[]{QuizDBContract.QuizEntry._ID},
                        null, null, null);
                int cursorCount = 0;
                if (cursor.moveToFirst()){
                    cursorCount = cursor.getCount();
                }
                if (cursorCount<=0){
                    Toast.makeText(ActivityWelcomePage.this, R.string.no_stats, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(ActivityWelcomePage.this, ActivityStatistics.class);
                    startActivity(intent);
                }
                cursor.close();
            }
        });

        ((CardView)findViewById(R.id.card_view_instructions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivityInstructions.class);
                startActivity(intent);
            }
        });

        ((CardView)findViewById(R.id.card_view_socialize)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivitySocialize.class);
                startActivity(intent);
            }
        });

        ((CardView)findViewById(R.id.card_view_acknowledgments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelcomePage.this, ActivityAcknowledgments.class);
                startActivity(intent);
            }
        });

        ((CardView)findViewById(R.id.card_view_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("userSession", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("LoggedIn", false);
                editor.commit();
                Intent intent = new Intent(ActivityWelcomePage.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });


//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if(extras != null) {
//            String userStr = extras.getString("isUserLoggedIn");
//            if (userStr != null && !userStr.isEmpty()) {
//                isUserLoggedIn = true;
//            }
//        }
//        if (!isUserLoggedIn) {
//            intent = new Intent(ActivityWelcomePage.this, UserLogin.class);
//            startActivity(intent);
//        }


    }

    @TargetApi(21)
    private void setupWindowAnimations(){
        try {
            Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
            getWindow().setExitTransition(fade);
        } catch (NoClassDefFoundError e){

        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    private RoundedBitmapDrawable getRoundedDrawable(String filename){
        try {
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), getAssets().open(filename));
            dr.setCornerRadius(500);
            return dr;
        }
        catch (IOException e){
//            Log.d("imageHandling", e.toString());
        }
        return null;
    }

}
