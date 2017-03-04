package com.fbr1.mesasutnfrro.forms;

import com.fbr1.mesasutnfrro.util.MesasUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MesaPDFValidator {

    private MultipartFile[] multipartFiles;

    private String message;

    public MesaPDFValidator(MultipartFile[] multipartFiles) {
        this.multipartFiles = multipartFiles;
    }

    public boolean isValid() throws IOException {
        // Check for invalid PDF format
        for(MultipartFile file : multipartFiles){
            if(!MesasUtil.checkIsPDF(file.getBytes())){
                message = "El archivo no tiene formato de pdf";
                return false;
            }
        }

        // TODO Check for invalid Mesa PDF format

        return true;
    }

    public String getMessage() {
        return message;
    }
}
