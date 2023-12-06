package cz.adaptech.tesseract4android.sample;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.ROTATE_90_CLOCKWISE;
import static org.opencv.core.Core.ROTATE_90_COUNTERCLOCKWISE;
import static org.opencv.core.Core.add;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.putText;

import android.graphics.Color;
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

    static ColorController lightLime = new ColorController(ColorController.BasicColor.LIME_LIGHT);
    static ColorController darkLime = new ColorController(ColorController.BasicColor.LIME_DARK);
    static ColorController pureWhite = new ColorController(ColorController.BasicColor.PURE_WHITE);

    static ColorController debuggingBlue = new ColorController(ColorController.BasicColor.BLUE);


    public FrameProcessingDisplay() {
    }

    public static Mat returnProcessedMat(Mat originalMat, ResultIterator iterator, WordChecker wordChecker){
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
            ColorController boxColor, textColor;

            // Store or process the word and its location information
            result.append("Word: ").append(word).append(", Location: ").append(rect.toShortString()).append("\n");
//            Place background on word
            Point leftBottom = new Point(rect.left,  rect.bottom);
            Point rightTop   = new Point(rect.right, rect.top);

            if(wordChecker.checkWord(word)){
                boxColor = debuggingBlue;
                textColor = lightLime;
            }else{
                boxColor = lightLime;
                textColor = debuggingBlue;
            }

            int x = (int) ((((double)rect.top/originalMat.height()))*originalMat.width());
            int y = (int) ((((double)(originalMat.width()-rect.left)/originalMat.width()))*originalMat.height());

//            Decide on text size
//            int guessedWidth = rect.right - rect.left;

//            double fontSize = guessedWidth/16D;
//            double fontSize = guessedWidth;
            double fontSize = 0.2;
//            fontSize = fontSize/2.5D;

            double targetWidth = (rect.right - rect.left);//*0.9;

//            while((Imgproc.getTextSize(word, Imgproc.FONT_HERSHEY_SIMPLEX, fontSize, (int) Math.round(fontSize*0.1), null)).width <= targetWidth ){
//                fontSize *= 1.1;
//            }

            // Initial font size
            fontSize = 10.0;

// Adjust the loop condition to ensure the font size fits the width of the rectangle
            while ((Imgproc.getTextSize(word, Imgproc.FONT_HERSHEY_SIMPLEX, fontSize, (int) Math.ceil(fontSize / 10), null)).width > (rect.bottom - rect.top)) {
                fontSize -= 0.2; // Decrease font size
            }
//            fontSize -= 0.2;

//            Apply to mat
            Imgproc.rectangle(originalMat, leftBottom, rightTop, boxColor.getScalar((short) 255), -1);
            putText(textOverlayMat, word, new Point(x,y-5), Imgproc.FONT_HERSHEY_SIMPLEX, fontSize, textColor.getScalar((short) 255), (int) Math.round(fontSize*0.1));

        } while (iterator.next(TessBaseAPI.PageIteratorLevel.RIL_WORD));

        Log.d(Settings.LOG_TAG, "result = "+result);

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
