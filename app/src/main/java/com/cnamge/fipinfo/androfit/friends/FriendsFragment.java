package com.cnamge.fipinfo.androfit.friends;

import android.content.Context;
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
import com.cnamge.fipinfo.androfit.model.FriendRequest;
import com.orm.SugarRecord;

import java.util.List;

public class FriendsFragment extends Fragment implements FriendsAdapter.Listener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<FriendRequest> requests;
    private FriendsAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.friends_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView ;
    }

    public void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private List<FriendRequest> getAllRequests() {
        return SugarRecord.find(FriendRequest.class, "user = ?", this.getActivity().getSharedPreferences(getString(R.string.preferences_file_label), Context.MODE_PRIVATE).getLong(getString(R.string.current_user_id), -1) + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        this.requests = getAllRequests();
        setItems(requests);
    }


    private void setItems(List<FriendRequest> items) {
        FriendsAdapter adapter = new FriendsAdapter(items, this, this.getContext());
        this.adapter = adapter;
        recyclerView.setAdapter(this.adapter);
    }

    @Override
    public void onItemClicked(FriendRequest item) {
        if (item.getAccepted() != null && item.getAccepted()){
            //TODO: Launch friend detail activity
            showMessage("Going on profile of " + item.getFriend().getUsername());
        }else{
            showMessage(getString(R.string.not_friends_msg));
        }
    }

    @Override
    public void onAcceptClicked(FriendRequest item, int position) {
        item.setAccepted(true);
        item.save();
        this.adapter.setItems(getAllRequests());
        this.adapter.notifyItemChanged(position);
    }

    @Override
    public void onDeclineClicked(FriendRequest item, int position) {
        item.setAccepted(false);
        item.save();
        this.adapter.setItems(getAllRequests());
        this.adapter.notifyItemChanged(position);
    }
}