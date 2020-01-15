package com.cnamge.fipinfo.androfit.meals;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.main.MainActivity;
import com.cnamge.fipinfo.androfit.model.CalendarInteractor;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

public class MealsEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final int GALLERY_REQUEST_CODE = 2;
    private ImageView imageView;
    private Intent pictureIntent;

    public enum CurrentDatePicker {
        MEAL_DATE,
        MEAL_TIME
    }

    private FloatingActionButton addMealPictureButton;
    private EditText descriptionEditText;
    private EditText timeMeal;
    private EditText dateMeal;
    private EditText titleEditText;

    private TextView timeMealTextView;
    private TextView dateMealTextView;
    private TextView titleTextView;

    private Meal currentMeal;
    private Context context;

    private CurrentDatePicker currentDatePicker = null;


    private CalendarInteractor calendarInteractor;
    private int currentYear = 0;
    private int currentMonth = 0;
    private int currentDay = 0;

    private boolean dateHaveError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_add_edit_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);

        linkActivityToXml();

        if (getIntent() != null && getIntent().getExtras() != null) {
            String context = (String) getIntent().getExtras().get(getString(R.string.meal_intent_edit_context_name));
            if (context != null && context.equals(getString(R.string.meal_intent_context_add))){
                appbarRightTextView.setText(R.string.title_meal_add);

                this.context = this.getApplicationContext();
                this.currentMeal = new Meal();
                this.calendarInteractor = new CalendarInteractor(this.getActivity());
            }else{
                appbarRightTextView.setText(R.string.title_meal_edit);
                long mealId = (long) getIntent().getExtras().get(getString(R.string.meal_intent_name));
                this.context = this.getApplicationContext();
                this.currentMeal = Meal.findById(Meal.class, mealId);
                this.calendarInteractor = new CalendarInteractor(this.getActivity());
                this.setupViewForEdition(currentMeal);
            }
        }
        this.setEditionListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    void onCancelButtonClicked(){
        cancel();
    }

    public void registerModification() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(1,intent);
        finish();
        showMessage("Modifications saved");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar c = Calendar.getInstance();
        if (currentYear != 0) {
            c.set(currentYear, currentMonth, currentDay, hourOfDay, minute);
        } else {
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
        }
        long date = c.getTimeInMillis();
        try {
            currentMeal.setDate(date);
            currentMeal.setTime(date);
            refreshDateEditText(currentMeal.getDateString(), currentMeal.getTimeString());
            dateHaveError = false;
        } catch (Exception e){
            dateHaveError = true;
            showErrorOnDate(e.getMessage());
        }
    }

    void onDateClicked(){
        this.currentDatePicker = CurrentDatePicker.MEAL_DATE;
        showDatePicker(context.getString(R.string.meal_date_label));
    }

    void onTimeClicked(){
        this.currentDatePicker = CurrentDatePicker.MEAL_TIME;
        showTimePicker(context.getString(R.string.meal_time_label));
    }

    void onRegisterButtonClicked() {
        this.currentMeal.save();
        registerModification();
    }

    private void linkActivityToXml() {
        Button cancelButton = findViewById(R.id.meal_cancel_button);
        Button saveButton = findViewById(R.id.meal_save_button);

        this.imageView = findViewById(R.id.meal_image_ic);
        this.addMealPictureButton = findViewById(R.id.meal_add_picture_button);
        this.descriptionEditText = findViewById(R.id.meal_edit_add_description_Text);
        this.dateMeal = findViewById(R.id.meal_add_edit_date_content);
        this.timeMeal = findViewById(R.id.meal_add_edit_time_content);
        this.titleEditText = findViewById(R.id.meal_edit_title_content);

        this.timeMealTextView = findViewById(R.id.meal_add_edit_time_label);
        this.dateMealTextView = findViewById(R.id.meal_add_edit_date_label);
        this.titleTextView = findViewById(R.id.meal_edit_title_label);

        this.dateMeal.setOnClickListener(v -> onDateClicked());
        this.timeMeal.setOnClickListener(v -> onTimeClicked());
        this.addMealPictureButton.setOnClickListener(v -> onAddMealPictureButtonClick());

        cancelButton.setOnClickListener(v -> onCancelButtonClicked());
        saveButton.setOnClickListener(v -> onRegisterButtonClicked());
    }

    void onTitleEdited(String text) {
        try {
            currentMeal.setName(text);
        } catch (Exception e) {
            showErrorOnTitle(e.getMessage());
        }
    }

    void onDescriptionEdited(String text){
        this.currentMeal.setDescription(text);
    }

    void setEditionListener(){
        this.titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                titleEditText.setTextColor(getColor(R.color.black));
                titleTextView.setTextColor(getColor(R.color.mainGreen));
                titleEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.mainGreen)));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onTitleEdited(s.toString());
            }
        });

        this.descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onDescriptionEdited(s.toString());
            }
        });
    }

    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        this.finish();
    }

    public void setupViewForEdition(Meal meal) {
        if (!(meal.getDescription() == null || meal.getDescription().isEmpty())) {
            this.descriptionEditText.setText(meal.getDescription());
        }

        this.dateMeal.setText(meal.getDateString());
        this.titleEditText.setText(meal.getName());
        this.timeMeal.setText(meal.getTimeString());
    }

    void onDateEdited(int year, int monthOfYear, int dayOfMonth){
        this.currentDay = dayOfMonth;
        this.currentMonth = monthOfYear;
        this.currentYear = year;

        showTimePicker(context.getString(R.string.meal_time_label));
    }

    // DateTime picker methods
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        onDateEdited(year, monthOfYear, dayOfMonth);
    }

    public void showDatePicker(String title){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd;
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setCancelColor(getColor(R.color.white));
        dpd.setOkColor(getColor(R.color.white));
        dpd.setTitle(title);

        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    public void showTimePicker(String title){
        TimePickerDialog dpd;
        dpd = TimePickerDialog.newInstance(this, true);
        dpd.setVersion(TimePickerDialog.Version.VERSION_2);
        dpd.setCancelColor(getColor(R.color.white));
        dpd.setOkColor(getColor(R.color.white));
        dpd.setTitle(title);
        dpd.show(getSupportFragmentManager(), "Timepickerdialog");
    }

    public void refreshDateEditText(String dateMealString, String timeMealString) {
        this.dateMeal.setText(dateMealString);
        this.timeMeal.setText(timeMealString);
    }

    public void showErrorOnDate(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        this.timeMeal.setTextColor(getColor(R.color.red));
        this.timeMealTextView.setTextColor(getColor(R.color.red));
        this.timeMeal.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));

        this.dateMeal.setTextColor(getColor(R.color.red));
        this.dateMealTextView.setTextColor(getColor(R.color.red));
        this.dateMeal.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));

    }

    public void showErrorOnTitle(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        this.titleEditText.setTextColor(getColor(R.color.red));
        this.titleTextView.setTextColor(getColor(R.color.red));
        this.titleEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
    }

    public Activity getActivity() {
        return this;
    }

    void onAddMealPictureButtonClick(){
        pickFromGallery();
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        this.pictureIntent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        this.pictureIntent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        this.pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(this.pictureIntent,GALLERY_REQUEST_CODE);
    }

    protected void setCurrentMealImage() {
        Uri selectedImage = this.pictureIntent.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        // Get the cursor
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();

        this.currentMeal.setimage_url(imgDecodableString);
        this.currentMeal.save();
        this.imageView.setImageBitmap(BitmapFactory.decodeFile(this.currentMeal.getimage_url()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Toast.makeText(getApplicationContext(), "You must accept local storage read permissions", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (requestCode == GALLERY_REQUEST_CODE) {
                startActivityForResult(this.pictureIntent, GALLERY_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == GALLERY_REQUEST_CODE) {
                this.pictureIntent = data;
                if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
                } else {
                    this.setCurrentMealImage();
                }
            }
    }
}
