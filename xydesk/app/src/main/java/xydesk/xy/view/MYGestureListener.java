package xydesk.xy.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import xydesk.xy.i.GridViewScroolI;

/**
 * Created by Administrator on 2016/12/28.
 */

public class MYGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

    private GestureDetector gDetector;
    private GridViewScroolI gridViewScroolI;

    public MYGestureListener(Context con, GestureDetector gDetector, GridViewScroolI gridViewScroolI) {
        if (null == gDetector) {
            gDetector = new GestureDetector(con, this);
        }
        this.gridViewScroolI = gridViewScroolI;
        this.gDetector = gDetector;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        //TODO viewFlipper.showNext()...whatever you want
        if (gridViewScroolI != null) {
            gridViewScroolI.scrool(e1, e2, velocityX, velocityY);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gDetector.onTouchEvent(event);
    }
}
