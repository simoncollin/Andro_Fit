package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.main.MainActivity;
import com.cnamge.fipinfo.androfit.model.Session;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class SessionEditActivity extends AppCompatActivity implements SessionEditInterface, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private SessionEditPresenter presenter;

    private Button cancelButton;
    private Button saveButton;

    private EditText descriptionEditText;
    private EditText endDateEditText;
    private EditText beginningDateEditText;
    private EditText locationEditText;
    private EditText titleEditText;

    private TextView endDateTextView;
    private TextView beginningDateTextView;
    private TextView locationTextView;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_edit_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);

        if (getIntent() != null && getIntent().getExtras() != null) {
            String context = (String) getIntent().getExtras().get(getString(R.string.session_intent_edit_context_name));
            if (context != null && context.equals(getString(R.string.session_intent_context_add))){
                appbarRightTextView.setText(R.string.title_session_add);
                this.presenter = new SessionEditPresenter(this, this.getApplicationContext());
            }else{
                appbarRightTextView.setText(R.string.title_session_edit);
                long sessionId = (long) getIntent().getExtras().get(getString(R.string.session_intent_name));
                this.presenter = new SessionEditPresenter(this, this.getApplicationContext(), sessionId);
            }
        }

        linkActivityToXml();

        setEditionListener();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void linkActivityToXml() {
        this.cancelButton = findViewById(R.id.session_cancel_button);
        this.saveButton = findViewById(R.id.session_save_button);

        this.cancelButton.setOnClickListener(v -> Log.println(Log.ERROR, "TEST", "Test"));
        this.saveButton.setOnClickListener(v -> presenter.onRegisterButtonClicked());

        this.descriptionEditText = findViewById(R.id.session_description_editText);
        this.endDateEditText = findViewById(R.id.session_edit_end_date_content);
        this.beginningDateEditText = findViewById(R.id.session_edit_beginning_date_content);
        this.locationEditText = findViewById(R.id.session_edit_location_content);
        this.titleEditText = findViewById(R.id.session_edit_title_content);

        this.endDateTextView = findViewById(R.id.session_edit_end_date_label);
        this.beginningDateTextView = findViewById(R.id.session_edit_beginning_date_label);
        this.locationTextView = findViewById(R.id.session_edit_location_label);
        this.titleTextView = findViewById(R.id.session_edit_title_label);

        this.endDateEditText.setOnClickListener(v -> presenter.onEndDateClicked());
        this.beginningDateEditText.setOnClickListener(v -> presenter.onBeginningDateClicked());
    }

    void setEditionListener(){
        this.locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                locationEditText.setTextColor(getColor(R.color.black));
                locationTextView.setTextColor(getColor(R.color.mainGreen));
                locationEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.mainGreen)));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.onLocationEdited(s.toString());
            }
        });

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
                presenter.onTitleEdited(s.toString());
            }
        });

        this.descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                presenter.onDescriptionEdited(s.toString());
            }
        });
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerModification() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(1,intent);
        finish();
        showMessage("Modifications saved");
    }

    @Override
    public void cancel() {
        this.finish();
    }

    @Override
    public void setupViewForEdition(Session session) {
        if (!(session.getDescription() == null || session.getDescription().isEmpty())) {
            this.descriptionEditText.setText(session.getDescription());
        }

        this.endDateEditText.setText(session.getEndDateHourString());
        this.beginningDateEditText.setText(session.getBeginDateHourString());
        this.locationEditText.setText(session.getLocation());
        this.titleEditText.setText(session.getName());
    }

    // DateTime picker methods
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        presenter.onDateEdited(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        presenter.onTimeEdited(hourOfDay, minute);
    }

    @Override
    public void showDatePicker(String title){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd;
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setCancelColor(getColor(R.color.white));
        dpd.setOkColor(getColor(R.color.white));
        dpd.setTitle(title);

        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void showTimePicker(String title){
        TimePickerDialog dpd;
        dpd = TimePickerDialog.newInstance(this, true);
        dpd.setVersion(TimePickerDialog.Version.VERSION_2);
        dpd.setCancelColor(getColor(R.color.white));
        dpd.setOkColor(getColor(R.color.white));
        dpd.setTitle(title);
        dpd.show(getSupportFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void refreshDateEditText(String beginningDateStr, String endDateStr) {
        this.endDateEditText.setText(endDateStr);
        this.beginningDateEditText.setText(beginningDateStr);
    }

    @Override
    public void showErrorOnDate(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        this.endDateEditText.setTextColor(getColor(R.color.red));
        this.endDateTextView.setTextColor(getColor(R.color.red));
        this.endDateEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));

        this.beginningDateEditText.setTextColor(getColor(R.color.red));
        this.beginningDateTextView.setTextColor(getColor(R.color.red));
        this.beginningDateEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));

    }

    @Override
    public void showErrorOnTitle(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        this.titleEditText.setTextColor(getColor(R.color.red));
        this.titleTextView.setTextColor(getColor(R.color.red));
        this.titleEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
    }

    @Override
    public void showErrorOnLocation(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        this.locationEditText.setTextColor(getColor(R.color.red));
        this.locationTextView.setTextColor(getColor(R.color.red));
        this.locationEditText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
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
                this.presenter.saveCalendarEvent();
            }
        }
    }
}
