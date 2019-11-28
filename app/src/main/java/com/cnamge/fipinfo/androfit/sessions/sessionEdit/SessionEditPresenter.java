package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import com.cnamge.fipinfo.androfit.model.Session;

public class SessionEditPresenter {

    public enum SessionEditContext {
        MODIFICATION,
        CREATION
    }

    private SessionEditInterface mInterface;
    private Session currentSession;
    private SessionEditContext currentEditContext;


    SessionEditPresenter(SessionEditInterface mInterface, long sessionId) {
        this.mInterface = mInterface;
        this.currentSession = Session.find(Session.class, "id = ?","" + sessionId).get(0);
        this.currentEditContext = SessionEditContext.MODIFICATION;
        mInterface.setupViewForEdition(currentSession);
    }

    SessionEditPresenter(SessionEditInterface mInterface) {
        this.mInterface = mInterface;
        this.currentSession = new Session();
        this.currentEditContext = SessionEditContext.CREATION;
        mInterface.setupViewForCreation();
    }

    void onDestroy() {
        mInterface = null;
    }

    void onCancelButtonClicked(){
        mInterface.cancel();
    }

    void onRegisterButtonClicked(){
        // TODO Enregistrer en BD les modifs
        mInterface.registerModification();
    }
}
