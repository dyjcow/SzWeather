package com.dyj.szweather.module.citymanager.adapter;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dyj.szweather.R;
import com.dyj.szweather.util.MyUtil;


public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchMoveListener moveListener;

    /**
     * 构造方法
     * @param moveListener
     */
    public MyItemTouchHelperCallback(ItemTouchMoveListener moveListener){
        this.moveListener = moveListener;
    }

    /**
     * 用来判断当前是什么动作，例如方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
            viewHolder) {
        //要监听的拖拽方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //要监听的侧滑方向
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int flags = makeMovementFlags(dragFlags, swipeFlags);
        return flags;
    }

    /**
     * 是否打开长按拖拽
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 上下移动时回调的方法
     * 返回ture表示item已经移动到新的位置
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
            viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if(viewHolder.getItemViewType() != target.getItemViewType()){
            return false;
        }
        //用onItemMove判断是否滑动并将结果返回给result
        boolean result = moveListener.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return result;
    }

    /**
     * 侧滑时回调的方法
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        moveListener.onItemRemove(viewHolder.getAdapterPosition());
    }

    /**
     * 当 item 由静止状态变为滑动或拖动状态时调用此方法，可通过 actionState 判断 Item 在哪种状态下执行某些操作
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            //为item设置背景颜色
            viewHolder.itemView.setBackgroundColor(MyUtil.ViewGetColor(viewHolder.itemView.getContext(),R.color.gray));
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 与用户交互结束后调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
            viewHolder) {
        //恢复未选中之前的背景颜色
        viewHolder.itemView.setBackgroundColor(MyUtil.ViewGetColor(viewHolder.itemView.getContext(),R.color.white));
        //设置不透明度
        viewHolder.itemView.setAlpha(1);//1-0
        //表示不应用缩放
//        viewHolder.itemView.setScaleX(1);
//        viewHolder.itemView.setScaleY(1);
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        //dX:水平方向移动的增量(负：往左；正：往右) 0-view.getWidth()
        float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            //设置透明度
            viewHolder.itemView.setAlpha(alpha);//1-0

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
