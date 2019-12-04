package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import android.content.Context;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class SessionEditPresenter {

    public enum CurrentDatePicker {
        BEGINNING_DATE,
        END_DATE
    }

    private SessionEditInterface mInterface;
    private Session currentSession;
    private Context context;

    private CurrentDatePicker currentDatePicker = null;
    private int currentYear = 0;
    private int currentMonth = 0;
    private int currentDay = 0;

    private boolean dateHaveError = false;
    private boolean locationHaveError = false;
    private boolean titleHaveError = false;


    SessionEditPresenter(SessionEditInterface mInterface, Context context, long sessionId) {
        this.mInterface = mInterface;
        this.currentSession = Session.find(Session.class, "id = ?","" + sessionId).get(0);
        this.context = context;
        mInterface.setupViewForEdition(currentSession);
    }

    SessionEditPresenter(SessionEditInterface mInterface, Context context) {
        this.mInterface = mInterface;
        this.context = context;
        this.currentSession = new Session();
    }

    void onDestroy() {
        mInterface = null;
    }

    void onCancelButtonClicked(){
        mInterface.cancel();
    }

    void onRegisterButtonClicked(){
        if (!formHaveError()){
            this.currentSession.save();
            mInterface.registerModification();
        }else {
            mInterface.showMessage(context.getString(R.string.form_error_message));
        }
    }

    private boolean formHaveError(){
        return dateHaveError || locationHaveError || titleHaveError;
    }

    void onEndDateClicked(){
        this.currentDatePicker = CurrentDatePicker.END_DATE;
        mInterface.showDatePicker(context.getString(R.string.end_date_label));
    }

    void onBeginningDateClicked(){
        this.currentDatePicker = CurrentDatePicker.BEGINNING_DATE;
        mInterface.showDatePicker(context.getString(R.string.begin_date_label));
    }

    void onDateEdited(int year, int monthOfYear, int dayOfMonth){
        this.currentDay = dayOfMonth;
        this.currentMonth = monthOfYear;
        this.currentYear = year;
        String title;
        if (this.currentDatePicker == CurrentDatePicker.BEGINNING_DATE){
            title = context.getString(R.string.begin_date_label);
        } else {
            title = context.getString(R.string.end_date_label);
        }
        mInterface.showTimePicker(title);
    }

    void onTimeEdited(int hour, int minute) {
            Calendar c = Calendar.getInstance();
            c.set(currentYear, currentMonth, currentDay, hour, minute);
            long date = c.getTimeInMillis();
        try {
            if (this.currentDatePicker == CurrentDatePicker.BEGINNING_DATE) {
                currentSession.setBeginDate(date);
                onEndDateClicked();
            } else {
                currentSession.setEndDate(date);
            }
            mInterface.refreshDateEditText(currentSession.getBeginDateHourString(), currentSession.getEndDateHourString());
            dateHaveError = false;
        } catch (Exception e){
            dateHaveError = true;
            mInterface.showErrorOnDate(e.getMessage());
        } finally {
            String tmp = new SimpleDateFormat("dd/MM/YYYY - HH:mm", Locale.FRANCE).format(new Date(date));

            if (this.currentDatePicker == CurrentDatePicker.BEGINNING_DATE) {
                if (currentSession.getEndDate() == 0f){
                    mInterface.refreshDateEditText(tmp, "");
                } else {
                    mInterface.refreshDateEditText(tmp, currentSession.getEndDateHourString());
                }
            } else {
                if (currentSession.getBeginDate() == 0f){
                    mInterface.refreshDateEditText("", tmp);
                } else {
                    mInterface.refreshDateEditText(currentSession.getBeginDateHourString(), tmp);
                }
            }
        }
    }

    void onDescriptionEdited(String text){
        this.currentSession.setDescription(text);
    }

    void onLocationEdited(String text){
        try {
            currentSession.setLocation(text);
        }catch (Exception e){
            mInterface.showErrorOnLocation(e.getMessage());
        }
    }

    void onTitleEdited(String text){
        try {
            currentSession.setName(text);
        }catch (Exception e){
            mInterface.showErrorOnTitle(e.getMessage());
        }
    }
}
