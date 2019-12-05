package com.cnamge.fipinfo.androfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cnamge.fipinfo.androfit.R;

import hakobastvatsatryan.DropdownTextView;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.settings_fragments, container, false);
        DropdownTextView dropdown = container.findViewById(R.id.first_dropdown_text_view);
        //dropdown.setTitleText(R.string.desc_androfit);

        return baseView;
    }

}