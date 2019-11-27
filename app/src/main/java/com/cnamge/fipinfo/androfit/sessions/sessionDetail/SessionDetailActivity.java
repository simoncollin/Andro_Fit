package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

public class SessionDetailActivity extends AppCompatActivity implements SessionDetailInterface {

    private SessionDetailPresenter presenter;

    private TextView sessionTitle;
    private TextView sessionLocation;
    private TextView sessionDate;
    private TextView sessionDuration;
    private TextView sessionDescriptionContent;
    private TextView sessionDescriptionLabel;
    private ShareButton sessionShareOnFacebookButton;
    private ImageButton backButton;
    private ImageButton editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);
        appbarRightTextView.setText(getString(R.string.title_session_detail));

        linkActivityToXml();

        if (getIntent() == null || getIntent().getExtras() == null) {
            return;
        }

        long sessionId = (long) getIntent().getExtras().get(getString(R.string.session_intent_name));
        Session currentSession = Session.findById(Session.class, sessionId);
        this.presenter = new SessionDetailPresenter(this, currentSession);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);

        String text = currentSession.toString();
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.5f);
        int height = (int) (baseline + paint.descent() + 0.5f);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        this.sessionShareOnFacebookButton.setShareContent(content);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void linkActivityToXml(){
        this.sessionTitle = findViewById(R.id.session_detail_title);
        this.sessionLocation = findViewById(R.id.session_detail_location);
        this.sessionDate = findViewById(R.id.session_detail_date);
        this.sessionDuration = findViewById(R.id.session_detail_duration);
        this.sessionDescriptionLabel = findViewById(R.id.session_description_label);
        this.sessionDescriptionContent = findViewById(R.id.session_description_content);

        this.sessionShareOnFacebookButton = findViewById(R.id.session_detail_facebook_button);
        this.backButton = findViewById(R.id.session_detail_back_button);
        this.editButton = findViewById(R.id.session_detail_edit_button);

        this.editButton.setOnClickListener(v -> presenter.onEditButtonClicked());
        this.backButton.setOnClickListener(v -> presenter.onBackButtonClicked());
    }

    @Override
    public void setupView(Session session) {
        this.sessionTitle.setText(session.getName());
        this.sessionLocation.setText(session.getLocation());
        this.sessionDate.setText(session.getBeginDateHourString());
        this.sessionDuration.setText(session.getDurationString());

        if (session.getDescription().isEmpty() || session.getDescription() == null) {
            this.sessionDescriptionContent.setAlpha(0f);
            this.sessionDescriptionLabel.setAlpha(0f);
        }else{
            this.sessionDescriptionContent.setText(session.getDescription());
        }
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToEditActivity(Session session) {
        // TODO when edit activity is done
        Toast.makeText(this, "Go to edit " + session.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goBack() {
        this.finish();
    }


}