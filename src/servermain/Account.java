/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author smits
 */

//TO HOLD USER ACCOUNTS
public class Account {
    
    String userId, password,files;
    

    public Account( String data) {
        String [] d = data.split(" ");
        this.userId = d[0];
        this.password = d[1];
        files = "";
        
        for(int i = 2; i < d.length ; i++){
            files = files + " " +  d[i];
        }
        
    }

    public String getfiles() {
        return files;
    }

    public void setfiles(String files) {
        this.files = files;
    }
    
    public void addFile(String fileName){
        files += " " + fileName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getString(){
        String data= userId + " "+ password + files;
        return data;
    }

    @Override
    public String toString() {
        return "Account{" + "userId=" + userId + ", password=" + password + ", files=" + files + '}';
    }
    
    public List<String> getFileList(){
        List<String> fileList = new ArrayList<>();
        for(String item : files.split(" ")){
        fileList.add(item);
        }
        return fileList;
    }
    
    
    

}
