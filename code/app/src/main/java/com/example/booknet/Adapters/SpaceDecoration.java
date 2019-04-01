package com.example.booknet.Adapters;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * A decoration object for a RecyclerView that makes padding on all sides of the list item.
 *
 * @author Jamie
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {

    private int height;
    private int sides;

    /**
     * Create a SpaceDecoration
     *
     * @param height The space added to both top and bottom
     *               (The space between items will be twice this!)
     * @param sides  The space added on each left/right side
     */
    public SpaceDecoration(int height, int sides) {
        this.height = height;
        this.sides = sides;
    }

    /**
     * Sets the offset of the item view, so that thte item has padding.
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = height;
        outRect.top = height;
        outRect.left = sides;
        outRect.right = sides;
    }
}
