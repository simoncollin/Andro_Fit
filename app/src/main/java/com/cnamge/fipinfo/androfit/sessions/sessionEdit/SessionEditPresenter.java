package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import com.cnamge.fipinfo.androfit.model.Session;

public class SessionEditPresenter {

    private SessionEditInterface mInterface;
    private Session currentSession;

    SessionEditPresenter(SessionEditInterface mInterface, long sessionId) {
        this.mInterface = mInterface;
        this.currentSession = Session.find(Session.class, "id = ?","" + sessionId).get(0);

        // TODO: setup view with current session info
        //mInterface.setupView(currentSession);
    }

    SessionEditPresenter(SessionEditInterface mInterface) {
        this.mInterface = mInterface;
        this.currentSession = new Session();

        // TODO: setup view with current session info
        //mInterface.setupView(currentSession);
    }

    void onDestroy() {
        mInterface = null;
    }

    void onCancelButtonClicked(){
        mInterface.cancel();
    }

    void onRegisterButtonClicked(String title, String location, long beginningDate, long endDate, String description){
        // TODO Enregistrer en BD les modifs
        mInterface.registerModification();
    }
}
