package com.example.navdrawertest10;

import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AdminInventoryTouchHelper extends ItemTouchHelper.SimpleCallback {

    private MyAdapter_Admin_Inventory_Items adapter;
    public AdminInventoryTouchHelper(MyAdapter_Admin_Inventory_Items adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            adapter.updateData(position);
            adapter.notifyDataSetChanged();
        }else{
            adapter.deleteData(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeRightActionIcon(R.drawable.ic_delete)
                .setSwipeRightActionIconTint(Color.WHITE)
                .addSwipeRightLabel("Delete Item")
                .setSwipeRightLabelColor(Color.WHITE)
                .addSwipeLeftBackgroundColor(R.color.purple_500)
                .addSwipeLeftActionIcon(R.drawable.ic_reset_pass)
                .setSwipeLeftActionIconTint(Color.WHITE)
                .addSwipeLeftLabel("Update Item")
                .setSwipeLeftLabelColor(Color.WHITE)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
