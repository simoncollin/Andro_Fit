package com.cnamge.fipinfo.androfit.meals;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.main.MainActivity;
import com.cnamge.fipinfo.androfit.model.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import android.Manifest;
import android.content.Context;

import androidx.core.app.ActivityCompat;

import com.cnamge.fipinfo.androfit.model.CalendarInteractor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MealsEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

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
        c.set(currentYear, currentMonth, currentDay, hourOfDay, minute);
        long date = c.getTimeInMillis();
        try {
            if (this.currentDatePicker == CurrentDatePicker.MEAL_DATE) {
                currentMeal.setDate(date);

            } else {
                currentMeal.setTime(date);
            }
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

    void saveCalendarEvent() {
        this.calendarInteractor.saveCalendarEvent(this.currentMeal);
    }

    void onRegisterButtonClicked() {
        this.currentMeal.save();
        registerModification();
        Activity activity = this.getActivity();
        if (activity.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 1);
        } else {
            this.saveCalendarEvent();
        }
    }

    private void linkActivityToXml() {
        Button cancelButton = findViewById(R.id.meal_cancel_button);
        Button saveButton = findViewById(R.id.meal_save_button);

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
    }

    void onDateEdited(int year, int monthOfYear, int dayOfMonth){
        this.currentDay = dayOfMonth;
        this.currentMonth = monthOfYear;
        this.currentYear = year;
        String title;
        if (this.currentDatePicker == CurrentDatePicker.MEAL_DATE){
            title = context.getString(R.string.meal_date_label);
        } else {
            title = context.getString(R.string.meal_time_label);
        }
        showTimePicker(title);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (requestCode == 1) {
                Toast.makeText(getApplicationContext(), "You must accept agenda permissions to synchronize your sessions in your agenda", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (requestCode == 1) {
                saveCalendarEvent();
            }
        }
    }

    void onAddMealPictureButtonClick(){
        showMessage("Add Meal button clicked !");
    }
}
