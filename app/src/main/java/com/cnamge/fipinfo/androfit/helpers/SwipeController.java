package com.cnamge.fipinfo.androfit.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cnamge.fipinfo.androfit.R;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;


enum ButtonsState {
    GONE,
    RIGHT_VISIBLE
}

public class SwipeController extends ItemTouchHelper.Callback {

    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 300;
    private RectF deleteButton, editButton;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private SwipeControllerActions buttonsActions;
    private Context context;

    public SwipeController(SwipeControllerActions buttonsActions, Context context) {
        this.buttonsActions = buttonsActions;
        this.context = context;
    }

    // Setup how the ViewHolder can be dragged or swiped
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT);
    }

    // Manage the drag and drop
    // Never called cause we don't use drag n drop in this app
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    // Called when the viewHolder is swiped
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){}

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth*2 + 40);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
            if (swipeBack) {
                if (dX < -buttonWidth*2) buttonShowedState = ButtonsState.RIGHT_VISIBLE;

                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }
            return false;
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if (buttonsActions != null
                    && editButton != null
                    && deleteButton != null
                    && (editButton.contains(event.getX(), event.getY()) || deleteButton.contains(event.getX(), event.getY())))
                {
                    if (deleteButton.contains(event.getX(), event.getY())) {
                        buttonsActions.onDeleteClicked(viewHolder.getAdapterPosition());
                    }
                    else if (editButton.contains(event.getX(), event.getY())) {
                        buttonsActions.onEditClicked(viewHolder.getAdapterPosition());
                    }
                }
                buttonShowedState = ButtonsState.GONE;
                currentItemViewHolder = null;
            }
            return false;
        });
    }
    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    // Drawing functions
    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        View itemView = viewHolder.itemView;

        Paint p = new Paint();

        // Delete Button config
        RectF deleteButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(ContextCompat.getColor(context, R.color.red));
        c.drawRect(deleteButton, p);
        drawText(context.getResources().getString(R.string.delete), c, deleteButton, p);

        // Edit button config
        RectF editButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding*2, itemView.getTop(), itemView.getRight()  - buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(ContextCompat.getColor(context, R.color.blue));
        c.drawRect(editButton, p);
        drawText(context.getResources().getString(R.string.edit), c, editButton, p);

        this.deleteButton = deleteButton;
        this.editButton = editButton;
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 44;
        p.setColor(ContextCompat.getColor(context, R.color.white));
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}