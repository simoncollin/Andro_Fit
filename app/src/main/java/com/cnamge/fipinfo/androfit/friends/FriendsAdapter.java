package com.cnamge.fipinfo.androfit.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.FriendRequest;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>  {

    interface Listener {
        void onItemClicked(FriendRequest item);
        void onAcceptClicked(FriendRequest item);
        void onDeclineClicked(FriendRequest item);
    }

    private List<FriendRequest> items;
    private Listener listener;
    private Context context;

    FriendsAdapter(List<FriendRequest> items, Listener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    List<FriendRequest> getItems() {
        return this.items;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item_layout, parent, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        final FriendRequest item = items.get(position);

        holder.sName.setText(item.getFriend().getUsername());
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));

        holder.acceptIV.setAlpha(0F);
        holder.declineIV.setAlpha(0F);

        if(item.getAccepted() == null){
            holder.acceptIV.setAlpha(1F);
            holder.declineIV.setAlpha(1F);
            holder.acceptIV.setOnClickListener(v -> listener.onAcceptClicked(item));
            holder.declineIV.setOnClickListener(v -> listener.onDeclineClicked(item));
        } else if(!item.getAccepted()){
            holder.itemView.setBackgroundColor(context.getColor(R.color.grey));
            holder.itemView.setAlpha(0.6F);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FriendsViewHolder extends RecyclerView.ViewHolder {

        TextView sName;
        ImageView declineIV;
        ImageView acceptIV;

        FriendsViewHolder(View request) {
            super(request);
            this.sName = request.findViewById(R.id.friend_request_username);
            this.acceptIV = request.findViewById(R.id.accept_request_button);
            this.declineIV = request.findViewById(R.id.decline_request_button);

        }

    }
}