/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.daoImpl;

import dao.Dao;
import dao.FileDao;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
public class DaoImpl  implements Dao{
    
    List<Account> accountList = new ArrayList<>();
    Map<String, Integer> map = new HashMap<String, Integer>();  //FOR FAST DATA ACCESS
    Helper help;
    FileDao fileDao=new FileDaoImpl();
    
    //TO LIMIT NUMBER OF CONNECTIONS
    public int tcount=0;
    
    @Override
    public int getTcount() {
        return tcount;
    }
    @Override
    public void decreaseTcount() {
       tcount = tcount - 1;
    }
    @Override
    public void increaseTcount() {
        tcount = tcount + 1;
    }
    
    
    //TO CONVERT ARRAY TO STRING FORMAT TO WRITE IN FILE
    @Override
    public String getDataListString() {
        String dataListString = "";
        for(Account account : accountList){
            dataListString += account.getString()+"\n";
    
        }
       
        
        return dataListString;
    }

    //FOR LOADING ALL DATA FROM FILE AND STORING IN LIST FOR EASY ACCESS
    @Override
    public void createAccountList() {
        try {
             help = new Helper();
            
            List<String> dataList = help.read("account.txt");
            
            
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

    //UPDATES THE ACCOUNTLIST AND FILE FOR ADDITION OF USER
    @Override
    public void addUser(String id, String pw) {
        String data=id+" "+pw;
        Account account=new Account(data);
        accountList.add(account);
        map.put(id, accountList.indexOf(account));
        try {
           updateAccountFile();
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

    //TO UPDATE ACCOUNT LIST, CREATE FILE 
    @Override
    public void uploadFile(String fileName,String fileContent, String id) {
        getAccount(id).addFile(fileName);
        fileDao.addFile(fileName, fileContent);
        
         try {
            updateAccountFile();
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        try {
            help.writeFile(fileName,fileContent,id);
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //CHECKS FOR DUPLICATE FILENAME
    @Override
    public String verifiedString(String name){
        String suggestedName=name;
        boolean verified=false;
        while(!verified){
        if (check(suggestedName)){
            String[] items= suggestedName.split("\\.");
            if(items[0].contains("_copy_")){
                String[] first=items[0].split("_copy_");

                
                int val=Integer.parseInt(first[1]);
                suggestedName=first[0]+"_copy_"+(val+1)+".txt";
                
            }else{
                suggestedName = items[0]+"_copy_1.txt";
            }
        }
        verified=!check(suggestedName);
        
       
    } 
        return suggestedName;
    }
    
    public boolean check(String name){
        
        for(String currentName: fileDao.getFileNameList()){
            if (name.equalsIgnoreCase(currentName)){
                return true;
            }
    }
        return false;
    }

    @Override
    public void updateAccountFile() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("account.txt"));
        for(Account account : accountList){
            writer.println(account.getString());
        }
        writer.close();
    }

    @Override
    public List<String> getUsers() {
        List<String> usernameList = new ArrayList<>();
        for(Account acc: accountList){
            usernameList.add(acc.getUserId());
        }
        return usernameList;
    }

    
}
