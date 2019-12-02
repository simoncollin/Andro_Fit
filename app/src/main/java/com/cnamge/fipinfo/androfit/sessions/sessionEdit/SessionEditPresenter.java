package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.cnamge.fipinfo.androfit.model.CalendarInteractor;
import com.cnamge.fipinfo.androfit.model.Session;

public class SessionEditPresenter {

    public enum SessionEditContext {
        MODIFICATION,
        CREATION
    }

    private SessionEditInterface mInterface;
    private Session currentSession;
    private SessionEditContext currentEditContext;
    private CalendarInteractor calendarInteractor;


    SessionEditPresenter(SessionEditInterface mInterface, long sessionId) {
        this.mInterface = mInterface;
        this.currentSession = Session.findById(Session.class, sessionId);
        this.currentEditContext = SessionEditContext.MODIFICATION;
        this.calendarInteractor = new CalendarInteractor(this.mInterface.getActivity());
        mInterface.setupViewForEdition(currentSession);
    }

    SessionEditPresenter(SessionEditInterface mInterface) {
        this.mInterface = mInterface;
        this.currentSession = new Session();
        this.currentEditContext = SessionEditContext.CREATION;
        this.calendarInteractor = new CalendarInteractor(this.mInterface.getActivity());
        mInterface.setupViewForCreation();
    }

    void onDestroy() {
        mInterface = null;
    }

    void onCancelButtonClicked(){
        mInterface.cancel();
    }

    void onRegisterButtonClicked() {
        this.currentSession.save();
        mInterface.registerModification();
        Activity activity = this.mInterface.getActivity();
        if (activity.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
            && activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 1);
        } else {
            this.saveCalendarEvent();
        }
    }

    void saveCalendarEvent() {
        this.calendarInteractor.saveCalendarEvent(this.currentSession);
    }
}
