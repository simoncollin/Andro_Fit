package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import android.app.Activity;

import com.cnamge.fipinfo.androfit.model.Session;

public interface SessionEditInterface {

    void showMessage(String text);
    void registerModification();
    void cancel();
    void setupViewForEdition(Session session);
    void showDatePicker(String title);
    void showTimePicker(String title);
    void refreshDateEditText(String beginningDateStr, String endDateStr);
    void showErrorOnDate(String message);
    void showErrorOnTitle(String message);
    void showErrorOnLocation(String message);
    void setupViewForCreation();
    Activity getActivity();
}
