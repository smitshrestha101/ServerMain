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

        Dao dao = new DaoImpl();
        dao.createAccountList();
        dao.getFileDao().createFileList(dao.getUsers());
        
        System.out.println(dao.getDataListString());

        while (dao.getTcount()<=5){
            Socket socket=null;
            
            try{
                
                socket=serverSocket.accept();
                               
                DataInputStream dis=new DataInputStream(socket.getInputStream());
                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
                
                if (dao.getTcount()>4){                     //LIMITING NUMBER OF THREADS
                    dos.writeUTF("BUSY");
                    System.out.println("BUSY");
                }
                else{
                    
                    dos.writeUTF("READY");
                                
                Thread t=new Handler(socket,dis,dos, dao);
                                
                dao.increaseTcount();
                
                t.start();
                }
            }
            
            catch(Exception e){
                socket.close();
                e.printStackTrace();
            }

       
    }
}}

