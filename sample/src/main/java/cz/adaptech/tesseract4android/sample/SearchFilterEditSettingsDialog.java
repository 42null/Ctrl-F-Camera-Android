package cz.adaptech.tesseract4android.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

public class SearchFilterEditSettingsDialog extends DialogFragment {

    public interface OnSearchSettingsListener {
        void onSaveSettings(String searchText, boolean isSearchEnabled, boolean option1Checked, boolean option2Checked);
        void onCancelSettings();
    }

    private EditText searchInput;
    private Switch searchSwitch;
    private CheckBox checkBox1;
    private CheckBox checkBox2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_filtering_edit_settings_dialog, container, false);

        CardView cardView = view.findViewById(R.id.cardView);
//        searchInput = view.findViewById(R.id.search_input);
////        searchSwitch = view.findViewById(R.id.search_switch);
//        checkBox1 = view.findViewById(R.id.checkBox1);
//        checkBox2 = view.findViewById(R.id.checkBox2);

//        // Set up the Save and Cancel buttons
//        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSaveButtonClick();
//            }
//        });
//
//        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCancelButtonClick();
//            }
//        });

        return view;
    }

    private void onSaveButtonClick() {
        if (getActivity() instanceof OnSearchSettingsListener) {
            String searchText = searchInput.getText().toString();
            boolean isSearchEnabled = searchSwitch.isChecked();
            boolean option1Checked = checkBox1.isChecked();
            boolean option2Checked = checkBox2.isChecked();

            ((OnSearchSettingsListener) getActivity()).onSaveSettings(searchText, isSearchEnabled, option1Checked, option2Checked);
        }
        dismiss();
    }

    private void onCancelButtonClick() {
        if (getActivity() instanceof OnSearchSettingsListener) {
            ((OnSearchSettingsListener) getActivity()).onCancelSettings();
        }
        dismiss();
    }
}

