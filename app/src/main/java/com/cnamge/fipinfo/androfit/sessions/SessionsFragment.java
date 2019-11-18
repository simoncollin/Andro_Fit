package com.cnamge.fipinfo.androfit.sessions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;

import java.util.List;

public class SessionsFragment extends Fragment implements SessionsInterface {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private SessionsPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sessions_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.sessions_recycler_view);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        presenter = new SessionsPresenter(this);

        return rootView ;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setItems(List<Session> items) {
        recyclerView.setAdapter(new SessionsAdapter(items, this.presenter));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
