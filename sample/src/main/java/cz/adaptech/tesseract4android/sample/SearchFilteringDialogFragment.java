package cz.adaptech.tesseract4android.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;


public class SearchFilteringDialogFragment extends DialogFragment {

    private String persistentText = ""; //TODO: Make save in viewModel true persistence

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View customTitleView = getLayoutInflater().inflate(R.layout.centered_text_view, null);
        builder.setCustomTitle(customTitleView);

        TextView titleTextView = customTitleView.findViewById(R.id.alertTitle);
        titleTextView.setText("Search Filter Options");


        builder.setMessage("Options")
//            .setMultiChoiceItems(new String[]{"test","test2"}, null, new DialogInterface.OnMultiChoiceClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//
//                }
//            })
            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            })
           .setNegativeButton("Save", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   // User cancels the dialog.
               }
           });

        // Create an EditText field programmatically
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter text here");


//        Somehow this aligns the positive to the right and the negative to the left.
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);

        builder.setView(editText);

        AlertDialog dialog = builder.create();
        return dialog;
    }


}