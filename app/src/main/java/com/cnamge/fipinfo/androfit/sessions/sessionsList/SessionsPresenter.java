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
            this.sessions = getAllSessions();
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

    void onEditButtonClicked(int position){
        sessionsInterface.goToEditActivity(sessions.get(position));
    }
    
    public void onDeleteButtonClicked(int position){
        this.toDeleteSession = sessions.get(position);
        Activity activity = this.sessionsInterface.getActivity();
        if (activity.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
            && activity.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 1);
        } else {
            this.deleteCalendarEvent();
        }

        this.toDeleteSession.delete(); // Delete from DB
        sessionsInterface.showMessage(this.toDeleteSession.getName() + context.getResources().getString(R.string.sessions_deleted));

        this.sessions = getAllSessions(); // Refresh presenter sessions list

        // Notify adapter that item has been removed
        adapter.getItems().remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }

    private List<Session> getAllSessions() {
        return SugarRecord.find(Session.class, "creator = ?", context.getSharedPreferences(context.getString(R.string.preferences_file_label), Context.MODE_PRIVATE).getLong(context.getString(R.string.current_user_id), -1) + "");
    }

    void deleteCalendarEvent() {
        if (this.toDeleteSession != null) {
            this.calendarInteractor.deleteCalendarEvent(this.toDeleteSession);
        }
    }
}
