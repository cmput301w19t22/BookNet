package com.example.booknet.Adapters;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceDecoration extends RecyclerView.ItemDecoration {

    private int height;
    private int sides;

    public SpaceDecoration(int height, int sides) {
        this.height = height;
        this.sides = sides;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = height;
        outRect.top = height;
        outRect.left = sides;
        outRect.right = sides;
    }
}
