package com.cnamge.fipinfo.androfit.sessions.sessionEdit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;

public class SessionEditActivity extends AppCompatActivity implements SessionEditInterface {

    private SessionEditPresenter presenter;

    private Button cancelButton;
    private Button saveButton;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_edit_activity_layout);

        Toolbar appBar = findViewById(R.id.app_bar);
        setActionBar(appBar);
        TextView appbarRightTextView = findViewById(R.id.app_bar_rightTextView);

        linkActivityToXml();

        if (getIntent() != null && getIntent().getExtras() != null) {
            String context = (String) getIntent().getExtras().get(getString(R.string.session_intent_edit_context_name));
            if (context.equals(getString(R.string.session_intent_context_add))){
                appbarRightTextView.setText(R.string.title_session_add);
                this.presenter = new SessionEditPresenter(this);
            }else{
                appbarRightTextView.setText(R.string.title_session_edit);
                long sessionId = (long) getIntent().getExtras().get(getString(R.string.session_intent_name));
                this.presenter = new SessionEditPresenter(this, sessionId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void linkActivityToXml() {
        this.cancelButton = findViewById(R.id.session_cancel_button);
        this.saveButton = findViewById(R.id.session_save_button);
        this.cancelButton.setOnClickListener(v -> presenter.onCancelButtonClicked());
        this.saveButton.setOnClickListener(v -> presenter.onRegisterButtonClicked());

        this.descriptionEditText = findViewById(R.id.session_description_editText);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerModification() {
        // Todo: finish this activity and notify Session list of the new entry
        showMessage("Modification saved");
    }

    @Override
    public void cancel() { this.finish(); }

    @Override
    public void setupViewForEdition(Session session) {
        if (!(session.getDescription().isEmpty() || session.getDescription() == null)) {
            this.descriptionEditText.setText(session.getDescription());
        }
    }

    @Override
    public void setupViewForCreation() {}

}
