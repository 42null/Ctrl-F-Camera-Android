package cz.adaptech.tesseract4android.sample.helpers;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class OCR {

    public static Mat preprocess(Mat rgba){
        Mat gray = new Mat();
        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGBA2GRAY);

        // Applying blur
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

//         Adaptive thresholding
        Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 31, 2);

        return gray;
    }

}
