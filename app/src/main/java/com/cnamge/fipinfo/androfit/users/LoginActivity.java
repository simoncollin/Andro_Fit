package com.cnamge.fipinfo.androfit.users;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.helpers.MockDataHelper;
import com.cnamge.fipinfo.androfit.main.MainActivity;

import static com.cnamge.fipinfo.androfit.meals.MealsEditActivity.GALLERY_REQUEST_CODE;

public class LoginActivity extends AppCompatActivity {

    Button b1,b2;
    EditText ed1,ed2;

    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        MockDataHelper.setup();

        b1 = findViewById(R.id.button);
        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText2);
        ed1.setText("admin");
        ed2.setText("admin");

        b2 = findViewById(R.id.button2);
        b1.setOnClickListener(v -> {
            if (ed1.getText().toString().equals("admin") &&
                ed2.getText().toString().equals("admin")) {
                launchMainActivity();
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file_label), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(getString(R.string.current_user_id), MockDataHelper.currentUser.getId());
                editor.commit();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                counter--;
                if (counter == 0) {
                    b1.setEnabled(false);
                }
            }
        });
        b2.setOnClickListener(v -> finish());

        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        }
        if (this.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 1);
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
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file_label), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.current_user_id));
        editor.commit();
    }
}
