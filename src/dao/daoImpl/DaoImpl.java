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
//                System.out.println(parse);
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
        //System.out.println("addUser");
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

    @Override
    public void uploadFile(String fileContent, String id) {
        System.out.println("File content" + fileContent);
        String[] items = fileContent.split("\\*");
        System.out.println("SPlit name" + items[0]);
        String verified = verifiedString(items[0]);
        System.out.println("Verified Name"+ verified);
        getAccount(id).addFile(verified);
        fileDao.addFile(verified, items[1]);
        
         try {
            updateAccountFile();
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        try {
            help.writeFile(verified,items[1]);
        } catch (IOException ex) {
            Logger.getLogger(DaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String verifiedString(String name){
        String suggestedName=name;
        System.out.println("suggestedname"+suggestedName);
        boolean verified=false;
        while(!verified){
            System.out.println("inside loop");
        if (check(suggestedName)){
            System.out.println("checking");
            System.out.println("Size " + suggestedName);
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
        
       
    }   System.out.println("suggestedname2"+suggestedName);
        return suggestedName;
    }
    
    public boolean check(String name){
        System.out.println("called");
        
        for(String currentName: fileDao.getFileNameList()){
            System.out.println("cur"+currentName);
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

    
}
