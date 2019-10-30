package com.cnamge.fipinfo.androfit;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.model.Meal;
import com.cnamge.fipinfo.androfit.model.Session;
import com.orm.SugarRecord;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);

        this.purgeAndReplaceFixtures(5);
    }

    protected void purgeAndReplaceFixtures(int count) {
        SugarRecord.deleteAll(Session.class);
        SugarRecord.deleteAll(Meal.class);

        for (int i = 1; i <= count; i++) {
            (new Session(
                "Session number " + i,
                "location " + i,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                "description " + i
            )).save();
            (new Meal(
                "Meal number " + i,
                System.currentTimeMillis(),
                "description " + i,
                "image " + i
            )).save();
        }

        List<Session> sessions = SugarRecord.listAll(Session.class);
        List<Meal> meals = SugarRecord.listAll(Meal.class);

        Log.v("RESULTS", "Sessions count: " + sessions.size());
        Log.v("RESULTS", "Meals count: " + meals.size());

        Session firstSession = sessions.get(0);
        Meal firstMeal = meals.get(0);
        Log.v("RESULTS", "First session: " + firstSession);
        Log.v("RESULTS", "First meal: " + firstMeal);

        Session lastSession = sessions.get(sessions.size()-1);
        Meal lastMeal = meals.get(meals.size()-1);
        Log.v("RESULTS", "Last session: " + lastSession);
        Log.v("RESULTS", "Last meal: " + lastMeal);
    }
}
