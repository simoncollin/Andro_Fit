package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import com.cnamge.fipinfo.androfit.model.Session;

public interface SessionDetailInterface {

    void setupView(Session session);
    void showMessage(String text);
    void goToEditActivity(Session session);
    void goBack();
}
