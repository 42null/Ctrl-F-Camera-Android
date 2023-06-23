package cz.adaptech.tesseract4android.sample;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.resize;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Locale;

import cz.adaptech.tesseract4android.sample.ui.main.MainViewModel;

//public class MainActivity extends AppCompatActivity {


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        if (!OpenCVLoader.initDebug()) {
//            Log.d(TAG, "Main OpenCV not loaded");
//        } else {
//            Log.d(TAG, "Main OpenCV loaded");
//        }
//        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow();
//        }
//    }
//}
public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2, ViewModelStoreOwner {
    private static final String TAG = "LOG_TAG";

    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private CameraBridgeViewBase mOpenCvCameraView;

    private TessBaseAPI mTess;

    public static boolean buttonClick = false;

//    private final MutableLiveData<String> result = new MutableLiveData<>();
    private static String result = "NOT YET SET";
    private final MutableLiveData<Boolean> processing = new MutableLiveData<>(false);
    private ViewModelStore viewModelStore = new ViewModelStore();
    private MainViewModel viewModel;
    private final MutableLiveData<String> progress = new MutableLiveData<>();
    private boolean stopped;


    private long mFrameCounter = 0;
    private long mStartTime = 0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");

                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV initialized");
        }



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Permissions for Android 6+
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
        );

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = findViewById(R.id.main_surface);
//        mOpenCvCameraView = findViewById(R.id.image);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        Button button = findViewById(R.id.start);
        button.setOnClickListener(v -> {
            Log.d(TAG, "Button Pressed original");
            buttonClick = true;
        });



//        String dataPath = new File(getBaseContext().getFilesDir(), "tesseract").getAbsolutePath();

//         Initialize API for specified language (can be called multiple times during Tesseract lifetime)
//        if (!mTess.init(dataPath, "eng")) {
//            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file)
//            mTess.recycle();
//            Log.d(TAG, "onCreate: Failed & recycled");
//            return;
//        }
//        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789a
//        bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);


        // Copy sample image and language data to storage
