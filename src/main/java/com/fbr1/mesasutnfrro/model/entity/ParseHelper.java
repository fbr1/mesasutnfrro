package com.fbr1.mesasutnfrro.model.entity;

import java.util.ArrayList;

public class ParseHelper {

    public static final int ESPECIALIDAD = 0;
    public static final int AULA = 1;
    public static final int EXAMEN = 2;
    public static final int MAX_DEPTH = 2;

    private int depth;
    private String text;
    private String name;
    private ArrayList<ParseHelper> childs;


    public String getSplitPattern(){
        switch(this.depth){
            case ParseHelper.ESPECIALIDAD:
                return "(ISI|IE|IQ|IC|IM)\\n";
            case ParseHelper.AULA:
                return "(?<!\\.|:)(?:(?:\\d{2,3})(?:\\/\\d{2,3})*|SUM)(?!\\.|:)";
            case ParseHelper.EXAMEN:
                return System.getProperty("line.separator");
            default:
                return null;
        }
    }

    public String getMatchPattern(){
        switch(this.depth){
            case ParseHelper.ESPECIALIDAD:
                return "(ISI|IE|IQ|IC|IM)\\n";
            case ParseHelper.AULA:
                return "(?<!\\.|:)((?:\\d{2,3})(?:\\/\\d{2,3})*|SUM)(?!\\.|:)";
            case ParseHelper.EXAMEN:
                return "()";
            default:
                return null;
        }
    }

    public ParseHelper() {
        this.depth = 0;
    }

    public ParseHelper(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<ParseHelper> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ParseHelper> childs) {
        this.childs = childs;
    }
}
