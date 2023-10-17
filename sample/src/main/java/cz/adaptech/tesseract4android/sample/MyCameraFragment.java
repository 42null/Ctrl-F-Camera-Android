package cz.adaptech.tesseract4android.sample;

import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.rotate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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


public class MyCameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2, ViewModelStoreOwner {

    private static final int CAMERA_PERMISSION_REQUEST = 1;

    // Page Elements
    private CameraBridgeViewBase mOpenCvCameraView;
    View startView;

    private TessBaseAPI mTess;

    private ResultIterator lastResultIterator = null;

    public static boolean buttonClick = false;
    public static Mat keepFrame = null;

    private final MutableLiveData<String> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> processing = new MutableLiveData<>(false);
    private ViewModelStore viewModelStore = new ViewModelStore();
    private final MutableLiveData<String> progress = new MutableLiveData<>();
    private boolean stopped;

    private long mFrameCounter = 0;
    private long mStartTime = 0;

    private boolean showingPreprocess = Settings.STARTING_SETTING_SHOW_PREPROCESSING;

    public MyCameraFragment() {
        // Required empty public constructor
        super(R.layout.my_camera_fragment);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(requireContext()) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_camera_fragment, container, false);

        mOpenCvCameraView = rootView.findViewById(R.id.main_surface);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        Button button = rootView.findViewById(R.id.start);
        button.setOnClickListener(v -> {
            if (keepFrame == null) {
                buttonClick = true;
            } else {
                keepFrame = null;
                updateElementText(rootView.findViewById(R.id.start), "Scan For Text");
            }
        });
        startView = button;//TODO: Cleanup

        return rootView;
    }





    @Override
    public void onCameraViewStarted(int width, int height) {
        mFrameCounter = 0;
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Log.d(Settings.LOG_TAG, "CAMERA FRAME!!!");
        if(keepFrame != null){
            return keepFrame;
        }
        Mat rgba = frame.rgba();

        if(buttonClick){
            buttonClick = false;

            Mat preprocessGray = OCR.preprocess(rgba);

//            Mat rotated = new Mat();
            rotate(preprocessGray, preprocessGray, ROTATE_180);


            recognizeImage2(preprocessGray, rgba);
            return keepFrame;
        }

        if(showingPreprocess){
            return OCR.preprocess(rgba);//rgba;
        }else{
            return rgba;
        }    }

    public void recognizeImage2(@NonNull Mat mat, @NonNull Mat originalMat0) {
        keepFrame = originalMat0;

//        result = "";
        result.postValue("");
//        processing.setValue(true);
//        progress.setValue("Processing...");
        stopped = false;

        // Start process in another thread
        new Thread(() -> {
            Mat originalMat = originalMat0.clone();

//            mTess.setImage(imagePath);
            // Or set it as Bitmap, Pix,...
            mTess.setImage(CustomBitmapConverters.matToBitmap(mat));

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

            updateElementText( startView,"Searching for text...");//TODO: Make grab text from settings
            keepFrame = FrameProcessingDisplay.returnProcessedMat(originalMat, lastResultIterator);
            updateElementText( startView,"Take new picture");//TODO: Make grab text from settings

//            updateElementText(findViewById(R.id.start),"Back to camera view");//TODO: Make grab text from settings



            // We can free up the recognition results and any stored image data in the tessApi
            // if we don't need them anymore.
            mTess.clear();

//             Publish the results
            processing.postValue(false);
            if (stopped) {
                progress.postValue("Stopped.");
            } else {
                long duration = SystemClock.uptimeMillis() - startTime;
                progress.postValue(String.format(Locale.ENGLISH,
                        "Completed in %.3fs.", (duration / 1000f)));
            }



        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mOpenCvCameraView.setCameraPermissionGranted();
            } else {
                String message = "Camera permission was not granted";
                Log.e(Settings.LOG_TAG, message);
//                Toast.makeText(this, message, Toast.LENGTH_LONG).show();@@@
            }
        } else {
            Log.e(Settings.LOG_TAG, "Unexpected permission request");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, requireActivity(), mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    // Implement other methods and functions as in your original code

    @NonNull
    public String getResult() {
        return result.getValue();
    }

    @NonNull
    public ResultIterator getResultIterator() {
        return lastResultIterator;
    }

    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
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