//        Assets.extractAssets(getBaseContext());
//
//            String dataPath = Assets.getTessDataPath(requireContext());
//            String language = Assets.getLanguage();
//            viewModel.initTesseract(dataPath, language, TessBaseAPI.OEM_LSTM_ONLY);

        mTess = new TessBaseAPI();
        Assets.extractAssets(getBaseContext());

        String dataPath = Assets.getTessDataPath(getBaseContext());
        String language = Assets.getLanguage();

        if (!mTess.init(dataPath, "eng")) {
            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file)
            mTess.recycle();
            Log.d(TAG, "if (!mTess.init(dataPath, \"eng\")) {");
            return;
        }
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO); // Set page segmentation mode
//        mTess.init(DATA_PATH, LANGUAGE);
//        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mOpenCvCameraView.setCameraPermissionGranted();
            } else {
                String message = "Camera permission was not granted";
                Log.e(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(TAG, "Unexpected permission request");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelStore.clear();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mFrameCounter = 0;
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onCameraViewStopped() {
    }

//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
////        return frame.gray();
////    }
////        Log.d("LOG_TAG", "Button Pressed2");
//
//        // Convert the frame to a Mat object
//        Mat rgba = frame.rgba();
//
//        // Preprocess the image using OpenCV operations
//        Mat gray = new Mat();
//        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGBA2GRAY);
//        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0);
//        Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
//
//        Bitmap bitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(gray, bitmap);
//        mFrameCounter++;
//        if(buttonClick || mFrameCounter == 500){
//            Log.d(TAG, "A");
//            buttonClick = false;
//            System.out.println("BUTTON CLICK!!!!");
//            Log.d(TAG, "Starting...");
//            mTess.setImage(bitmap);
//            String recognizedText = mTess.getUTF8Text();
//            System.out.println("BUTTON CLICK!!!!+"+recognizedText);
//            Log.d(TAG, "recognizedText..."+recognizedText);
//
////            mTess.recycle();
//            mTess.clear();
//        }
//
//        return gray;
//    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        // Process the camera frame here
        Mat rgba = frame.rgba();

        // Perform text detection
        // Assuming you want to process the entire frame, you can adjust the region of interest (ROI) accordingly
        Rect roi = new Rect(0, 0, rgba.cols(), rgba.rows());
        Mat cropped = new Mat(rgba, roi);


//         resize(cropped, cropped, fx=1.2, fy=1.2, interpolation=cv2.INTER_CUBIC)

        Mat gray = new Mat();
        Imgproc.cvtColor(cropped, gray, Imgproc.COLOR_RGBA2GRAY);

        // Applying dilation and erosion
//        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(0.5, 0.5));
//        Imgproc.dilate(gray, gray, kernel);
//        Imgproc.erode(gray, gray, kernel);

        // Applying blur
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

////         Alternatively, you can use the bilateral filter or median blur
//         Imgproc.bilateralFilter(gray, gray,  5, 75, 75);
//         Imgproc.medianBlur(gray, gray, 3);

//         Adaptive thresholding
        Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 31, 2);

//         Save the preprocessed image
//        Imgcodecs.imwrite("path/to/save/preprocessed_image.jpg", gray);





        if(buttonClick){
            buttonClick = false;
//            Log.d("LOG_TAG","re2c="+getResult());

            Mat rotated = new Mat();
            rotate(gray, rotated, ROTATE_180);
            Bitmap outputBitmap = Bitmap.createBitmap(rotated.cols(), rotated.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rotated, outputBitmap);

            recognizeImage2(outputBitmap);

            // Convert the cropped frame to grayscale for better OCR performance


            // Perform thresholding or other preprocessing if needed

//            // Pass the preprocessed frame to Tesseract for text recognition
//            Bitmap bitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(gray, bitmap);
//            mTess.setImage(bitmap);
//
//            String recognizedText = mTess.getUTF8Text();
//            // Do something with the recognized text, such as logging or displaying it
            Log.d("LOG_TAG","re3c="+getResult());

        }


        return gray;
    }

    private final Object recycleLock = new Object();
    public void recognizeImage2(@NonNull Bitmap imageBitmap) {

        result = "";
//        processing.setValue(true);
//        progress.setValue("Processing...");
        stopped = false;

        // Start process in another thread
        new Thread(() -> {
//            mTess.setImage(imagePath);
            // Or set it as Bitmap, Pix,...
            mTess.setImage(imageBitmap);

            long startTime = SystemClock.uptimeMillis();

            // Use getHOCRText(0) method to trigger recognition with progress notifications and
            // ability to cancel ongoing processing.
            mTess.getHOCRText(0);

            // At this point the recognition has completed (or was interrupted by calling stop())
            // and we can get the results we want. In this case just normal UTF8 text.
            //
            // Note that calling only this method (without the getHOCRText() above) would also
            // trigger the recognition and return the same result, but we would received no progress
            // notifications and we wouldn't be able to stop() the ongoing recognition.
            String text = mTess.getUTF8Text();
            result = text;
            Log.d("LOG_TAG", "result"+result);

            // We can free up the recognition results and any stored image data in the tessApi
            // if we don't need them anymore.
            mTess.clear();

            // Publish the results
//            processing.postValue(false);
//            if (stopped) {
//                progress.postValue("Stopped.");
//            } else {
//                long duration = SystemClock.uptimeMillis() - startTime;
//                progress.postValue(String.format(Locale.ENGLISH,
//                        "Completed in %.3fs.", (duration / 1000f)));
//            }
        }).start();
    }
    @NonNull
    public String getResult() {
        return result;
    }







    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    public void recognizeImage(Mat mat) {

//        result.setValue("");
//        processing.setValue(true);
//        progress.setValue("Processing...");
        stopped = false;

        // Start process in another thread
        new Thread(() -> {
            Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, bitmap);

            mTess.setImage(bitmap);
            // Or set it as Bitmap, Pix,...
            // tessApi.setImage(imageBitmap);

//            long startTime = SystemClock.uptimeMillis();
//
//            // Use getHOCRText(0) method to trigger recognition with progress notifications and
//            // ability to cancel ongoing processing.
//            mTess.getHOCRText(0);
//
//            // Then get just normal UTF8 text as result. Using only this method would also trigger
//            // recognition, but would just block until it is completed.
            String text = mTess.getUTF8Text();
//
////            result.postValue(text);
//            processing.postValue(false);
//            if (stopped) {
//                progress.postValue("Stopped.");
//            } else {
//                long duration = SystemClock.uptimeMillis() - startTime;
//                progress.postValue(String.format(Locale.ENGLISH,
//                        "Completed in %.3fs.", (duration / 1000f)));
//            }

//            Log.d(TAG, "recognizeImage: result= "+result);
            Log.d(TAG, "MAT TEXT = "+text);
            // Assuming you have a bitmap named "bitmap" and a file name "filename"
            Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bmp32, mat);
            Imgcodecs.imwrite("filename.png", mat);

        }).start();
    }

}