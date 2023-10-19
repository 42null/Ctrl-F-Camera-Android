package cz.adaptech.tesseract4android.sample;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import org.opencv.core.Mat;

import java.util.Locale;

import cz.adaptech.tesseract4android.sample.helpers.CustomBitmapConverters;

public class SeparateLiveResultObtainer {

    private boolean running = false;

    private Mat originalMat;
    private Mat LatestFrame;


    public SeparateLiveResultObtainer(){

    }

    public void startNew(){
        running = true;
    }

//    public void getLatest(){
//    }
//    keepFrame = FrameProcessingDisplay.returnProcessedMat(originalMat, lastResultIterator);


//    public void recognizeImage2(@NonNull Mat mat, @NonNull Mat originalMat0) {
//        keepFrame = originalMat0;
//
////        result = "";
//        result.postValue("");
////        processing.setValue(true);
////        progress.setValue("Processing...");
//        stopped = false;
//
//        // Start process in another thread
//        new Thread(() -> {
//            Mat originalMat = originalMat0.clone();
//
////            mTess.setImage(imagePath);
//            // Or set it as Bitmap, Pix,...
//            mTess.setImage(CustomBitmapConverters.matToBitmap(mat));
//
//            long startTime = SystemClock.uptimeMillis();
//
//            // Use getHOCRText(0) method to trigger recognition with progress notifications and
//            // ability to cancel ongoing processing.
//            mTess.getHOCRText(0);
//
//            // At this point the recognition has completed (or was interrupted by calling stop())
//            // and we can get the results we want. In this case just normal UTF8 text.
//            //
//            // Note that calling only this method (without the getHOCRText() above) would also
//            // trigger the recognition and return the same result, but we would received no progress
//            // notifications and we wouldn't be able to stop() the ongoing recognition.
//            String text = mTess.getUTF8Text();
//            result.postValue(text);
//            Log.d("Settings.TAG", "Result: "+result.getValue());
//
//            lastResultIterator = mTess.getResultIterator();
//
//            updateElementText(startButton,"Searching for text...");//TODO: Make grab text from settings
//            keepFrame = FrameProcessingDisplay.returnProcessedMat(originalMat, lastResultIterator);
//            updateElementText(startButton,"Take new picture");//TODO: Make grab text from settings
//
////            updateElementText(findViewById(R.id.start),"Back to camera view");//TODO: Make grab text from settings
//
//
//
//            // We can free up the recognition results and any stored image data in the tessApi
//            // if we don't need them anymore.
//            mTess.clear();
//
////             Publish the results
//            processing.postValue(false);
//            if (stopped) {
//                progress.postValue("Stopped.");
//            } else {
//                long duration = SystemClock.uptimeMillis() - startTime;
//                progress.postValue(String.format(Locale.ENGLISH,
//                        "Completed in %.3fs.", (duration / 1000f)));
//            }
//        }).start();
//    }

    //    Starts pulling text if not started, run in another thread and take care of it.
    private void startIfNotPullingText(){

    }
}
