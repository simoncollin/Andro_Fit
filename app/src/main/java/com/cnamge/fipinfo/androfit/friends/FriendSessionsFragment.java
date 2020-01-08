package com.cnamge.fipinfo.androfit.friends;

import android.content.Intent;
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
import com.cnamge.fipinfo.androfit.model.User;
import com.cnamge.fipinfo.androfit.sessions.sessionDetail.SessionDetailActivity;
import com.cnamge.fipinfo.androfit.sessions.sessionsList.SessionsAdapter;
import com.orm.SugarRecord;

import java.util.List;

public class FriendSessionsFragment extends Fragment implements SessionsAdapter.Listener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Session> sessions;
    private SessionsAdapter adapter;
    private User currentUser;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_sessions_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.friend_session_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView ;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private List<Session> getAllSessions() {
        return SugarRecord.find(Session.class, "creator = ?", currentUser.getId() + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        this.sessions = getAllSessions();
        setItems(sessions);
    }


    private void setItems(List<Session> items) {
        SessionsAdapter adapter = new SessionsAdapter(items, this);
        this.adapter = adapter;
        recyclerView.setAdapter(this.adapter);
    }


    @Override
    public void onItemClicked(Session item) {
        Intent intent = new Intent(this.getActivity(), SessionDetailActivity.class);
        intent.putExtra(getContext().getString(R.string.session_intent_name), item.getId());
        startActivity(intent);
    }
}
