/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.daoImpl;

import dao.Dao;
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
    
}
