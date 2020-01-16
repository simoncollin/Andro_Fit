package com.cnamge.fipinfo.androfit.meals;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.cnamge.fipinfo.androfit.model.User;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.orm.SugarRecord;


public class MealDetailActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 2;
    private TextView mealTitle;
    private TextView mealDate;
    private TextView mealTime;
    private TextView mealDescriptionContent;
    private ImageView imageView;
    private ShareButton mealShareOnFacebookButton;
    private ImageButton backButton;
    private ImageButton editButton;

    private Meal detailledMeal;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_detail_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);
        appbarRightTextView.setText(getString(R.string.title_meal_detail));

        linkActivityToXml();

        if (getIntent() == null || getIntent().getExtras() == null) {
            return;
        }

        long mealId = (long) getIntent().getExtras().get(getString(R.string.meal_intent_name));

        this.detailledMeal = Meal.findById(Meal.class, mealId);
        this.context = getApplicationContext();
        this.setupView(detailledMeal);
        this.imageView.setImageBitmap(BitmapFactory.decodeFile(this.detailledMeal.getimage_url()));
        this.setFacebookButtonShareContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Meal meal = SugarRecord.findById(Meal.class, detailledMeal.getId());
        this.detailledMeal = meal;
        setupView(detailledMeal);
    }

    private void linkActivityToXml(){
        this.imageView = findViewById(R.id.meal_detail_image_ic);
        this.mealTitle = findViewById(R.id.meal_detail_title);
        this.mealDate = findViewById(R.id.meal_detail_date);
        this.mealTime = findViewById(R.id.meal_detail_time);
        this.mealDescriptionContent = findViewById(R.id.meal_description_content);

        this.mealShareOnFacebookButton = findViewById(R.id.meal_detail_facebook_button);
        this.backButton = findViewById(R.id.meal_detail_back_button);
        this.editButton = findViewById(R.id.meal_detail_edit_button);

        this.backButton.setOnClickListener(v -> onBackButtonClicked());
    }

    public void setupView(Meal meal) {
        this.mealTitle.setText(meal.getName());
        this.mealDate.setText(meal.getDateString());
        this.mealTime.setText(meal.getTimeString());

        if ( meal.getDescription() == null || meal.getDescription().isEmpty()) {
            this.mealDescriptionContent.setAlpha(0f);
            findViewById(R.id.meal_description_label).setAlpha(0f);
        }else{
            this.mealDescriptionContent.setText(meal.getDescription());
        }

        User sessionCreator = meal.getCreator();
        User appOwner = SugarRecord.findById(User.class, getSharedPreferences(getString(R.string.preferences_file_label), Context.MODE_PRIVATE).getLong(getString(R.string.current_user_id), -1));

        if (!(sessionCreator.getId().equals(appOwner.getId()))){
            this.editButton.setAlpha(0F);
        }else{
            this.editButton.setOnClickListener(v -> onEditButtonClicked());
        }
    }

    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void goToEditActivity(Meal meal) {
        Intent intent = new Intent(this, MealsEditActivity.class);
        intent.putExtra(getString(R.string.meal_intent_name), meal.getId());
        intent.putExtra(getString(R.string.meal_intent_edit_context_name), getString(R.string.meal_intent_context_edit));
        startActivity(intent);
    }

    public void goBack() {
        this.finish();
    }

    public void setFacebookButtonShareContent() {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(getMealBitmap())
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#AndroFit")
                        .build()
                ).build();
        this.mealShareOnFacebookButton.setShareContent(content);
    }


    void onBackButtonClicked(){
        goBack();
    }

    void onEditButtonClicked(){
        goToEditActivity(this.detailledMeal);
    }

    Bitmap getMealBitmap() {
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
            return this.getMealBitmap();
        } else {
            return BitmapFactory.decodeFile(this.detailledMeal.getimage_url());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Toast.makeText(getApplicationContext(), "You must accept local storage read permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }
}