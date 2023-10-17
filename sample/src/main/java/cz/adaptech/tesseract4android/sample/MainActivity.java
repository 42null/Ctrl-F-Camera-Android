package cz.adaptech.tesseract4android.sample;

import static cz.adaptech.tesseract4android.sample.Settings.CAMERA_PERMISSION_REQUEST;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.checkbox.MaterialCheckBox;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.OpenCVLoader;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    MaterialCheckBox materialCheckBox;


    private FragmentManager fragmentManager;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, new ExampleFragment2(), null)
                    .commit();
        }

//        if (savedInstanceState == null) {
//            MyCameraFragment myCameraFragment = new MyCameraFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, myCameraFragment)
//                    .commit();
//        }
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!OpenCVLoader.initDebug()) {
            Log.d(Settings.LOG_TAG, "Main OpenCV not loaded");
        } else {
            Log.d(Settings.LOG_TAG, "Main OpenCV loaded");
        }
        setContentView(R.layout.activity_main);


        // Permissions for Android 6+
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
        );

//        if (savedInstanceState == null) {
//            MyCameraFragment myCameraFragment = new MyCameraFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MyCameraFragment.newInstance())
//                    .commitNow();
//        }

        materialCheckBox = (MaterialCheckBox) findViewById(R.id.selectorOptionCheckbox1);
        Log.d(Settings.LOG_TAG, "!!!isChecked = "+"A");
        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                Log.d(Settings.LOG_TAG, "!!!isChecked = "+isChecked);
//                myCameraActivity.setShowPreProcessing(isChecked);
            }
        });
        Log.d(Settings.LOG_TAG, "!!!isChecked = "+"B");

//        myCameraActivity = new MyCameraActivity();

//        runOnUiThread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                materialCheckBox.setChecked(Settings.STARTING_SETTING_SHOW_PREPROCESSING);
//            }
//        });
        fragmentManager = getSupportFragmentManager();
//        fragmentManager.getFragment(ExampleFragment.bu)
    }

    @Override
    protected void onStart(){
        super.onStart();

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container_view, ExampleFragment.class, null)
//                    .commit();
////        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        ExampleFragment2 exampleFragment2 = (ExampleFragment2) fragmentManager.findFragmentById(R.id.example_fragment2);
//        if(exampleFragment2 != null){
//            exampleFragment2.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }

//        for(int i = 0; i < permissions.length; i++){
//            if(Objects.equals(permissions[i], Manifest.permission.CAMERA)){//If camera permission granted
//                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
//                    ExampleFragment2 exampleFragment2 = (ExampleFragment2) fragmentManager.findFragmentById(R.id.example_fragment2);
//                    exampleFragment2.onRequestPermissionsResult(requestCode, permissions, grantResults);
////                    if (exampleFragment2 != null) {
////                        // Call the method on the Fragment
////                        exampleFragment2.setCameraPermissionWasGranted();
////                    }
//                }
//            }
//        }
        // Call methods from CameraActivity as needed
//        myCameraActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


}
