/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import servermain.Account;

/**
 *
 * @author smits
 */
public interface Dao {
    
    
    public String getDataListString();
    public void createAccountList();
    
    public List<Account> getAccountList();
    public Map<String, Integer> getMap();
    public void addUser(String id, String pw);
    public Account getAccount(String inputId);
    public FileDao getFileDao();
    public void uploadFile(String fileContent, String id);
    public void updateAccountFile() throws IOException;
}
