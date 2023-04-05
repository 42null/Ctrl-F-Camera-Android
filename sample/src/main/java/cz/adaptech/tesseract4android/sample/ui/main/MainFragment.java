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

import java.io.File;

import cz.adaptech.tesseract4android.sample.Assets;
import cz.adaptech.tesseract4android.sample.R;
import cz.adaptech.tesseract4android.sample.databinding.FragmentMainBinding;
//
//public class MainFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {
//
//    private static final String LOG_TAG = "LOG_TAG";
//
//    private FragmentMainBinding binding;
//
//    private MainViewModel viewModel;
//
//    private CameraBridgeViewBase mOpenCvCameraView;
//
//
//    public static MainFragment newInstance() {
//        return new MainFragment();
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//
//        if(OpenCVLoader.initDebug()){
//            Log.d(LOG_TAG, "OpenCV initialized");
//        }
//
//        // Copy sample image and language data to storage
//        Assets.extractAssets(requireContext());
//
//        if (!viewModel.isInitialized()) {
//            String dataPath = Assets.getTessDataPath(requireContext());
//            String language = Assets.getLanguage();
//            viewModel.initTesseract(dataPath, language, TessBaseAPI.OEM_LSTM_ONLY);
//        }
//        super.onResume();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        binding = FragmentMainBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
////    @Override
////    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////
////        binding.image.setImageBitmap(Assets.getImageBitmap(requireContext()));
////        binding.start.setOnClickListener(v -> {
////            File imageFile = Assets.getImageFile(requireContext());
////            viewModel.recognizeImage(imageFile);
////        });
////        binding.stop.setOnClickListener(v -> {
////            viewModel.stop();
////        });
////        binding.text.setMovementMethod(new ScrollingMovementMethod());
////
////        viewModel.getProcessing().observe(getViewLifecycleOwner(), processing -> {
////            binding.start.setEnabled(!processing);
////            binding.stop.setEnabled(processing);
////        });
////        viewModel.getProgress().observe(getViewLifecycleOwner(), progress -> {
////            binding.status.setText(progress);
////        });
////        viewModel.getResult().observe(getViewLifecycleOwner(), result -> {
////            binding.text.setText(result);
////        });
////    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // initialize the OpenCV camera view
//        mOpenCvCameraView = view.findViewById(R.id.image);
//        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
//        mOpenCvCameraView.setCvCameraViewListener(this);
//
//        binding.image.setImageBitmap(Assets.getImageBitmap(requireContext()));
//        binding.start.setOnClickListener(v -> {
//            File imageFile = Assets.getImageFile(requireContext());
//            viewModel.recognizeImage(imageFile);
//        });
//        binding.stop.setOnClickListener(v -> {
//            viewModel.stop();
//        });
//        binding.text.setMovementMethod(new ScrollingMovementMethod());
//
//        viewModel.getProcessing().observe(getViewLifecycleOwner(), processing -> {
//            binding.start.setEnabled(!processing);
//            binding.stop.setEnabled(processing);
//        });
//        viewModel.getProgress().observe(getViewLifecycleOwner(), progress -> {
//            binding.status.setText(progress);
//        });
//        viewModel.getResult().observe(getViewLifecycleOwner(), result -> {
//            binding.text.setText(result);
//        });
//    }
//
//
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//
//    }
//
//    @Override
//    public void onCameraViewStopped() {
//
//    }
//
////    @Override
////    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
////        // get current camera frame as OpenCV Mat object
////        Mat mat = frame.gray();
////
////        // return processed frame for live preview
////        return mat;
////    }
//@Override
//public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
//    // get current camera frame as OpenCV Mat object
//    Mat mat = frame.gray();
//
//    // create a new Bitmap object to display the processed frame
//    Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
//
//    // convert the Mat to Bitmap and display it in the ImageView
//    Utils.matToBitmap(mat, bitmap);
//    getActivity().runOnUiThread(new Runnable() {
//        @Override
//        public void run() {
//            binding.image.setImageBitmap(bitmap);
//        }
//    });
//
//    // return processed frame for live preview
//    return mat;
//}
//
//
//
//}
public class MainFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String LOG_TAG = "LOG_TAG";

    private FragmentMainBinding binding;

    private MainViewModel viewModel;

    private CameraBridgeViewBase mOpenCvCameraView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if(OpenCVLoader.initDebug()){
            Log.d(LOG_TAG, "OpenCV initialized");
        }

        // Copy sample image and language data to storage
        Assets.extractAssets(requireContext());

        if (!viewModel.isInitialized()) {
            String dataPath = Assets.getTessDataPath(requireContext());
            String language = Assets.getLanguage();
            viewModel.initTesseract(dataPath, language, TessBaseAPI.OEM_LSTM_ONLY);
        }
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
            File imageFile = Assets.getImageFile(requireContext());
            viewModel.recognizeImage(imageFile);
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
        Log.d(LOG_TAG, "onCameraViewStarted started");
    }

    @Override
    public void onCameraViewStopped() {
        Log.d(LOG_TAG, "onCameraViewStarted onCameraViewStopped");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Log.d(LOG_TAG, "onCameraViewStarted frame");

        // get current camera frame as OpenCV Mat object
        Mat mat = frame.gray();

        // update the camera view with the processed frame for live preview
        mOpenCvCameraView.deliverAndDrawFrame(frame);

        // return the processed frame for further processing (e.g., OCR)
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
