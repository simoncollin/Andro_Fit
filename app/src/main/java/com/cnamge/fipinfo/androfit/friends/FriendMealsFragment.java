package com.cnamge.fipinfo.androfit.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.meals.MealDetailActivity;
import com.cnamge.fipinfo.androfit.meals.MealsAdapter;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.cnamge.fipinfo.androfit.model.User;
import com.orm.SugarRecord;

import java.util.List;

public class FriendMealsFragment extends Fragment implements MealsAdapter.Listener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Meal> meals;
    private MealsAdapter adapter;
    private User currentUser;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_meals_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.friend_meals_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView ;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private List<Meal> getAllMeals() {
        return SugarRecord.find(Meal.class, "creator = ?", currentUser.getId() + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        this.meals = getAllMeals();
        setItems(meals);
    }


    private void setItems(List<Meal> items) {
        MealsAdapter adapter = new MealsAdapter(items, this);
        this.adapter = adapter;
        recyclerView.setAdapter(this.adapter);
    }

    @Override
    public void onItemClicked(Meal item) {
        Intent intent = new Intent(this.getActivity(), MealDetailActivity.class);
        intent.putExtra(getContext().getString(R.string.meal_intent_name), item.getId());
        startActivity(intent);
        showMessage(item.toString());
    }
}
