package com.qualitysoftware.whitetiles;

import java.io.Serializable;

public class ScoreEntry implements Serializable {
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
}
