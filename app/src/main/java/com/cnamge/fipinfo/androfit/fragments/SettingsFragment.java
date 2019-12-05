package com.cnamge.fipinfo.androfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cnamge.fipinfo.androfit.R;

import hakobastvatsatryan.DropdownTextView;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.settings_fragments, container, false);

        DropdownTextView dropdown = new DropdownTextView.Builder(this.getContext())
                .setTitleTextRes(R.string.credits)
                .setContentTextRes(R.string.desc_androfit)
                .setTitleTextSizeRes(R.dimen.dropdown_title)
                .setContentTextSizeRes(R.dimen.dropdown_content)
                .build();

        LinearLayout layout = baseView.findViewById(R.id.settings_linear_layout);
        layout.addView(dropdown);
        return baseView;
    }
}