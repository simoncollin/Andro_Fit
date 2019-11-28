package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cnamge.fipinfo.androfit.model.Session;

public class SessionDetailPresenter {

    private SessionDetailInterface sessionIDetailInterface;
    private Session detailledSession;

    SessionDetailPresenter(SessionDetailInterface mInterface, long sessionId) {
        this.sessionIDetailInterface = mInterface;
        this.detailledSession = Session.findById(Session.class, sessionId);
        sessionIDetailInterface.setupView(detailledSession);
    }

    void onDestroy() {
        sessionIDetailInterface = null;
    }

    void onBackButtonClicked(){
        sessionIDetailInterface.goBack();
    }

    void onEditButtonClicked(){
        sessionIDetailInterface.goToEditActivity(this.detailledSession);
    }

    Bitmap getBitmapFromSession() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);

        String text = this.detailledSession.toString();
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.5f);
        int height = (int) (baseline + paint.descent() + 0.5f);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);

        return image;
    }
}
