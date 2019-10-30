package com.cnamge.fipinfo.androfit;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);

        FloatingActionButton bottomBarButton = findViewById(R.id.bottom_bar_button);
        bottomBarButton.setOnClickListener(view -> this.onBottomBarButtonClick());

        ImageButton sessionsButon = findViewById(R.id.bottom_bar_sessions_button);
        ImageButton mealsButon = findViewById(R.id.bottom_bar_meals_button);
        ImageButton friendsButon = findViewById(R.id.bottom_bar_friends_button);
        ImageButton settingsButon = findViewById(R.id.bottom_bar_settings_button);

        sessionsButon.setOnClickListener(view -> Toast.makeText(this, "sessionsButtonClicked", Toast.LENGTH_SHORT).show());
        mealsButon.setOnClickListener(view -> Toast.makeText(this, "mealsButtonClicked", Toast.LENGTH_SHORT).show());
        friendsButon.setOnClickListener(view -> Toast.makeText(this, "friendsButtonClicked", Toast.LENGTH_SHORT).show());
        settingsButon.setOnClickListener(view -> Toast.makeText(this, "settingsButtonClicked", Toast.LENGTH_SHORT).show());
    }

    private void onBottomBarButtonClick(){
        Toast.makeText(this, "onBottomBarButtonClick", Toast.LENGTH_SHORT).show();
    }
}
