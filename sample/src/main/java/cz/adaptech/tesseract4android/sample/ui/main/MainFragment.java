package cz.adaptech.tesseract4android.sample.ui.main;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;

import cz.adaptech.tesseract4android.sample.Assets;
import cz.adaptech.tesseract4android.sample.MainActivity;
import cz.adaptech.tesseract4android.sample.databinding.FragmentMainBinding;
import cz.adaptech.tesseract4android.sample.ui.main.MainViewModel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.File;

import cz.adaptech.tesseract4android.sample.Assets;
import cz.adaptech.tesseract4android.sample.R;
import cz.adaptech.tesseract4android.sample.databinding.FragmentMainBinding;
//
public class MainFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "LOG_TAG";

    private FragmentMainBinding binding;

    private MainViewModel viewModel;

    private CameraBridgeViewBase mOpenCvCameraView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    boolean buttonClick = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV initialized");
        }

        // Copy sample image and language data to storage
        Assets.extractAssets(requireContext());

        if (!viewModel.isInitialized()) {
            String dataPath = Assets.getTessDataPath(requireContext());
            String language = Assets.getLanguage();
            viewModel.initTesseract(dataPath, language, TessBaseAPI.OEM_LSTM_ONLY);
        }

        Button button = getView().findViewById(R.id.start);//btnTakeFrame
        button.setOnClickListener(v -> {
            Log.d(TAG, "Button Pressed");
            buttonClick = true;
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOpenCvCameraView = (CameraBridgeViewBase) view.findViewById(R.id.image);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        binding.start.setOnClickListener(v -> {
//            File imageFile = Assets.getImageFile(requireContext());
//            viewModel.recognizeImage(imageFile);
        });
        binding.stop.setOnClickListener(v -> {
            viewModel.stop();
        });
        binding.text.setMovementMethod(new ScrollingMovementMethod());

        viewModel.getProcessing().observe(getViewLifecycleOwner(), processing -> {
            binding.start.setEnabled(!processing);
            binding.stop.setEnabled(processing);
        });
        viewModel.getProgress().observe(getViewLifecycleOwner(), progress -> {
            binding.status.setText(progress);
        });
        viewModel.getResult().observe(getViewLifecycleOwner(), result -> {
            binding.text.setText(result);
        });

        mOpenCvCameraView.enableView();

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "onCameraViewStarted started");
    }

    @Override
    public void onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStarted onCameraViewStopped");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Log.d(TAG, "onCameraViewStarted frame");

        // get current camera frame as OpenCV Mat object
        Mat mat = frame.gray();
        mat.setTo(new Scalar(0, 0, 255));

        // update the camera view with the processed frame for live preview
        mOpenCvCameraView.deliverAndDrawFrame(frame);

        // return the processed frame for further processing (e.g., OCR)

        if(buttonClick || MainActivity.buttonClick){
            buttonClick = false;
//                        buttonClick = false;
//            System.out.println("BUTTON CLICK!!!!");
            Log.d(TAG, "Starting...");
//
//            mTess.setImage(bitmap);
//            String recognizedText = mTess.getUTF8Text();
//            System.out.println("BUTTON CLICK!!!!+"+recognizedText);
//
////            mTess.recycle();
//            mTess.clear();
//            viewModel.recognizeImage();
        }

        return mat;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.enableView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

}
