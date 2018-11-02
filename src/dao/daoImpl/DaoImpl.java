/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.daoImpl;

import dao.Dao;
import dao.FileDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import servermain.Account;
import servermain.Helper;

/**
 *
 * @author smits
 */
public class DaoImpl implements Dao{
    
    List<Account> accountList = new ArrayList<>();
    Map<String, Integer> map = new HashMap<String, Integer>();
    Helper help;
    FileDao fileDao=new FileDaoImpl();
    
    
    
    @Override
    public String getDataListString() {
        String dataListString = "";
        for(Account account : accountList){
            dataListString += account.getString();
            dataListString+="\n";
        }
        return dataListString;
    }

    @Override
    public void createAccountList() {
        try {
             help = new Helper();
            
            List<String> dataList = help.read("account.txt");
            //help.update("account.txt","20");
            
            
            for (String parse : dataList) {
                String[] parts = parse.split(" ");
                Account account = new Account (parse);
                accountList.add(account);
                map.put(parts[0],accountList.indexOf(account));
                
            }
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Account> getAccountList() {
        return accountList;
    }

    @Override
    public Map<String, Integer> getMap() {
        return map;
    }

    @Override
    public void addUser(String id, String pw) {
        String data=id+" "+pw;
        Account account=new Account(data);
        accountList.add(account);
        map.put(id, accountList.indexOf(account));
        try {
            help.write("account.txt", getDataListString());
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Account getAccount(String inputId) {

        int index=(int)map.get(inputId);
        return accountList.get(index);
    }

    @Override
    public FileDao getFileDao() {
        return fileDao;
    }

    @Override
    public void uploadFile(String fileContent, String id) {
        String[] items = fileContent.split("\\*");
        
        verifiedString(items[0]);
        
        getAccount(id).addFile(items[0]);
        fileDao.addFile(items[0], items[1]);
         try {
            help.write("account.txt", getDataListString());
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        try {
            help.writeFile(items[0],items[1]);
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String verifiedString(String name){
        String suggestedName=name;
        boolean verified=false;
        while(!verified){
        if (check(name)){
            String[] items=name.split(".");
            String[] first=items[0].split("_copy_");
            if(first.length < 1){
            int val=Integer.parseInt(first[1]);
            suggestedName=first[0]+"_copy_"+(val+1)+".txt";
            }else{
                suggestedName = first[0]+"_copy_1";
            }
        }
        verified=!check(suggestedName);
    }
        return suggestedName;
    }
    
    public boolean check(String name){
        for(String currentName: fileDao.getFileNameList()){
            if (name.equals(currentName)){
                return true;
            }
    }
        return false;
    }

    
}
