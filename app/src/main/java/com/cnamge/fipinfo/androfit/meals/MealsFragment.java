package com.cnamge.fipinfo.androfit.meals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.helpers.SwipeController;
import com.cnamge.fipinfo.androfit.helpers.SwipeControllerActions;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.orm.SugarRecord;

import java.util.List;

public class MealsFragment extends Fragment implements MealsAdapter.Listener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeController swipeController;
    private List<Meal> meals;
    private MealsAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meals_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.meals_recycler_view);
        prepareRecyclerView();

        return rootView ;
    }

    private void prepareRecyclerView() {
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        SwipeControllerActions sca = new SwipeControllerActions() {
            @Override
            public void onDeleteClicked(int position) {
                onDeleteButtonClicked(position);
            }

            @Override
            public void onEditClicked(int position) {
                onEditButtonClicked(position);
            }
        };

        swipeController = new SwipeController(sca, this.getContext());

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void onEditButtonClicked(int position){
        Meal meal = this.meals.get(position);
        Intent intent = new Intent(this.getActivity(), MealsEditActivity.class);
        intent.putExtra(getString(R.string.session_intent_name), meal.getId());
        startActivity(intent);
    }

    private void onDeleteButtonClicked(int position){
        Meal meal = this.meals.get(position);
        meal.delete();
        showMessage(meal.getName() + getString(R.string.meal_deleted));
        this.meals = getAllMeals();
        adapter.getItems().remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }

    public void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private List<Meal> getAllMeals() {
        return SugarRecord.find(Meal.class, "creator = ?", this.getActivity().getSharedPreferences(getString(R.string.preferences_file_label), Context.MODE_PRIVATE).getLong(getString(R.string.current_user_id), -1) + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        this.meals = getAllMeals();
        setItems(meals);
    }


    private void setItems(List<Meal> items) {
        MealsAdapter adapter = new MealsAdapter(items, this);
        recyclerView.setAdapter(adapter);
        this.adapter = adapter;
    }

    @Override
    public void onItemClicked(Meal item) {
        Intent intent = new Intent(this.getActivity(), MealDetailActivity.class);
        intent.putExtra(getContext().getString(R.string.meal_intent_name), item.getId());
        startActivity(intent);
    }
}