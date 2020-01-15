package com.cnamge.fipinfo.androfit.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.fragments.MealsFragment;
import com.cnamge.fipinfo.androfit.fragments.FriendsFragment;
import com.cnamge.fipinfo.androfit.fragments.SettingsFragment;
import com.cnamge.fipinfo.androfit.friends.FriendsFragment;
import com.cnamge.fipinfo.androfit.model.FriendRequest;
import com.cnamge.fipinfo.androfit.meals.MealsEditActivity;
import com.cnamge.fipinfo.androfit.meals.MealsFragment;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.cnamge.fipinfo.androfit.model.Session;
import com.cnamge.fipinfo.androfit.model.User;
import com.cnamge.fipinfo.androfit.sessions.sessionEdit.SessionEditActivity;
import com.cnamge.fipinfo.androfit.sessions.sessionsList.SessionsFragment;
import com.cnamge.fipinfo.androfit.users.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orm.SugarRecord;

import java.util.List;

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

        //FOR TEST ONLY
        try {
            this.purgeAndReplaceFixtures();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    
    // TODO: Remove after devs
    protected void purgeAndReplaceFixtures() throws Exception{
        SugarRecord.deleteAll(Meal.class);
        //SugarRecord.deleteAll(User.class);
        SugarRecord.deleteAll(FriendRequest.class);
        //SugarRecord.deleteAll(Session.class);

        //User currentUser = new User("Toto");
        //currentUser.save();
        long userId = this.getSharedPreferences(getString(R.string.preferences_file_label), Context.MODE_PRIVATE).getLong(getString(R.string.current_user_id), 0);
        User currentUser = SugarRecord.findById(User.class, userId);
//        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_label), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.remove(getString(R.string.current_user_id));
//        editor.putLong(getString(R.string.current_user_id), currentUser.getId());
//        editor.commit();


        for (int i = 1; i <= 5; i++) {
            String desc;
            Boolean accepted;
            if (i % 3 == 0) {
                desc = "description " + i;
                accepted = true;
            } else if (i % 3 == 1) {
                desc = "";
                accepted = false;
            } else {
                desc = "";
                accepted = null;
            }

            User user = new User("friend n° " + i);
            user.save();

            (new FriendRequest(currentUser, accepted, user)).save();

            for (int j = 0; j < 3; j++) {
                (new Meal(
                        "Meal number " + j,
                        System.currentTimeMillis() + (j * 6000),
                        desc,
                        "Meal url " + j,
                        user
                )).save();
                (new Session(
                        "Name " + j,
                        "Location " + j,
                        System.currentTimeMillis() + (j * 6000),
                        System.currentTimeMillis() + (j * 66060),
                        "Description " + j,
                        0,
                        user
                )).save();
            }
        }

        List<Session> sessions = SugarRecord.listAll(Session.class);
        List<Meal> meals = SugarRecord.listAll(Meal.class);
        List<User> users = SugarRecord.listAll(User.class);
        List<FriendRequest> friendsRequest = SugarRecord.listAll(FriendRequest.class);

        Log.v("purgeAndReplaceFixtures", "Sessions count: " + sessions.size());
        Log.v("purgeAndReplaceFixtures", "Meals count: " + meals.size());
        Log.v("purgeAndReplaceFixtures", "users count: " + users.size());
        Log.v("purgeAndReplaceFixtures", "friendsRequest count: " + friendsRequest.size());

        Session firstSession = sessions.get(0);
        Meal firstMeal = meals.get(0);
        User firstUser = users.get(0);
        FriendRequest firstFriendRequest = friendsRequest.get(0);

        Log.v("purgeAndReplaceFixtures", "difference temps : " + firstSession.getDurationString());
        Log.v("purgeAndReplaceFixtures", "First session: " + firstSession);
        Log.v("purgeAndReplaceFixtures", "First meal: " + firstMeal);
        Log.v("purgeAndReplaceFixtures", "First user: " + firstUser);
        Log.v("purgeAndReplaceFixtures", "First friend request: " + firstFriendRequest);

        Session lastSession = sessions.get(sessions.size() - 1);
        Meal lastMeal = meals.get(meals.size() - 1);
        User lastUser = users.get(users.size() - 1);
        FriendRequest lastFriendRequest = friendsRequest.get(friendsRequest.size() - 1);

        Log.v("purgeAndReplaceFixtures", "Last session: " + lastSession);
        Log.v("purgeAndReplaceFixtures", "Last meal: " + lastMeal);
        Log.v("purgeAndReplaceFixtures", "Last user: " + lastUser);
        Log.v("purgeAndReplaceFixtures", "Last Friend request: " + lastFriendRequest);
    }
}
