package cz.adaptech.tesseract4android.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;


public class SearchFilteringDialogFragment extends DialogFragment {

    private MyViewModel viewModel;

    private String persistentText = ""; //TODO: Make save in viewModel true persistence
    Map<String, Boolean> localSearchOptionsHashMap;

//    Options


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        localSearchOptionsHashMap = viewModel.getSettingsStateMap(); //Get latest

        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        View customTitleView = getLayoutInflater().inflate(R.layout.edit_search_parameters_dialog, null);
//        builder.setCustomTitle(customTitleView);
//        TextView titleTextView = customTitleView.findViewById(R.id.alertTitle);
//        titleTextView.setText("Search Filter Options");
//        // Create an EditText field programmatically
//        final EditText editText = new EditText(getContext());
//        editText.setHint("Enter search word here");

        View customDialogView = getLayoutInflater().inflate(R.layout.edit_search_parameters_dialog, null);
        builder.setView(customDialogView);

        ListView multiChoiceListView = customDialogView.findViewById(R.id.multiChoiceListView);

        String[] providedItemsKeys = new String[localSearchOptionsHashMap.size()];
//        providedItemsKeys = settingsStateMap.keySet().toArray();


        multiChoiceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        multiChoiceListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, providedItemsKeys));

        multiChoiceListView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle multi-choice item click
            String key = providedItemsKeys[position];
            localSearchOptionsHashMap.put(key, multiChoiceListView.isItemChecked(position));
        });

        // Find the EditText field and set its hint and text
        EditText editText = customDialogView.findViewById(R.id.editText);
        editText.setHint("Enter search word here");
        editText.setText(persistentText);

//        for (int j = 0; j < 2; j++) {
//            multiChoiceListView.setItemChecked(j, true);
//        }

        int i = 0;
        for(String key : localSearchOptionsHashMap.keySet()){ //Avoids giving access
            providedItemsKeys[i] = key;
            multiChoiceListView.setItemChecked(i, Boolean.TRUE.equals(localSearchOptionsHashMap.get(key)));
            i++;
        }

        // Set title
        builder.setTitle("Search Filter Options");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener(){//Save (Button text set later)
            public void onClick(DialogInterface dialog, int id){
                //Save settings
                viewModel.setSettingsStateMap(localSearchOptionsHashMap);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){//Cancel (Button text set later)
            public void onClick(DialogInterface dialog, int id){
                //Cancel

            }
        });





////        Somehow this aligns the positive to the right and the negative to the left.
//        builder.setPositiveButton("Save", null);
//        builder.setNegativeButton("Cancel", null);

//        builder.setView(editText);

        AlertDialog dialog = builder.create();
        return dialog;
    }


}