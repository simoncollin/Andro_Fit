package com.cnamge.fipinfo.androfit.sessions.sessionsList;

import android.content.Context;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;
import com.orm.SugarRecord;

import java.util.List;

public class SessionsPresenter implements SessionsAdapter.Listener {

    private SessionsInterface sessionsInterface;
    private List<Session> sessions;
    private SessionsAdapter adapter;
    private Context context;

    SessionsPresenter(SessionsInterface mInterface, Context context) {
        this.sessionsInterface = mInterface;
        this. context = context;
        sessions = getAllSessions();
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

    void onDeleteButtonClicked(int position){
        Session sessionDeleted = sessions.get(position);
        sessionsInterface.showMessage(sessionDeleted.getName() + context.getResources().getString(R.string.sessions_deleted));

        sessionDeleted.delete(); // Delete from DB
        this.sessions = getAllSessions(); // Refresh presenter sessions list

        // Notify adapter that item has been removed
        adapter.getItems().remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }

    private List<Session> getAllSessions() {
        return SugarRecord.listAll(Session.class);
    }
}
