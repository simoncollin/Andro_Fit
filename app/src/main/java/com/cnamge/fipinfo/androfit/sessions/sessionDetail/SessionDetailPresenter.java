package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import com.cnamge.fipinfo.androfit.model.Session;

class SessionDetailPresenter {

    private SessionDetailInterface sessionIDetailInterface;
    private Session detailledSession;

    SessionDetailPresenter(SessionDetailInterface mInterface, long sessionId) {
        this.sessionIDetailInterface = mInterface;
        this.detailledSession = Session.find(Session.class, "id = ?","" + sessionId).get(0);
        sessionIDetailInterface.setupView(detailledSession);
    }

    void onDestroy() {
        sessionIDetailInterface = null;
    }

    void onFacebookButtonClicked() {
        // TODO back : Fonctionnalité de partage sur facebook
        sessionIDetailInterface.showMessage("Facebook sharing button clicked");
    }

    void onBackButtonClicked(){
        sessionIDetailInterface.goBack();
    }

    void onEditButtonClicked(){
        sessionIDetailInterface.goToEditActivity(this.detailledSession);
    }

    void onActivityResult() {
        this.detailledSession = Session.findById(Session.class, this.detailledSession.getId());
        sessionIDetailInterface.setupView(this.detailledSession);
    }
}
