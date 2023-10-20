package cz.adaptech.tesseract4android.sample;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class MyViewModel extends ViewModel {
    private MutableLiveData<Boolean> showPreProcessing = new MutableLiveData<>();
    private MutableLiveData<String> stringAllTextFromLastDetection = new MutableLiveData<>("");

    private Map<String, Boolean> settingsStateMap = new HashMap<>() {{
        put("\"(\" = \"C\"", true);
        put("Ignore Capitalization", false);
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


//Search Settings
    public void setSettingsStateMap(Map<String, Boolean> settingsMap) {
        this.settingsStateMap = settingsMap;
    }
    public Map<String, Boolean> getSettingsStateMap() {
        return settingsStateMap;
    }




}
