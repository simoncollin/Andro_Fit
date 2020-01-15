package com.cnamge.fipinfo.androfit.meals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Meal;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealsViewHolder>  {

    public interface Listener {
        void onItemClicked(Meal item);
    }

    private List<Meal> items;
    private Listener listener;

    public MealsAdapter(List<Meal> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    List<Meal> getItems() {
        return this.items;
    }

    @Override
    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_layout, parent, false);
        return new MealsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsViewHolder holder, int position) {
        final Meal item = items.get(position);

        holder.sName.setText(item.getName());
        holder.sDate.setText(item.getDateString());
        holder.sTime.setText(item.getTimeString());
        holder.loadImageFromStorage(item.getimage_url());

        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MealsViewHolder extends RecyclerView.ViewHolder {

        TextView sTime;
        ImageView sImage;
        TextView sName;
        TextView sDate;

        MealsViewHolder(View meal) {
            super(meal);
            this.sName = meal.findViewById(R.id.meal_name);
            this.sDate = meal.findViewById(R.id.meal_date);
            this.sTime = meal.findViewById(R.id.meal_time);
            this.sImage = meal.findViewById(R.id.meal_image_ic);
        }

        private void loadImageFromStorage(String path){
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                sImage.setImageBitmap(b);
            } catch (Exception e){
                e.printStackTrace();
                sImage.setImageResource(R.mipmap.ic_image);
            }
        }
    }
}