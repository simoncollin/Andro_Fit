package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;
import com.cnamge.fipinfo.androfit.sessions.sessionEdit.SessionEditActivity;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.cnamge.fipinfo.androfit.sessions.sessionEdit.SessionEditActivity;


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
        this.presenter = new SessionDetailPresenter(this, sessionId, getApplicationContext());
        this.setFacebookButtonShareContent();
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

        if ( session.getDescription() == null || session.getDescription().isEmpty()) {
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
        Intent intent = new Intent(this, SessionEditActivity.class);
        intent.putExtra(getString(R.string.session_intent_name), session.getId());
        intent.putExtra(getString(R.string.session_intent_edit_context_name), getString(R.string.session_intent_context_edit));
        startActivity(intent);
    }

    @Override
    public void goBack() {
        this.finish();
    }

    public void setFacebookButtonShareContent() {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(this.presenter.getSessionBitmap())
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#AndroFit")
                        .build()
                ).build();
        this.sessionShareOnFacebookButton.setShareContent(content);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult();
    }
}