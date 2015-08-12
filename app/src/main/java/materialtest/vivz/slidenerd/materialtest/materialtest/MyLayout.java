package materialtest.vivz.slidenerd.materialtest.materialtest;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Creado por soft12 el 11/08/2015.
 */
public class MyLayout extends FrameLayout {

    public static final String TAG = "VIVZ";
    Paint paint = null;

    public MyLayout(Context context) {
        super(context);

        init();
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        setWillNotDraw(false);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout dispatchTouchEvent DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout dispatchTouchEvent MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout dispatchTouchEvent UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MyLayout dispatchTouchEvent CANCEL");
                break;
        }
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "MyLayout dispatchTouchEvent RETURNS " + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout onInterceptTouchEvent DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout onInterceptTouchEvent MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout onInterceptTouchEvent UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MyLayout onInterceptTouchEvent CANCEL");
                break;
        }
        boolean b = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "MyLayout onInterceptTouchEvent RETURNS " + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout onTouchEvent DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout onTouchEvent MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout onTouchEvent UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MyLayout onTouchEvent CANCEL");
                break;
        }
        boolean b = super.onTouchEvent(event);
        Log.d(TAG, "MyLayout onTouchEvent RETURNS " + b);
        return b;
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
    }*/
}
