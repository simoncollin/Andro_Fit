package com.cnamge.fipinfo.androfit.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.main.MainActivity;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.settings_fragments, container, false);
        Spinner languageSpinner = baseView.findViewById(R.id.languageSpinner);
        TextView mTextView = baseView.findViewById(R.id.langue);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.requireContext(),
                R.array.languageChoice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        if (LocaleHelper.getLanguage(getActivity().getApplicationContext()).equalsIgnoreCase("en")) {
            languageSpinner.setSelection(adapter.getPosition("English"));
        } else {
            languageSpinner.setSelection(adapter.getPosition("French"));
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Context context;
                Resources resources;
                switch (i) {
                    case 0:
                        context = LocaleHelper.setLocale(getActivity().getApplicationContext(), "fr");
                        resources = context.getResources();
                        mTextView.setText(resources.getString(R.string.langue));
                        break;
                    case 1:
                        context = LocaleHelper.setLocale(getActivity().getApplicationContext(), "en");
                        resources = context.getResources();
                        mTextView.setText(resources.getString(R.string.langue));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return baseView;
    }
/*
    public void changeLanguage(int langue){
        Context context;
        Resources resources;
        switch(langue){
            case 0:
                context = LocaleHelper.setLocale(this.getBaseContext(), "fr");
                resources = context.getResources();
                appBarRightText.setText(resources.getString(R.string.langue));
                break;
            case 1:
                context = LocaleHelper.setLocale(this.getBaseContext(), "en");
                resources = context.getResources();
                appBarRightText.setText(resources.getString(R.string.langue));
                break;
        }
*/



    }