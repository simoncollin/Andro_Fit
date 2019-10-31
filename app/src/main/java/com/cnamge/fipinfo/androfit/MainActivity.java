package com.cnamge.fipinfo.androfit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements MainInterface{

    // Bottom bar buttons
    private FloatingActionButton bottomBarButton;
    private ImageButton sessionsButton;
    private ImageButton mealsButton;
    private ImageButton friendsButton;
    private ImageButton settingsButton;
    private TextView appBarRightText;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);

        this.mainPresenter = new MainPresenter(this);
        this.setupBottomBar();
    }

    private void setupBottomBar(){
        this.bottomBarButton = findViewById(R.id.bottom_bar_button);
        this.sessionsButton = findViewById(R.id.bottom_bar_sessions_button);
        this.mealsButton = findViewById(R.id.bottom_bar_meals_button);
        this.friendsButton = findViewById(R.id.bottom_bar_friends_button);
        this.settingsButton = findViewById(R.id.bottom_bar_settings_button);
        this.appBarRightText = findViewById(R.id.app_bar_rightTextView);

        //Attach event listener
        this.bottomBarButton.setOnClickListener(view -> mainPresenter.onBottomBarAddButtonClick());
        this.sessionsButton.setOnClickListener(view -> mainPresenter.onBottomBarSessionsButtonClick(view));
        this.mealsButton.setOnClickListener(view -> mainPresenter.onBottomBarMealsButtonClick(view));
        this.friendsButton.setOnClickListener(view -> mainPresenter.onBottomBarFriendsButtonClick(view));
        this.settingsButton.setOnClickListener(view -> mainPresenter.onBottomBarSettingsButtonClick(view));

        this.selectView(this.sessionsButton);
    }

    // Activity life cycle
    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onDestroy();
        super.onDestroy();
    }

    // Interface methods
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selectView(View view){
        if (view != null) {
            this.sessionsButton.setAlpha(0.5f);
            this.mealsButton.setAlpha(0.5f);
            this.friendsButton.setAlpha(0.5f);
            this.settingsButton.setAlpha(0.5f);

            if (view == this.sessionsButton) {
                this.sessionsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_sessions);
            } else if (view == this.mealsButton) {
                this.mealsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_meals);
            } else if (view == this.friendsButton) {
                this.friendsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_friends);
            } else if (view == this.settingsButton) {
                this.settingsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_settings);
            }
        }
    }
}
