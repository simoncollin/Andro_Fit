package com.cnamge.fipinfo.androfit.sessions.sessionsList;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;
import com.cnamge.fipinfo.androfit.helpers.SwipeController;
import com.cnamge.fipinfo.androfit.helpers.SwipeControllerActions;
import com.cnamge.fipinfo.androfit.model.Session;
import com.cnamge.fipinfo.androfit.sessions.sessionDetail.SessionDetailActivity;
import com.cnamge.fipinfo.androfit.sessions.sessionEdit.SessionEditActivity;

import java.util.List;

public class SessionsFragment extends Fragment implements SessionsInterface {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeController swipeController;
    private SessionsPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sessions_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.sessions_recycler_view);
        presenter = new SessionsPresenter(this, this.getContext());
        prepareRecyclerView();

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
        SessionsAdapter adapter = new SessionsAdapter(items, this.presenter);
        recyclerView.setAdapter(adapter);
        presenter.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void prepareRecyclerView() {
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        SwipeControllerActions sca = new SwipeControllerActions() {
            @Override
            public void onDeleteClicked(int position) {
                presenter.onDeleteButtonClicked(position);
            }

            @Override
            public void onEditClicked(int position) {
                presenter.onEditButtonClicked(position);
            }
        };

        swipeController = new SwipeController(sca, this.getContext());

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @Override
    public void launchDetailActivity(Session session){
        Intent intent = new Intent(this.getActivity(), SessionDetailActivity.class);
        intent.putExtra(getContext().getString(R.string.session_intent_name), session.getId());
        startActivity(intent);
    }

    @Override
    public void goToEditActivity(Session session) {
        Intent intent = new Intent(this.getActivity(), SessionEditActivity.class);
        intent.putExtra(getString(R.string.session_intent_name), session.getId());
        intent.putExtra(getString(R.string.session_intent_edit_context_name), getString(R.string.session_intent_context_edit));
        startActivity(intent);
    }

}
