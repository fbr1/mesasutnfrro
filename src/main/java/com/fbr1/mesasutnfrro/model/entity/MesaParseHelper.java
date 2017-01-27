package com.fbr1.mesasutnfrro.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MesaParseHelper {

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

    // Regex matching  1-Mañana, 2-Tarde y 3-Noche
    public static final Pattern TURNO_REGEX =
            Pattern.compile("(?:\\d-)?(?:Ma\\p{L}ana*|Tarde\\p{L}*|Noche\\p{L}*)",Pattern.CASE_INSENSITIVE);

    public static final Pattern HOURS_REGEX = Pattern.compile("(\\d{1,2}.\\d{2}.\\d{2})");


    private int depth;
    private String name;
    private List<String> lines;
    private List<MesaParseHelper> childs;

    public Pattern getMatchPattern(){
        switch(this.depth){
            case MesaParseHelper.ESPECIALIDAD:
                return ESPECIALIDAD_REGEX;
            case MesaParseHelper.AULA:
                return AULA_REGEX;
            case MesaParseHelper.EXAMEN:
                return Pattern.compile("(.+)");
            default:
                return null;
        }
    }

    public MesaParseHelper() {
        this.depth = 0;
        lines = new ArrayList<>();
    }

    public MesaParseHelper(int depth) {
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

    public List<MesaParseHelper> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<MesaParseHelper> childs) {
        this.childs = childs;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
