package com.cnamge.fipinfo.androfit.friends;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.User;

public class FriendDetailActivity extends AppCompatActivity {

    User currentFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail_activity);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);

        linkActivityToXml();

        if (getIntent() != null && getIntent().getExtras() != null) {
            long userId = (long) getIntent().getExtras().get("FRIEND_ID");
            this.currentFriend = User.findById(User.class, userId);
            appbarRightTextView.setText(currentFriend.getUsername());
        }
    }

    private void linkActivityToXml(){
        //TODO
    }
}
