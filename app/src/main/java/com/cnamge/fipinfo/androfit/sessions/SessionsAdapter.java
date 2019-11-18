package com.cnamge.fipinfo.androfit.sessions;

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
        holder.textView.setText(item.toString());
        holder.textView.setOnClickListener(v -> listener.onItemClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Objet représentant une cellule de la recyclerView
    static class SessionsViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        SessionsViewHolder(View session_item_view) {
            super(session_item_view);
            this.textView = session_item_view.findViewById(R.id.session_item_textView);
        }
    }
}
