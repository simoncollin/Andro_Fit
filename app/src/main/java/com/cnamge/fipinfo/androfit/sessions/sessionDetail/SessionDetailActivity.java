package com.cnamge.fipinfo.androfit.sessions.sessionDetail;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;

public class SessionDetailActivity extends AppCompatActivity implements SessionDetailInterface{

    private SessionDetailPresenter presenter;

    private TextView textViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);
        appbarRightTextView.setText(getString(R.string.title_session_detail));

        this.textViewTest = findViewById(R.id.textView_test);

        //récuparation de l'intent
        if (getIntent() != null && getIntent().getExtras() != null) {
            long sessionId = (long) getIntent().getExtras().get(getString(R.string.session_intent_name));
            this.presenter = new SessionDetailPresenter(this, sessionId);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public void setTextViewText(String text) {
        textViewTest.setText(text);
    }
}
