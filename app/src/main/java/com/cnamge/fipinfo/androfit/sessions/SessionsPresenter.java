package com.cnamge.fipinfo.androfit.sessions;

import com.cnamge.fipinfo.androfit.model.Session;
import com.orm.SugarRecord;

import java.util.List;

public class SessionsPresenter implements SessionsAdapter.Listener {

    private SessionsInterface sessionsInterface;
    private List<Session> sessions;

    SessionsPresenter(SessionsInterface mInterface) {
        this.sessionsInterface = mInterface;
        sessions = getAllSessions();
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
            sessionsInterface.showMessage(String.format("%s clicked", item.getName()));
        }
    }

    public void onEditButtonClicked(int position){
        sessionsInterface.showMessage("Edit button clicked : " + position);
    }

    public void onDeleteButtonClicked(int position){
        sessionsInterface.showMessage("Delete button clicked : " + position);
    }

    public SessionsInterface getViewInterface() {
        return sessionsInterface;
    }

    private List<Session> getAllSessions() {
        return SugarRecord.listAll(Session.class);
    }
}
