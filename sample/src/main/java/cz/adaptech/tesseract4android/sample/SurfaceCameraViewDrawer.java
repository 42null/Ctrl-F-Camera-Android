package cz.adaptech.tesseract4android.sample;//package cz.adaptech.tesseract4android.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class SurfaceCameraViewDrawer extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint;
    private float clickedX = -1;
    private float clickedY = -1;

    ImageView pointerA, pointerB;
    Point[] touchPoints = new Point[]{
        new Point(-1,-1),
        new Point(-1,-1)
    };



    public SurfaceCameraViewDrawer(Context context) {
        super(context);
        init();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public SurfaceCameraViewDrawer(Context context, AttributeSet attrs) {
        super(context);
        init();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public SurfaceCameraViewDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
        init();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public SurfaceCameraViewDrawer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        init();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);

        getHolder().addCallback(this);

//        pointerA = this.findViewById(R.id.pointer1_rename_me);
//        pointerB = this.findViewById(R.id.pointer2_rename_me);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface is created, initialize drawing here if needed
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface size or format has changed, handle it here
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface is destroyed, clean up drawing resources here
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;
        float screenHeight = displayMetrics.heightPixels;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchPoints[0].x = (int) event.getX();
                touchPoints[0].y = (int) event.getY();

                break;

            case MotionEvent.ACTION_MOVE:
//                clearCanvas();
                post(new Runnable() {
                    @Override
                    public void run() {
                        clearCanvas();
                        drawRectangle(touchPoints[0], new Point((int) event.getX(), (int) event.getY()));
                    }
                });
                break;

            case MotionEvent.ACTION_UP:
                touchPoints[1].x = (int) event.getX();
                touchPoints[1].y = (int) event.getY();
                break;
        }

        return true;
    }



    private void drawRectangle(Point point1, Point point2) {
        clearCanvas();
        if (point1.x != -1 && point1.y != -1 && point2.x != -1 && point2.y != -1) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//Document here @@@
                canvas.drawRect(point1.x, point1.y, point2.x, point2.y, paint);

                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    public void clearCanvas() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            // Clears the canvas with a transparent color
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

}