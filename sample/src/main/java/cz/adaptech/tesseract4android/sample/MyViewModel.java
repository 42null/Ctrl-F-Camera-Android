package cz.adaptech.tesseract4android.sample;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyViewModel extends ViewModel {

    public static final String keyParenthesesAsCs = "\"(\" = \"C\""; //TODO: Move to enum
    public static final String keyIgnoreCapitalization = "Ignore Capitalization"; //TODO: Move to enum

    private MutableLiveData<Boolean> showPreProcessing = new MutableLiveData<>();
    private MutableLiveData<String> stringAllTextFromLastDetection = new MutableLiveData<>("");

    private MutableLiveData<String> searchString = new MutableLiveData<>("");

    private Map<String, Boolean> settingsStateMap = new HashMap<>() {{
        put(keyParenthesesAsCs, false);
        put(keyIgnoreCapitalization, true);
    }};


//Preprocess
    public void setBooleanCheckboxShowPreProcessing(boolean value) {
        showPreProcessing.setValue(value);
    }
    public LiveData<Boolean> getBooleanCheckboxShowPreProcessing() {
        return showPreProcessing;
    }

//Last Text
    public void setAllTextFromLastDetection(String value) {
        stringAllTextFromLastDetection.postValue(value);
    }
    public String getAllTextFromLastDetection() {
        return stringAllTextFromLastDetection.getValue();
    }


//Search Strings
    public void setSearchString(String value) {
        searchString.postValue(value);
    }
    public String getSearchString() {
        return searchString.getValue();
    }

//Search Settings
    public void setSettingsStateMap(Map<String, Boolean> settingsMap) {
        this.settingsStateMap = settingsMap;
    }
    public Map<String, Boolean> getSettingsStateMap() {
        return settingsStateMap;
    }




    public WordChecker generateWordChecker(){
        return new WordChecker(Arrays.asList(searchString.getValue()), settingsStateMap.get(keyIgnoreCapitalization));
    }


}
