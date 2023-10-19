package cz.adaptech.tesseract4android.sample;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MyViewModel extends ViewModel {
    private MutableLiveData<Boolean> myBoolean = new MutableLiveData<>();

    public void setMyBoolean(boolean value) {
        myBoolean.setValue(value);
    }

    public LiveData<Boolean> getMyBoolean() {
        return myBoolean;
    }
}
