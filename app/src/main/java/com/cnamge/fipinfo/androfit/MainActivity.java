package com.cnamge.fipinfo.androfit;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.model.Session;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Session session = new Session();
        session.setName("Session name");
        session.save();

        Session persisted = Session.findById(Session.class, session.getId());

        TextView test = findViewById(R.id.app_bar_rightTextView);
        test.setText(persisted.getName());

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
    }
}
