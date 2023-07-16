package cz.adaptech.tesseract4android.sample;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.ROTATE_90_CLOCKWISE;
import static org.opencv.core.Core.ROTATE_90_COUNTERCLOCKWISE;
import static org.opencv.core.Core.add;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;
import static org.opencv.imgproc.Imgproc.LINE_AA;
import static org.opencv.imgproc.Imgproc.putText;
import static cz.adaptech.tesseract4android.sample.MainActivity.LOG_TAG;

import android.util.Log;

import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FrameProcessingDisplay {

    static Color lightLime = new Color(Color.BasicColor.LIME_LIGHT);
    static Color darkLime = new Color(Color.BasicColor.LIME_DARK);


    public FrameProcessingDisplay() {
    }

    public static Mat returnProcessedMat(Mat originalMat, ResultIterator iterator){

//        iterator.begin();
        android.graphics.Rect rect;
        StringBuilder result = new StringBuilder();
        rotate(originalMat, originalMat, ROTATE_180);

        do {
            String word = iterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD);
            rect = iterator.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD);


            // Store or process the word and its location information
            result.append("Word: ").append(word).append(", Location: ").append(rect.toShortString()).append("\n");
//            Place background on word
            Point leftBottom = new Point(rect.left,  rect.bottom);
            Point rightTop   = new Point(rect.right, rect.top);
            Imgproc.rectangle(originalMat, leftBottom, rightTop, lightLime.getScalar((short) 255), 2);

//            Mat rotated = new Mat();
//            putText(originalMat, word, rightTop, Imgproc.FONT_HERSHEY_PLAIN, 1, darkLime.getScalar((short) 255), 2);
//            putText(originalMat, word, leftBottom, Imgproc.FONT_HERSHEY_PLAIN, 1, darkLime.getScalar((short) 255), 2);

            rotate(originalMat, originalMat, ROTATE_90_COUNTERCLOCKWISE);
            int x = (int) ((((double)(originalMat.width()-rect.left)/originalMat.width()))*originalMat.height());
            int y = (int) ((((double)rect.top/originalMat.height()))*originalMat.width());
            Log.d(LOG_TAG, "x = "+x);
            Log.d(LOG_TAG, "y = "+y);

//            putText(originalMat, word, new Point( y,x ), Imgproc.FONT_HERSHEY_PLAIN, 1, new Scalar(255,0,255,255), 2);
            putText(originalMat, word, new Point(y,x), Imgproc.FONT_HERSHEY_PLAIN, 1, darkLime.getScalar((short) 255), 2);

            rotate(originalMat, originalMat, ROTATE_90_CLOCKWISE);



            Log.d(LOG_TAG, "result = "+result);
        } while (iterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD));

        iterator.delete();
        rotate(originalMat, originalMat, ROTATE_180);


//        Mat textImg = new Mat(new Size(originalMat.cols(), originalMat.rows()), CvType.CV_8UC3, Scalar.all(0));
//        Imgproc.putText(textImg, "stackoverflow", new Point(0, originalMat.cols() / 2), Imgproc.FONT_HERSHEY_SIMPLEX, 2.0, new Scalar(20, 20, 20), 2);
//        Core.rotate(textImg, textImg, Core.ROTATE_90_COUNTERCLOCKWISE);

        // Sum the images (add the text to the original img)
//        add(originalMat, textImg, originalMat);


        return originalMat;
    }

}
