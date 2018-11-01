/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.daoImpl;

import dao.FileDao;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servermain.DFile;
import servermain.Helper;

/**
 *
 * @author smits
 */
public class FileDaoImpl implements FileDao {
    List<DFile>FileList=new ArrayList<>();
    
    @Override
    public DFile getFile(String fileName) {
        for (DFile file:FileList){
            if (fileName.equalsIgnoreCase(file.getFileName())){
                return file;
            }
        }
       return null;
    }

    @Override
    public void addFile(String fileName, String fileContent) {
        DFile file = new DFile(fileName,fileContent);
        FileList.add(file);
        
    }

    @Override
    public void createFileList() {
        
    Helper help = new Helper();
    File folder = new File("/files");
    File[] listOfFiles = folder.listFiles();

    for (File file : listOfFiles) {
    if (file.isFile()) {
        String content = "";
        try {
            help.readFiles(file.getName());
        } catch (IOException ex) {
            Logger.getLogger(FileDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
     DFile dfile = new DFile(file.getName(),content);
     FileList.add(dfile);
     
        
        
    }
}
    }
    
    
}
