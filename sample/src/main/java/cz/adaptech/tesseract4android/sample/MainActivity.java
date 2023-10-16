package cz.adaptech.tesseract4android.sample;

import static android.graphics.Color.parseColor;
import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.resize;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Locale;
import java.util.Set;

import cz.adaptech.tesseract4android.sample.helpers.Converters;
import cz.adaptech.tesseract4android.sample.helpers.OCR;
import cz.adaptech.tesseract4android.sample.ui.main.MainFragment;
import cz.adaptech.tesseract4android.sample.ui.main.MainViewModel;

public class MainActivity extends AppCompatActivity {

    MyCameraActivity myCameraActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!OpenCVLoader.initDebug()) {
            Log.d(Settings.LOG_TAG, "Main OpenCV not loaded");
        } else {
            Log.d(Settings.LOG_TAG, "Main OpenCV loaded");
        }
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow();
//        }

        Intent cameraIntent = new Intent(this, MyCameraActivity.class);
        startActivity(cameraIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Call methods from CameraActivity as needed
        myCameraActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}






























//public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2, ViewModelStoreOwner {
//
//    private static final int CAMERA_PERMISSION_REQUEST = 1;
//
////    Page Elements
//    private CameraBridgeViewBase mOpenCvCameraView;
//    MaterialCheckBox materialCheckBox;
//
//
//    private TessBaseAPI mTess;
//
//    private ResultIterator lastResultIterator = null;
//
//    public static boolean buttonClick = false;
//    public static Mat keepFrame = null;
//
//    private final MutableLiveData<String> result = new MutableLiveData<>();
////    private static String result = "NOT YET SET";
//    private final MutableLiveData<Boolean> processing = new MutableLiveData<>(false);
//    private ViewModelStore viewModelStore = new ViewModelStore();
//    private MainViewModel viewModel;
//    private final MutableLiveData<String> progress = new MutableLiveData<>();
//    private boolean stopped;
//
//
//    private long mFrameCounter = 0;
//    private long mStartTime = 0;
//
//    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            if (status == LoaderCallbackInterface.SUCCESS) {
//                Log.i(Settings.LOG_TAG, "OpenCV loaded successfully");
//
//                mOpenCvCameraView.enableView();
//            } else {
//                super.onManagerConnected(status);
//            }
//        }
//    };
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.i(Settings.LOG_TAG, "called onCreate");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        MyCameraActivity myCameraActivity = new MyCameraActivity();
//
//
//        if(OpenCVLoader.initDebug()) {
//            Log.d(Settings.LOG_TAG, "OpenCV initialized");
//        }
//
//
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        // Permissions for Android 6+
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.CAMERA},
//                CAMERA_PERMISSION_REQUEST
//        );
//
//        setContentView(R.layout.activity_main);
//
//        mOpenCvCameraView = findViewById(R.id.main_surface);
////        mOpenCvCameraView = findViewById(R.id.image);
//
//        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
//
//        mOpenCvCameraView.setCvCameraViewListener(this);
//
//        Button button = findViewById(R.id.start);
//        button.setOnClickListener(v -> {
//            Log.d(Settings.LOG_TAG, "Button Pressed original");
//
//            if(keepFrame == null){
//                buttonClick = true;
//            }else{
//                keepFrame = null;
//                updateElementText( findViewById(R.id.start),"Scan For Text");
//            }
//        });
//
//
//
////        String dataPath = new File(getBaseContext().getFilesDir(), "tesseract").getAbsolutePath();
//
////         Initialize API for specified language (can be called multiple times during Tesseract lifetime)
////        if (!mTess.init(dataPath, "eng")) {
////            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file)
////            mTess.recycle();
////            Log.d(TAG, "onCreate: Failed & recycled");
////            return;
////        }
////        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789a
////        bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
////        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
//
//
//        // Copy sample image and language data to storage
////        Assets.extractAssets(getBaseContext());
////
////            String dataPath = Assets.getTessDataPath(requireContext());
////            String language = Assets.getLanguage();
////            viewModel.initTesseract(dataPath, language, TessBaseAPI.OEM_LSTM_ONLY);
//
//        mTess = new TessBaseAPI();
//        Assets.extractAssets(getBaseContext());
//
//        String dataPath = Assets.getTessDataPath(getBaseContext());
//        String language = Assets.getLanguage();
//
//        if (!mTess.init(dataPath, "eng")) {
//            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file)
//            mTess.recycle();
//            Log.d(Settings.LOG_TAG, "if (!mTess.init(dataPath, \"eng\")) {");
//            return;
//        }
//        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO); // Set page segmentation mode
//
//
//        materialCheckBox = (MaterialCheckBox) findViewById(R.id.selectorOptionCheckbox1);
//        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(Settings.LOG_TAG, "!!!isChecked = "+isChecked);
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
//        if (requestCode == CAMERA_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mOpenCvCameraView.setCameraPermissionGranted();
//            } else {
//                String message = "Camera permission was not granted";
//                Log.e(Settings.LOG_TAG, message);
//                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Log.e(Settings.LOG_TAG, "Unexpected permission request");
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!OpenCVLoader.initDebug()) {
//            Log.d(Settings.LOG_TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
//        } else {
//            Log.d(Settings.LOG_TAG, "OpenCV library found inside package. Using it!");
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        viewModelStore.clear();
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView.disableView();
//    }
//
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//        mFrameCounter = 0;
//        mStartTime = System.currentTimeMillis();
//    }
//
//    @Override
//    public void onCameraViewStopped() {
//    }
//
////    Scalar rectangleColor = getColor(R.color.lime_harmonized_container) Scalar(255,255,100,255);
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
//        if(keepFrame != null){
//            return keepFrame;
//        }
//        Mat rgba = frame.rgba();
//
//        if(buttonClick){
//            buttonClick = false;
//
//            Mat preprocessGray = OCR.preprocess(rgba);
//
////            Mat rotated = new Mat();
//            rotate(preprocessGray, preprocessGray, ROTATE_180);
//
//
//            recognizeImage2(preprocessGray, rgba);
//            return keepFrame;
//        }
//
//        return OCR.preprocess(rgba);//rgba;
//    }
//
//
//
//
//
//
//
//
//    private final Object recycleLock = new Object();
//    public void recognizeImage2(@NonNull Mat mat, @NonNull Mat originalMat0) {
//    keepFrame = originalMat0;
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
//            mTess.setImage(Converters.matToBitmap(mat));
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
//            updateElementText( findViewById(R.id.start),"Searching for text...");//TODO: Make grab text from settings
//            keepFrame = FrameProcessingDisplay.returnProcessedMat(originalMat, lastResultIterator);
//            updateElementText( findViewById(R.id.start),"Take new picture");//TODO: Make grab text from settings
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
//
//
//
//        }).start();
//    }
//
//    public void updateElementText(View view, String newText){
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ((TextView) view).setText(newText);
//            }
//        });
//    }
//
//
//
//    @NonNull
//    public String getResult() {
//        return result.getValue();
//    }
//
//    @NonNull
//    public ResultIterator getResultIterator() {
//        return lastResultIterator;
//    }
//
//    @Override
//    public ViewModelStore getViewModelStore() {
//        return viewModelStore;
//    }
//
//}