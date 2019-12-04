package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;

class SessionDetailPresenter {

    private SessionDetailInterface sessionIDetailInterface;
    private Session detailledSession;
    private Context context;

    SessionDetailPresenter(SessionDetailInterface mInterface, long sessionId, Context context) {
        this.sessionIDetailInterface = mInterface;
        this.detailledSession = Session.findById(Session.class, sessionId);
        this.context = context;
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
        int width = 1200;
        int height = 630;
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawPaint(paint);
        Drawable logo = this.context.getResources().getDrawable(R.mipmap.ic_launcher_round, null);
        Rect imageBounds = new Rect(50, 50, 200, 200);
        logo.setBounds(imageBounds);
        logo.draw(canvas);

        String[] lines = new String[4];
        lines[0] = this.detailledSession.getName();
        lines[1] = this.context.getResources().getString(R.string.location_label) + " : " + this.detailledSession.getLocation();
        lines[2] = this.context.getResources().getString(R.string.begin_date_label) + " : " + this.detailledSession.getBeginDateHourString();
        lines[3] = this.context.getResources().getString(R.string.duration_label) + " : " + this.detailledSession.getDurationString();


        paint.setColor(Color.BLACK);
        int textSize = 60;
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2) - 2*textSize);

        for (String line : lines) {
            canvas.drawText(line, xPos, yPos, paint);
            yPos += textSize;
        }

        return image;
    }

    void onActivityResult() {
        this.detailledSession = Session.findById(Session.class, this.detailledSession.getId());
        sessionIDetailInterface.setupView(this.detailledSession);
    }
}
