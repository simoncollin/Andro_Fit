package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;

public class SessionEditActivity extends AppCompatActivity implements SessionEditInterface {

    public enum SessionEditContext {
        MODIFICATION,
        CREATION
    }

    private SessionEditPresenter presenter;
    private SessionEditContext currentEditContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_edit_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);

        if (getIntent() != null && getIntent().getExtras() != null) {
            String context = (String) getIntent().getExtras().get(getString(R.string.session_intent_edit_context_name));
            if (context.equals(getString(R.string.session_intent_context_add))){
                appbarRightTextView.setText(R.string.title_session_add);
                this.currentEditContext = SessionEditContext.CREATION;
                this.presenter = new SessionEditPresenter(this);
            }else{
                appbarRightTextView.setText(R.string.title_session_edit);
                this.currentEditContext = SessionEditContext.MODIFICATION;
                long sessionId = (long) getIntent().getExtras().get(getString(R.string.session_intent_name));
                this.presenter = new SessionEditPresenter(this, sessionId);
            }
        }

        linkActivityToXml();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void linkActivityToXml() {

    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerModification() {
        // Todo: finish this activity and notify Session list of the new entry
    }

    @Override
    public void cancel() { this.finish(); }

}
