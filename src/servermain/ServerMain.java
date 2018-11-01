/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

import dao.Dao;
import dao.daoImpl.DaoImpl;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author smits
 */
public class ServerMain {

    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket=new ServerSocket(9002);
//        Map<String, Integer> map = new HashMap<String, Integer>();
   
//        Helper help = new Helper();
//
//        List<String> dataList = help.read("account.txt");
        //help.update("account.txt","20");
        
//        List<Account> accountList = new ArrayList<>();
//        for (String parse : dataList) {
//            String[] parts = parse.split(" ");
//            Account account = new Account (parse);
//            accountList.add(account);
//            map.put(parts[0],accountList.indexOf(account));
//            
//        }

        Dao dao = new DaoImpl();
        dao.createAccountList();
        dao.getFileDao().createFileList();
        
        
        System.out.println(dao.getDataListString());
        
        
        
        int tcount=0;
        
        while (tcount<5){
            Socket socket=null;
            
            try{
                socket=serverSocket.accept();
                
                DataInputStream dis=new DataInputStream(socket.getInputStream());
                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
                
                Thread t=new Handler(socket,dis,dos, dao);
                tcount++;
                
                t.start();
                   
            }
            
            catch(Exception e){
                socket.close();
                e.printStackTrace();
            }
        
         

     
        
        
       // System.out.println(map.get("3"));
        //System.out.println(accountList.get(0));
       
    }
}}

