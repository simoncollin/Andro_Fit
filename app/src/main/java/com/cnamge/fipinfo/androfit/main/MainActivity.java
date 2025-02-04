package com.cnamge.fipinfo.androfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.fragments.SettingsFragment;
import com.cnamge.fipinfo.androfit.friends.FriendsFragment;
import com.cnamge.fipinfo.androfit.meals.MealsEditActivity;
import com.cnamge.fipinfo.androfit.meals.MealsFragment;
import com.cnamge.fipinfo.androfit.sessions.sessionEdit.SessionEditActivity;
import com.cnamge.fipinfo.androfit.sessions.sessionsList.SessionsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements MainInterface {

    // Views objects
    private FloatingActionButton bottomBarButton;
    private ImageButton sessionsButton;
    private ImageButton mealsButton;
    private ImageButton friendsButton;
    private ImageButton settingsButton;
    private TextView appBarRightText;

    private MainPresenter mainPresenter;

    // Fragments instances
    private SessionsFragment sessionsFragment;
    private MealsFragment mealsFragment;
    private FriendsFragment friendsFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Top Bar
        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);

        // Initialize fragments
        this.sessionsFragment = new SessionsFragment();
        this.mealsFragment = new MealsFragment();
        this.friendsFragment = new FriendsFragment();
        this.settingsFragment = new SettingsFragment();

        this.showFragment(sessionsFragment);

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
                this.showFragment(this.sessionsFragment);
                this.mainPresenter.setCurrentFragment(MainPresenter.FragmentType.SESSIONS);
            } else if (view == this.mealsButton) {
                this.mealsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_meals);
                this.showFragment(this.mealsFragment);
                this.mainPresenter.setCurrentFragment(MainPresenter.FragmentType.MEALS);
            } else if (view == this.friendsButton) {
                this.friendsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_friends);
                this.showFragment(this.friendsFragment);
                this.mainPresenter.setCurrentFragment(MainPresenter.FragmentType.FRIENDS);
            } else if (view == this.settingsButton) {
                this.settingsButton.setAlpha(1f);
                this.appBarRightText.setText(R.string.title_settings);
                this.showFragment(this.settingsFragment);
                this.mainPresenter.setCurrentFragment(MainPresenter.FragmentType.SETTINGS);
            }
        }
    }

    @Override
    public void addSession() {
        Intent intent = new Intent(this, SessionEditActivity.class);
        intent.putExtra(getString(R.string.session_intent_edit_context_name), getString(R.string.session_intent_context_add));
        startActivity(intent);
    }

    @Override
    public void addMeal() {
        Intent intent = new Intent(this, MealsEditActivity.class);
        intent.putExtra(getString(R.string.meal_intent_edit_context_name), getString(R.string.meal_intent_context_add));
        startActivity(intent);
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
