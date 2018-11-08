/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

/**
 *
 * @author smits
 */

//MANAGING FILES
public class DFile {
    
    String fileName,contents;

    public DFile(String fileName, String contents) {
        this.fileName = fileName;
        this.contents = contents;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContents() {
        return contents;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    
    
    
}
