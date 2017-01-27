package com.fbr1.mesasutnfrro.util;

public class MesasUtil {
    /**
     * Removes white spaces at the beginning and end of a String.
     * Also removes double white spaces.
     *
     * @param str - String to remove extra white spaces
     * @return      String with extra white spaces removed
     */
    public static String removeExtraSpaces(String str){

        // Removes doubles white spaces
        str = str.replaceAll("\\s+", " ");

        if(str.length() > 1){
            // Remove extra white space at the end
            if(str.charAt(str.length()-1)==' '){
                str = str.substring(0, str.length()-1);
            }
            // Remove extra white space at the beginning
            if(str.charAt(0)==' '){
                str = str.substring(1, str.length());
            }
        }

        return str;
    }
}
