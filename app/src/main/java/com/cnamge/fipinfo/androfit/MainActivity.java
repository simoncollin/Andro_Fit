package com.cnamge.fipinfo.androfit;

import android.os.Bundle;
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
    }

    private void onBottomBarButtonClick(){
        Toast.makeText(this, "onBottomBarButtonClick", Toast.LENGTH_SHORT).show();
    }
}
