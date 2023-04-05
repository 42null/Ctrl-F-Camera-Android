package cz.adaptech.tesseract4android.sample;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
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

//public class MainActivity extends AppCompatActivity {


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        if (!OpenCVLoader.initDebug()) {
//            Log.d(LOG_TAG, "Main OpenCV not loaded");
//        } else {
//            Log.d(LOG_TAG, "Main OpenCV loaded");
//        }
//        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow();
//        }
//    }
//}
public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String LOG_TAG = "LOG_TAG";

    private static final int CAMERA_PERMISSION_REQUEST = 1;

    private CameraBridgeViewBase mOpenCvCameraView;

    private TessBaseAPI mTess;

    int tmpFrameLazySpacing = 0;

    private long mFrameCounter = 0;
    private long mStartTime = 0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(LOG_TAG, "OpenCV loaded successfully");

                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "called onCreate");
        super.onCreate(savedInstanceState);
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

        mTess = new TessBaseAPI();
//        String dataPath = new File(getBaseContext().getFilesDir(), "tesseract").getAbsolutePath();

//         Initialize API for specified language (can be called multiple times during Tesseract lifetime)
//        if (!mTess.init(dataPath, "eng")) {
//            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file)
//            mTess.recycle();
//            Log.d(LOG_TAG, "onCreate: Failed & recycled");
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
            return;
        }
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
                Log.e(LOG_TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(LOG_TAG, "Unexpected permission request");
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
            Log.d(LOG_TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(LOG_TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
//        // get current camera frame as OpenCV Mat object
//        Mat mat = frame.gray();
//        return mat;
//    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        mFrameCounter++;
        // get current camera frame as OpenCV Mat object
        Mat mat = frame.gray();

        // convert Mat to Bitmap
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);

        // extract text from bitmap using Tesseract
        if(tmpFrameLazySpacing % 20 == 0){
            long currentTime = System.currentTimeMillis();
            double fps = mFrameCounter / ((currentTime - mStartTime) / 1000.0);
            mTess.setImage(bitmap);
            String text = mTess.getUTF8Text();

            // log text to console
            Log.d(LOG_TAG, "Detected text ("+fps+" fps): " + text);
        }else{
        }
        tmpFrameLazySpacing++;
        return mat;
    }

}