package dimfcompany.com.salesideas.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyDivider extends DividerItemDecoration
{
    private Drawable mDivider;

    public MyDivider(Context context, int orientation)
    {
        super(context, orientation);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int position = parent.getChildAdapterPosition(view);
        if (position == parent.getAdapter().getItemCount() - 1)
        {
            outRect.setEmpty();
        } else
            {
                super.getItemOffsets(outRect, view, parent, state);
            }
    }

//    @Override
//    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state)
//    {
//        int dividerLeft = parent.getPaddingLeft();
//        int dividerRight = parent.getWidth() - parent.getPaddingRight();
//
//        int childCount = parent.getChildCount();
//        for (int i = 0; i <= childCount - 2; i++)
//        {
//            View child = parent.getChildAt(i);
//
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//            int dividerTop = 1;
//            int dividerBottom = 1;
//
//            mDivider.setBounds(10, 10, 10, 10);
//            mDivider.draw(canvas);
//        }
//    }
}
