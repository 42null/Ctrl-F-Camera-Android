package cz.adaptech.tesseract4android.sample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.opencv.android.JavaCameraView;

public class JavaCameraViewWithDrawing extends JavaCameraView {


    public JavaCameraViewWithDrawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return false;
    }
}
