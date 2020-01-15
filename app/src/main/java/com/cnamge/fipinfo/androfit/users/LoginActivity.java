package com.cnamge.fipinfo.androfit.users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.main.MainActivity;
import com.cnamge.fipinfo.androfit.model.User;
import com.facebook.share.Share;

public class LoginActivity extends AppCompatActivity {

    Button b1,b2;
    EditText ed1,ed2;

    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        long id = getSharedPreferences(getString(R.string.preferences_file_label), MODE_PRIVATE).getLong(getString(R.string.current_user_id), 0l);
        if (id != 0l){
            launchMainActivity();
        } else {

            b1 = (Button) findViewById(R.id.button);
            ed1 = (EditText) findViewById(R.id.editText);
            ed2 = (EditText) findViewById(R.id.editText2);

            b2 = (Button) findViewById(R.id.button2);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ed1.getText().toString().equals("admin") &&
                            ed2.getText().toString().equals("admin")) {
                        Toast.makeText(getApplicationContext(),
                                "Redirecting...", Toast.LENGTH_SHORT).show();
                        User user = new User("Toto");
                        user.save();
                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file_label), MODE_PRIVATE).getLong(getString(R.string.current_user_id), 0l);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong(user.getId());
                        launchMainActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();

                        counter--;

                        if (counter == 0) {
                            b1.setEnabled(false);
                        }
                    }
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file_label), MODE_PRIVATE).getLong(getString(R.string.current_user_id), 0l);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.current_user_id));
        editor.commit();
    }
}
