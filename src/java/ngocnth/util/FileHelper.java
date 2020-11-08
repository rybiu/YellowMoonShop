/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ngocnth.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import javax.servlet.http.Part;

/**
 *
 * @author Ruby
 */
public class FileHelper {
    
    public static File uploadFile(Part filePart, String dir) throws IOException {
        String fileName = filePart.getSubmittedFileName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String targetFileName = new Date().getTime() + "." + fileExtension;
        InputStream fileContent = filePart.getInputStream();
        File file = new File(dir + "\\" + targetFileName);
        Files.copy(fileContent, file.toPath());
        return file;
    }
    
    public static void deleteFile(String fileName, String dir) throws IOException {
        File file = new File(dir + "\\" + fileName);
        if (file.exists()) Files.delete(file.toPath());
    }
    
}
