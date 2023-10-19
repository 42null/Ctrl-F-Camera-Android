package cz.adaptech.tesseract4android.sample;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MyViewModel extends ViewModel {
    private MutableLiveData<Boolean> showPreProcessing = new MutableLiveData<>();
    private MutableLiveData<String> stringAllTextFromLastDetection = new MutableLiveData<>("");

    public void setBooleanCheckboxShowPreProcessing(boolean value) {
        showPreProcessing.setValue(value);
    }
    public LiveData<Boolean> getBooleanCheckboxShowPreProcessing() {
        return showPreProcessing;
    }


    public void setAllTextFromLastDetection(String value) {
        stringAllTextFromLastDetection.postValue(value);
    }
    public String getAllTextFromLastDetection() {
        return stringAllTextFromLastDetection.getValue();
    }

}
