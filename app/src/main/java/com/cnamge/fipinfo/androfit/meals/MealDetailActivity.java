package com.cnamge.fipinfo.androfit.meals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;


public class MealDetailActivity extends AppCompatActivity {

    private TextView mealTitle;
    private TextView mealDate;
    private TextView mealTime;
    private TextView mealDescriptionContent;
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
        this.setFacebookButtonShareContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void linkActivityToXml(){
        this.mealTitle = findViewById(R.id.meal_detail_title);
        this.mealDate = findViewById(R.id.meal_detail_date);
        this.mealTime = findViewById(R.id.meal_detail_time);
        this.mealDescriptionContent = findViewById(R.id.meal_description_content);

        this.mealShareOnFacebookButton = findViewById(R.id.meal_detail_facebook_button);
        this.backButton = findViewById(R.id.meal_detail_back_button);
        this.editButton = findViewById(R.id.meal_detail_edit_button);

        this.editButton.setOnClickListener(v -> onEditButtonClicked());
        this.backButton.setOnClickListener(v -> onBackButtonClicked());
    }

    public void setupView(Meal meal) {
        this.mealTitle.setText(meal.getName());
        this.mealDate.setText(meal.getDateString());
        this.mealTime.setText(meal.getTimeString());

        if ( meal.getDescription() == null || meal.getDescription().isEmpty()) {
            this.mealDescriptionContent.setAlpha(0f);
        }else{
            this.mealDescriptionContent.setText(meal.getDescription());
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
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        int width = 1200;
        int height = 630;
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawPaint(paint);
        Drawable logo = this.context.getResources().getDrawable(R.mipmap.ic_launcher_round, null);
        Rect imageBounds = new Rect(50, 50, 200, 200);
        logo.setBounds(imageBounds);
        logo.draw(canvas);

        String[] lines = new String[4];
        lines[0] = this.detailledMeal.getName();
        lines[1] = this.context.getResources().getString(R.string.meal_date_label) + " : " + this.detailledMeal.getDateString();
        lines[2] = this.context.getResources().getString(R.string.meal_time_label) + " : " + this.detailledMeal.getTimeString();
        lines[3] = this.context.getResources().getString(R.string.description_label) + " : " + this.detailledMeal.getDescription();


        paint.setColor(Color.BLACK);
        int textSize = 60;
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2) - 2*textSize);

        for (String line : lines) {
            canvas.drawText(line, xPos, yPos, paint);
            yPos += textSize;
        }

        return image;
    }
}