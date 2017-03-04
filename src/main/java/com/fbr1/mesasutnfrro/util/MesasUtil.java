package com.fbr1.mesasutnfrro.util;

import org.apache.pdfbox.io.IOUtils;

import java.io.*;
import java.text.Collator;
import java.util.List;

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

    public static boolean equalsStringIgnoringAccents(String str1, String str2){
        final Collator instance = Collator.getInstance();

        // Ignore Accents
        instance.setStrength(Collator.NO_DECOMPOSITION);

        if (instance.compare(str1, str2) == 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Saves files to disk
     * Accepts inputStreams coming from files or HTTP connections
     *
     * @param is - InputStream
     * @param filePath - Location to save the file
     */
    public static void saveToDisk(InputStream is, String filePath) throws IOException{
        File targetFile = new File(filePath);
        OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(outStream);

    }
    /**
     * Check if the file starts with %PDF-
     *
     * @param data - Byte array of a file
     * @return  boolean - Whether the file is a PDF or not
     */
    public static boolean checkIsPDF(byte[] data)
    {
        if(data != null && 
                data.length > 4 &&
                data[0] == 0x25 && // %
                data[1] == 0x50 && // P
                data[2] == 0x44 && // D
                data[3] == 0x46 && // F
                data[4] == 0x2D) // -
        {
            return true;
        }
        return false;

    }
}
