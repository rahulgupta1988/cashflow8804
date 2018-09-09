package cashflow.sixdcash.com.cashflow.Utility;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Praveen on 9/21/2016.
 */
public class MyTouchListener implements View.OnTouchListener {



    /** Swipe min distance. */
    private static final int SWIPE_MIN_DISTANCE = 60;
    /** Swipe max off path. */
    private static final int SWIPE_MAX_OFF_PATH = 250;
    /** Swipe threshold velocity. */
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;


    MyGestureListener myGestureListener=new MyGestureListener();
    GestureDetector gestureDetector=new GestureDetector(myGestureListener);


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        gestureDetector.onTouchEvent(motionEvent);
        Log.d("Touck3425","asdasfd");
        return true;
    }





    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {

                Log.i("e1 X 8980",""+e1.getX());
                Log.i("e2 X 8980",""+e2.getX());
               /* if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH || Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;

                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // right to left swipe
                    rightToLeft();

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // left to right swipe

                } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    // bottom to top

                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    // top to bottom

                }


*/
                if (e1.getX()>e2.getX()) {
                    // left swipe to right
                    rightToLeft();

                }
/*
                else if (e1.getX()>e2.getX()) {
                    // right to left swipe
                    rightToLeft();

                }*/


            } catch (Exception e) {
                // nothing
            }

            return false;
        }

    }

    public  void rightToLeft(){}
}
