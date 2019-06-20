package com.example.crossword.models;

import java.util.ArrayList;

public class Crossword {

    private ArrayList<Synonym> synonyms;
    private String type;
    private int timer;

    public ArrayList<Synonym> getSynonyms(){
        return synonyms;
    }

    public void setSynonyms(ArrayList<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimer() {
        return timer;
    }
    public void setTimer(int timer){
        this.timer = timer;
    }

}

