package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import com.cnamge.fipinfo.androfit.model.Session;

public interface SessionEditInterface {

    void showMessage(String text);
    void registerModification();
    void cancel();

    void setupViewForEdition(Session session);
    void setupViewForCreation();
}
