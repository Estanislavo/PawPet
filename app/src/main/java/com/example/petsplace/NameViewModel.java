package com.example.petsplace;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.petsplace.auxiliary.ArticleIntroduction;

import java.util.ArrayList;

public class NameViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<ArrayList<ArticleIntroduction>> currentName;

    public MutableLiveData<ArrayList<ArticleIntroduction>> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<>();
        }
        return currentName;
    }

    public void setCurrentName(ArrayList<ArticleIntroduction> art){
        currentName.postValue(art);
    }

// Rest of the ViewModel...
}