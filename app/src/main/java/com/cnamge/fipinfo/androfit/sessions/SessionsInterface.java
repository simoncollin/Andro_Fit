package com.cnamge.fipinfo.androfit.sessions;

import com.cnamge.fipinfo.androfit.model.Session;

import java.util.List;

public interface SessionsInterface {
    void setItems(List<Session> items);

    void showMessage(String message);
}
