package let.it.be.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Plan was to do a simple touch to click check and pass view with it's item position in RecycleView
 * But somehow it became a bit complicated. Next time should use GestureDetector or set listener in
 * RecycleView custom adapter instead...
 *
 * ... but it was fun =)
 * */
public class RecycleViewItemClickedListener implements RecyclerView.OnItemTouchListener {

    private int maxClickMoveError;
    private int maxClickPressTime;
    private OnItemClickListener mListener;
    private MotionEvent clickEvent;
    private boolean isWithinClickDistanceBounds;
    private boolean isWithinClickTimeBounds;

    public RecycleViewItemClickedListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        maxClickMoveError = ViewConfiguration.get(context).getScaledTouchSlop();
        maxClickPressTime = ViewConfiguration.getLongPressTimeout();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {

        final int action = e.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startAClick(e);
                break;
            case MotionEvent.ACTION_UP:
                if (clickEvent != null && mListener != null) {
                    View childView = view.findChildViewUnder(e.getX(), e.getY());
                    int itemPosition = view.getChildAdapterPosition(childView);
                    if (itemPosition < 0 || childView == null) break;
                    mListener.onItemClick(childView, itemPosition);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                checkIfStillAClick(e);
                break;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private void startAClick(MotionEvent e) {
        clickEvent = e;
        isWithinClickDistanceBounds = true;
        isWithinClickTimeBounds = true;
    }

    private boolean checkIfStillAClick(MotionEvent e) {
        if (clickEvent == null) return false;
        if (!isWithinClickDistanceBounds || !isWithinClickTimeBounds) return false;

        final int deltaX = (int) (clickEvent.getX() - e.getX());
        final int deltaY = (int) (clickEvent.getY() - e.getY());
        int distance = (deltaX * deltaX) + (deltaY * deltaY);
        isWithinClickDistanceBounds = maxClickMoveError >= distance;
        long timeDelta = e.getEventTime() - e.getDownTime();
        isWithinClickTimeBounds = timeDelta <= maxClickPressTime;

        if (!isWithinClickDistanceBounds || !isWithinClickTimeBounds) {
            clickEvent = null;
        }

        return clickEvent != null;
    }
}
