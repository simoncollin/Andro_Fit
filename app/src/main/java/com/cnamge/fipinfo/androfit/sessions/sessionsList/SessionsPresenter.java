package com.cnamge.fipinfo.androfit.sessions.sessionsList;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.CalendarInteractor;
import com.cnamge.fipinfo.androfit.model.Session;
import com.orm.SugarRecord;

import java.util.List;

public class SessionsPresenter implements SessionsAdapter.Listener {

    private SessionsInterface sessionsInterface;
    private List<Session> sessions;
    private SessionsAdapter adapter;
    private Context context;
    private CalendarInteractor calendarInteractor;
    private Session toDeleteSession;


    SessionsPresenter(SessionsInterface mInterface, Context context) {
        this.sessionsInterface = mInterface;
        this.context = context;
        this.calendarInteractor = new CalendarInteractor(this.sessionsInterface.getActivity());
        this.sessions = getAllSessions();
    }

    void setAdapter(SessionsAdapter adapter){
        this.adapter = adapter;
    }

    void onResume() {
        if (sessionsInterface != null) {
            sessionsInterface.setItems(sessions);
        }
    }

    void onDestroy() {
        sessionsInterface = null;
    }

    @Override
    public void onItemClicked(Session item) {
        if (sessionsInterface != null) {
            this.sessionsInterface.launchDetailActivity(item);
        }
    }

    public void onEditButtonClicked(int position){
        sessionsInterface.goToEditActivity(sessions.get(position));
    }

    public void onDeleteButtonClicked(int position){
        this.toDeleteSession = sessions.get(position);
        sessionsInterface.showMessage(this.toDeleteSession.getName() + context.getResources().getString(R.string.sessions_deleted));

        this.toDeleteSession.delete(); // Delete from DB
        this.sessions = getAllSessions(); // Refresh presenter sessions list

        // Notify adapter that item has been removed
        adapter.getItems().remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());

        Activity activity = this.sessionsInterface.getActivity();
        if (activity.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
            && activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 1);
        } else {
            this.deleteCalendarEvent();
        }
    }

    // TODO: remove if not used
    public SessionsInterface getViewInterface() {
        return sessionsInterface;
    }

    private List<Session> getAllSessions() {
        return SugarRecord.listAll(Session.class);
    }

    void deleteCalendarEvent() {
        if (this.toDeleteSession != null) {
            this.calendarInteractor.deleteCalendarEvent(this.toDeleteSession);
        }
    }
}
