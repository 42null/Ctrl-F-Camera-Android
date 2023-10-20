package cz.adaptech.tesseract4android.sample;

import static cz.adaptech.tesseract4android.sample.Settings.CAMERA_PERMISSION_REQUEST;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;

import org.opencv.android.OpenCVLoader;


public class MainActivity extends AppCompatActivity {

//    Settings bar
    MaterialCheckBox showPreProcessingMaterialCheckBox;
    MaterialButton copyTextMaterialButton;
    MaterialButton findTextMaterialButton;
    MaterialButton editTextMaterialButton;



    private CameraFragment cameraFragment;

    private MyViewModel viewModel;


    private int lastNavBarLightColor = -1; //Unset

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            cameraFragment = new CameraFragment();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, cameraFragment, null)
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

//        Larger parts
        cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.example_fragment2);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

//        Page elements (settings bar)
        showPreProcessingMaterialCheckBox = (MaterialCheckBox) findViewById(R.id.selectorOptionCheckbox1);
        copyTextMaterialButton = (MaterialButton) findViewById(R.id.selectorOptionCopyButton);
        findTextMaterialButton = (MaterialButton) findViewById(R.id.selectorOptionFindButton);
        editTextMaterialButton = (MaterialButton) findViewById(R.id.selectorOptionEditButton);

        runOnUiThread(new Runnable(){
            @Override
            public void run(){ showPreProcessingMaterialCheckBox.setChecked(Settings.STARTING_SETTING_SHOW_PREPROCESSING); }
        });

        changeNavigationBarColor(showPreProcessingMaterialCheckBox.getCheckedState() == MaterialCheckBox.STATE_CHECKED);
        showPreProcessingMaterialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                viewModel.setBooleanCheckboxShowPreProcessing(isChecked);
                changeNavigationBarColor(isChecked);
            }
        });

        copyTextMaterialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String textToCopy = viewModel.getAllTextFromLastDetection();
                ClipData clip = ClipData.newPlainText("label", textToCopy);
                clipboard.setPrimaryClip(clip);

//                int snackbarAvailableWidth = getInnerAvailableElementWidth(v);
//
//                Paint textPaint = new Paint();
//                float textWidth = textPaint.measureText(textToCopy);
//
//                String truncatedText;
//                if (textWidth > snackbarAvailableWidth) {
//                    int maxLength = (int)((snackbarAvailableWidth / textWidth) * textToCopy.length());
//                    truncatedText = textToCopy.substring(0, maxLength);
//                } else {
//                    // The original text fits, so display it as is
//                    truncatedText = textToCopy;
//                }
//
//                Snackbar.make(v, "Copied \""+(truncatedText)+"\"", Snackbar.LENGTH_SHORT).show();
            }
        });

        findTextMaterialButton.setOnClickListener(v -> {
//            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            String textToCopy = viewModel.getAllTextFromLastDetection();
//            ClipData clip = ClipData.newPlainText("label", textToCopy);
//            clipboard.setPrimaryClip(clip);
        });

        editTextMaterialButton.setOnClickListener(v -> {
            new SearchFilteringDialogFragment().show(getSupportFragmentManager(), "GAME_DIALOG");

//            SearchFilterEditSettingsDialog searchSettingsFragment = new SearchFilterEditSettingsDialog();
//            searchSettingsFragment.show(getSupportFragmentManager(), "SearchSettingsFragment");
        });

    }

    private int getInnerAvailableElementWidth(View view){
        // Calculate available width
        int viewWidth = view.getWidth();
        int padding = view.getPaddingLeft() + view.getPaddingRight();
        int margin = ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin + ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).rightMargin;
        return viewWidth - padding - margin;
    }

    private void changeNavigationBarColor(boolean isShowingPreProcessing){//TODO: Fix to be update instead of change!!!
        if(Settings.allowChangeNavigationBarColor){
            // Check if the current mode is light
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO){
                // Set the navigationBarColor only in light mode
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                if(viewModel.getBooleanCheckboxShowPreProcessing().getValue()){ //Might not update in time
                    int color = getWindow().getNavigationBarColor();
                    if(lastNavBarLightColor == -1 && color != getResources().getColor(R.color.white)){
                        lastNavBarLightColor = color;
                    }

                    if(isShowingPreProcessing){
                        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
                    }else{
                        getWindow().setNavigationBarColor(lastNavBarLightColor);
                    }

                }
            }
        }
    }


    @Override
    protected void onStart(){
        super.onStart();

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
