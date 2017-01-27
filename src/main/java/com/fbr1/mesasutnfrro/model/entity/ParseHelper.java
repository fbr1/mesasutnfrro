package com.fbr1.mesasutnfrro.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ParseHelper {

    private static final int ESPECIALIDAD = 0;
    private static final int AULA = 1;
    private static final int EXAMEN = 2;
    public static final int MAX_DEPTH = 2;

    public static final Pattern ESPECIALIDAD_REGEX = Pattern.compile("( ?(?:ISI|IE|IQ|IC|IM) ?)");

    // Regex matching one or more aulas
    public static final Pattern AULA_REGEX =
            Pattern.compile("(?<!(?:\\.|:)|(?:\\.|:)\\d)([\\d{1,3}/]+\\d{1,3} ?|\\d{1,3} ?|SUM ?)(?!\\d(?:\\.|:)|(?:\\.|:))");

    // Regex matching aula in line.
    // i.e: "308"
    public static final Pattern AULA_LINE_REGEX = Pattern.compile("(^\\d{2,3}|SUM)$");

    // Regex matching a line with only the time
    public static final Pattern HOURS_LINE_REGEX = Pattern.compile("(^\\d{1,2}.\\d{2}.\\d{2}$)");

    // Regex matching "<DAY> <DAYNUMBER> DE <MONTH> DE <YEAR>"
    public static final Pattern DATE_REGEX =
            Pattern.compile("(?<day>[a-zA-Z]+) (?<daynumber>\\d{1,2}) DE (?<month>[a-zA-Z]+) DE (?<year>\\d{4})");

    public static final Pattern HOURS_REGEX = Pattern.compile("(\\d{1,2}.\\d{2}.\\d{2})");


    private int depth;
    private String name;
    private List<String> lines;
    private List<ParseHelper> childs;

    public Pattern getMatchPattern(){
        switch(this.depth){
            case ParseHelper.ESPECIALIDAD:
                return ESPECIALIDAD_REGEX;
            case ParseHelper.AULA:
                return AULA_REGEX;
            case ParseHelper.EXAMEN:
                return Pattern.compile("(.+)");
            default:
                return null;
        }
    }

    public ParseHelper() {
        this.depth = 0;
        lines = new ArrayList<>();
    }

    public ParseHelper(int depth) {
        this();
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

    public List<ParseHelper> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ParseHelper> childs) {
        this.childs = childs;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
