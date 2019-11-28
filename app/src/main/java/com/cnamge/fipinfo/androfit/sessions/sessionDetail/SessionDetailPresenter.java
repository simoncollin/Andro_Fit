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

    Bitmap getSessionBitmap() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        String text = this.detailledSession.toString();
        int width = 1200;
        int height = 630;

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawPaint(paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, 10, 25, paint);

        return image;
    }
}
