package com.cnamge.fipinfo.androfit.sessions.sessionsList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.model.Session;

import java.util.List;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionsViewHolder>  {

    interface Listener {
        void onItemClicked(Session item);
    }

    private List<Session> items;
    private Listener listener;

    public SessionsAdapter(List<Session> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    public List<Session> getItems() {
        return this.items;
    }

    @Override
    // Méthode appelé en interne pour généré une nouvelle cell. Doit retourner un objet de type ViewHolder
    public SessionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item_layout, parent, false);
        return new SessionsViewHolder(v);
    }

    @Override
    // Méthode qui sert à configurer l'interface
    public void onBindViewHolder(@NonNull SessionsViewHolder holder, int position) {
        final Session item = items.get(position);

        holder.sName.setText(item.getName());
        holder.sLocation.setText(item.getLocation());
        holder.sDate.setText(item.getBeginDateString());
        holder.sDuration.setText(item.getDurationString());
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Objet représentant une cellule de la recyclerView
    static class SessionsViewHolder extends RecyclerView.ViewHolder {

        TextView sName;
        TextView sLocation;
        TextView sDate;
        TextView sDuration;

        SessionsViewHolder(View session_item_view) {
            super(session_item_view);
            this.sName = session_item_view.findViewById(R.id.session_name);
            this.sLocation = session_item_view.findViewById(R.id.session_location);
            this.sDate = session_item_view.findViewById(R.id.session_date);
            this.sDuration = session_item_view.findViewById(R.id.session_duration);
        }
    }
}
