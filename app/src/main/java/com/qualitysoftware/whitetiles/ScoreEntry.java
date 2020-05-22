package com.qualitysoftware.whitetiles;

import java.io.Serializable;

import static java.lang.Integer.parseInt;

public class ScoreEntry implements Serializable, Comparable<ScoreEntry>{
    private String value;
    private String time;

    public ScoreEntry(String _value, String _time){
        value = _value;
        time = _time;
    }

    public String getValue(){
        return value;
    }

    public String getTime(){
        return time;
    }

    @Override
    public int compareTo(ScoreEntry comparesTo) {
        return parseInt(comparesTo.getValue()) - parseInt(value);
    }
}
