package cz.adaptech.tesseract4android.sample;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.rotate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Locale;

import cz.adaptech.tesseract4android.sample.helpers.CustomBitmapConverters;
import cz.adaptech.tesseract4android.sample.helpers.OCR;

public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2, ViewModelStoreOwner {
//public class MyCameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2, ViewModelStoreOwner {

//    CONSTANTS

//    PAGE ELEMENTS
    private CameraBridgeViewBase mOpenCvCameraView;
    private SurfaceView canvasView;
    View startButton;
    ProgressBar textSearchingProgressBar;


    private MyViewModel viewModel;

    private TessBaseAPI mTess;

    private ResultIterator lastResultIterator = null;

//    private SeparateLiveResultObtainer separateLiveResultObtainer = new SeparateLiveResultObtainer();



    public static boolean buttonClick = false;
    public static Mat keepFrame = null;

    private final MutableLiveData<String> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> processing = new MutableLiveData<>(false);
    private ViewModelStore viewModelStore = new ViewModelStore();
    private final MutableLiveData<String> progress = new MutableLiveData<>();
    private boolean stopped;

    private long mFrameCounter = 0;
    private long mStartTime = 0;


//    Pointer selecting events
    int[] startHoldLocation = new int[2];



    private boolean showingPreprocess = Settings.STARTING_SETTING_SHOW_PREPROCESSING;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(Settings.LOG_TAG, "OpenCV loaded successfully");

                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };


    public CameraFragment(Context context) {
        super(R.layout.camera_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(Settings.LOG_TAG, "called onCreateView");
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        mOpenCvCameraView = view.findViewById(R.id.main_camera_surface);
//        canvasView = view.findViewById(R.id.camera_view_overlay_canvas);

        Button button = view.findViewById(R.id.start);
        button.setOnClickListener(v -> {
            Log.d(Settings.LOG_TAG, "Button Pressed original");

            if(keepFrame == null){
                buttonClick = true;
            }else{
                keepFrame = null;
                updateElementText( view.findViewById(R.id.start),getContext().getString(R.string.scan_picture));
            }
        });


        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
        Log.d(Settings.LOG_TAG, "mOpenCvCameraView = "+mOpenCvCameraView);
        mOpenCvCameraView.setCameraPermissionGranted();


        startButton = view.findViewById(R.id.start);
        textSearchingProgressBar = view.findViewById(R.id.textSearchingProgressBar);


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(Settings.LOG_TAG, "called onCreate in ExampleFragment2");
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        // Observe the LiveData and react to changes in the boolean value
        viewModel.getBooleanCheckboxShowPreProcessing().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newValue) {
                showingPreprocess = !showingPreprocess;
            }
        });

        if(OpenCVLoader.initDebug()) {
            Log.d(Settings.LOG_TAG, "OpenCV initialized");
        }
//
//
////        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////        mOpenCvCameraView = findViewById(R.id.image);
//
//        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
//
//        mOpenCvCameraView.setCvCameraViewListener(this);


        mTess = new TessBaseAPI();
        Assets.extractAssets(getContext());//getBaseContext());

        String dataPath = Assets.getTessDataPath(getContext());//getBaseContext());
        String language = Assets.getLanguage();

        if (!mTess.init(dataPath, "eng")) {
            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file)
            mTess.recycle();
            Log.d(Settings.LOG_TAG, "if (!mTess.init(dataPath, \"eng\")) {");
        }
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO); // Set page segmentation mode

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        for(int i = 0; i < permissions.length; i++){
//            if(Objects.equals(permissions[i], Manifest.permission.CAMERA)){//If camera permission granted
//                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
//                    mOpenCvCameraView.setCameraPermissionGranted();
//                }
//            }
//        }
        mOpenCvCameraView.setCameraPermissionGranted();
//
//        if (requestCode == Settings.CAMERA_PERMISSION_REQUEST) {
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mOpenCvCameraView.setCameraPermissionGranted();
//            } else {
//                String message = "Camera permission was not granted";
//                Log.e(Settings.LOG_TAG, message);
////                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Log.e(Settings.LOG_TAG, "Unexpected permission request");
//        }

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
            Log.d(Settings.LOG_TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getContext(), mLoaderCallback);
        } else {
            Log.d(Settings.LOG_TAG, "OpenCV library found inside package. Using it!");
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
        Log.d(Settings.LOG_TAG, "onCameraViewStarted is started");
        mFrameCounter = 0;
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onCameraViewStopped() {
        //TODO: ADD TO HERE
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        if(keepFrame != null){
            return keepFrame;
        }

        if(frame == null){
            Log.i(Settings.LOG_TAG, "FRAME WAS NULL!!!!");
        }
        Mat rgba = frame.rgba();

        if(buttonClick){
            buttonClick = false;

            Mat preprocessGray = OCR.preprocess(rgba);

//            Mat rotated = new Mat();
            rotate(preprocessGray, preprocessGray, ROTATE_180);

//            separateLiveResultObtainer.startNew();
            recognizeImage2(preprocessGray, rgba);

            return keepFrame;
        }

        if(showingPreprocess){
            return OCR.preprocess(rgba);//rgba;
        }else{
            return rgba;
        }
    }





    public void recognizeImage2(@NonNull Mat mat, @NonNull Mat originalMat0) {
        keepFrame = originalMat0;

//        result = "";
        result.postValue("");
//        processing.setValue(true);
//        progress.setValue("Processing...");
        stopped = false;
        startButton.setClickable(false);//Disable button
        // Start process in another thread
        new Thread(() -> {
            Mat originalMat = originalMat0.clone();

//            mTess.setImage(imagePath);
            // Or set it as Bitmap, Pix,...
            mTess.setImage(CustomBitmapConverters.matToBitmap(mat));
            updateElementViability(textSearchingProgressBar, true);
            updateElementText(startButton, getContext().getString(R.string.processing_image));

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
            result.postValue(text);
            Log.d("Settings.TAG", "Result: "+result.getValue());

            lastResultIterator = mTess.getResultIterator();

            keepFrame = FrameProcessingDisplay.returnProcessedMat(originalMat, lastResultIterator, viewModel.generateWordChecker());
            viewModel.setAllTextFromLastDetection(result.getValue());


            updateElementText(startButton, getContext().getString(R.string.take_new_picture));

            // We can free up the recognition results and any stored image data in the tessApi
            // if we don't need them anymore.
            mTess.clear();


            updateElementViability(textSearchingProgressBar, false);

//             Publish the results
            processing.postValue(false);
            if (stopped) {
                progress.postValue("Stopped.");
            } else {
                long duration = SystemClock.uptimeMillis() - startTime;
                progress.postValue(String.format(Locale.ENGLISH,
                        "Completed in %.3fs.", (duration / 1000f)));
            }
            startButton.setClickable(true); //Re-enable button
        }).start();

    }

    public void updateElementViability(View textSearchingProgressBar, boolean visible){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSearchingProgressBar.setVisibility(visible? View.VISIBLE: View.INVISIBLE);
            }
        });
    }

    public void updateElementText(View view, String newText){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) view).setText(newText);
            }
        });
    }

}

