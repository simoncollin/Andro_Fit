package com.cnamge.fipinfo.androfit.friends;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.User;
import com.google.android.material.tabs.TabLayout;

public class FriendDetailActivity extends AppCompatActivity {

    User currentFriend;

    // Fragments instances
    private FriendSessionsFragment sessionsFragment;
    private FriendMealsFragment mealsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail_activity);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);

        // Initialize fragments
        this.sessionsFragment = new FriendSessionsFragment();
        this.mealsFragment = new FriendMealsFragment();

        linkActivityToXml();

        if (getIntent() != null && getIntent().getExtras() != null) {
            long userId = (long) getIntent().getExtras().get("FRIEND_ID");
            this.currentFriend = User.findById(User.class, userId);
            this.mealsFragment.setCurrentUser(currentFriend);
            this.sessionsFragment.setCurrentUser(currentFriend);
            appbarRightTextView.setText(currentFriend.getUsername());
        }

        showFragment(sessionsFragment);
    }

    private void linkActivityToXml(){
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        showFragment(sessionsFragment);
                        break;
                    case 1:
                        showFragment(mealsFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.friend_frame, fragment);
        fragmentTransaction.commit();
    }
}
