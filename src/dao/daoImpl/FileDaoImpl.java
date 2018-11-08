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

    List<DFile> FileList = new ArrayList<>();

    @Override
    public DFile getFile(String fileName) {
        for (DFile file : FileList) {
            if (fileName.equalsIgnoreCase(file.getFileName())) {
                return file;
            }
        }
        return null;
    }

    @Override
    public void addFile(String fileName, String fileContent) {
        DFile file = new DFile(fileName, fileContent);
        FileList.add(file);

    }

    @Override
    public List<String> getFileNameList() {
        List<String> fileNameList = new ArrayList<>();
        for (DFile file : FileList) {

            fileNameList.add(file.getFileName());
        }
        return fileNameList;
    }

    @Override
    public void createFileList(List<String> usernameList) {
        Helper help = new Helper();

        for (String username : usernameList) {
            File folder = new File(System.getProperty("user.dir") + "\\files\\" + username);
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {

                    if (file.isFile()) {
                        String content = "";
                        try {
                            content = help.readFiles(System.getProperty("user.dir") + "\\files\\"+username +"\\"+ file.getName());
                        } catch (IOException ex) {
                            Logger.getLogger(FileDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        DFile dfile = new DFile(file.getName(), content);
                        FileList.add(dfile);
                    }

                }
            }
        }
    }

    @Override
    public int getFileSize() {
        return FileList.size();
    }

}
