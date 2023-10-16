package cz.adaptech.tesseract4android.sample;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.ROTATE_90_CLOCKWISE;
import static org.opencv.core.Core.ROTATE_90_COUNTERCLOCKWISE;
import static org.opencv.core.Core.add;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.putText;

import android.graphics.Rect;
import android.util.Log;

import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class FrameProcessingDisplay {

    static Color lightLime = new Color(Color.BasicColor.LIME_LIGHT);
    static Color darkLime = new Color(Color.BasicColor.LIME_DARK);
    static Color pureWhite = new Color(Color.BasicColor.PURE_WHITE);

    static Color debuggingBlue = new Color(Color.BasicColor.BLUE);


    public FrameProcessingDisplay() {
    }

    public static Mat returnProcessedMat(Mat originalMat, ResultIterator iterator){

        Mat textOverlayMat = new Mat(originalMat.size(), originalMat.type());//Same size & same type
        textOverlayMat.setTo(new Scalar(0, 0, 0, 0));//Transparent
        rotate(textOverlayMat, originalMat, ROTATE_90_CLOCKWISE);

//        iterator.begin();
        Rect rect;
        String word;
        StringBuilder result = new StringBuilder();
        rotate(originalMat, originalMat, ROTATE_180);

        do {
            word = iterator.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD);
            rect = iterator.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD);

            // Store or process the word and its location information
            result.append("Word: ").append(word).append(", Location: ").append(rect.toShortString()).append("\n");
//            Place background on word
            Point leftBottom = new Point(rect.left,  rect.bottom);
            Point rightTop   = new Point(rect.right, rect.top);
            Imgproc.rectangle(originalMat, leftBottom, rightTop, lightLime.getScalar((short) 255), -1);

            int x = (int) ((((double)rect.top/originalMat.height()))*originalMat.width());
            int y = (int) ((((double)(originalMat.width()-rect.left)/originalMat.width()))*originalMat.height());

//            Decide on text size
            int guessedWidth = rect.right - rect.left;

            double fontSize = guessedWidth/16D;
            fontSize = fontSize/2.5D;

            putText(textOverlayMat, word, new Point(x,y-5), Imgproc.FONT_HERSHEY_SIMPLEX, fontSize, debuggingBlue.getScalar((short) 255), (int) Math.max(fontSize/10, 1));

            Log.d(Settings.LOG_TAG, "result = "+result);
        } while (iterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD));

        iterator.delete();
        rotate(originalMat, originalMat, ROTATE_180);


        rotate(textOverlayMat, textOverlayMat, ROTATE_90_COUNTERCLOCKWISE);
//        textOverlayMat.copyTo(originalMat, textOverlayMat);
        textOverlayMat.copyTo(originalMat, textOverlayMat);




        // Resize text image to match background image size
        Mat resizedTextImage = new Mat();
        Imgproc.resize(textOverlayMat, resizedTextImage, new Size(originalMat.cols(), originalMat.rows()));

        // Create a mask from the alpha channel of the text image
        Mat alphaChannel = new Mat();
        Core.extractChannel(resizedTextImage, alphaChannel, 3);
        Mat mask = new Mat();
        Imgproc.threshold(alphaChannel, mask, 0, 255, Imgproc.THRESH_BINARY);

        // Copy the text image to the background using the mask
        resizedTextImage.copyTo(originalMat, mask);



        // Sum the images (add the text to the original img)
//        add(originalMat, textOverlayMat, originalMat);


        return originalMat;
    }

}
