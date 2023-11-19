package cz.adaptech.tesseract4android.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordChecker {

    private List<String> wordsToFind;
    private List<String> wordsToFindIgnoreCapitals = null;
    private boolean ignoreCapitals;

    public WordChecker(List<String> wordsToFind, boolean ignoreCapitals){
        this.wordsToFind = wordsToFind;
        this.ignoreCapitals = ignoreCapitals;

        if(ignoreCapitals){
            generateIgnoreCapitalsList();
        }
    }

    private void generateIgnoreCapitalsList(){
        wordsToFindIgnoreCapitals = new ArrayList<>();
        for(String word: wordsToFind){
            wordsToFindIgnoreCapitals.add(word.toLowerCase(Locale.ROOT));
        }
    }


    public boolean checkWord(String word){
        if(this.ignoreCapitals){
            if(wordsToFindIgnoreCapitals == null){
                generateIgnoreCapitalsList();
            }
        }

        for(String searchStr: this.ignoreCapitals? wordsToFindIgnoreCapitals : wordsToFind){
            if(this.ignoreCapitals){
                word = word.toLowerCase(Locale.ROOT);
            }
            if(word.contains(searchStr)){
                return true;
            }
        }
        return false;
    }

}
