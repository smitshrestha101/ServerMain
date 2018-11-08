/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

import dao.Dao;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smits
 */
public class Handler extends Thread {
    
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    Dao dao;
    String menu = "Select an option:\n"
            + "1. New User\n"
            + "2. Existing User\n"
            + "3. Disconnect";
    
    public Handler(Socket socket, DataInputStream dis, DataOutputStream dos, Dao dao) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        this.dao = dao;
        
    }
    
    @Override
    public void run() {
        String receive;
        boolean loop = true;
        
        while (loop) {
            try {
                
                dos.writeUTF(menu);
                
                receive = dis.readUTF();
                
                switch (receive) {
                    case "1":
                        newUser();
                        //this.socket.close();
                        break;
                    case "2":
                        existingUser();
                        //this.socket.close();
                        break;
                    case "3":
                        disconnect();
                        loop = false;
                        break;
                    default:
                        break;
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
        try {
            this.dis.close();
            this.dos.close();
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void newUser() throws IOException {
        String input = "";
        Helper help = new Helper();
        
        boolean exists = true;
        String id = "";
        String pw = "";
        while (exists == true) {            
            dos.writeUTF("READY");
            dos.writeUTF("READY*Enter new id and password separated by *: ");
            
            input = dis.readUTF();
            // System.out.println("input "+input);
            String[] idpw = input.split("\\*");
            id = idpw[0];
            pw = idpw[1];
            
            boolean keyExist = dao.getMap().containsKey(id);
            if (keyExist == false) {
                exists = false;
                dos.writeUTF("SUCCESS");
            } else {
                dos.writeUTF("CREATEFAILED");
            }
        }
        // System.out.println("adduser");
        dao.addUser(id, pw);
        
        fileAccess(id);
        
    }
    
    public void existingUser() throws IOException {
        String inputId, inputPw, inputIdPw;
        
        int trials = 0;
        dos.writeUTF("READY");
        
        while (trials < 3) {
            
            dos.writeUTF("Enter id and password separated by *: ");
            inputIdPw = dis.readUTF();
            
            String[] input = inputIdPw.split("\\*");
            inputId = input[0];
            inputPw = input[1];

            //dos.writeUTF("Enter Password: ");
            // inputPw = dis.readUTF();
            //System.out.println(inputPw+inputId);
            boolean keyExist = dao.getMap().containsKey(inputId);
            
            Account acc = null;
            
            if (keyExist) {
                acc = dao.getAccount(inputId);
                
                if (inputPw.equals(acc.getPassword())) {
                    //System.out.println("insert");
                    dos.writeUTF("LOGINSUCCESS");
                    fileAccess(inputId);
                    
                } else {
                    dos.writeUTF("LOGINERROR");
                    trials++;
                }
                
            } else {
                trials++;
            }
            
        }
//        dos.writeUTF("You failed the 3 trials, the connection will be terminated!");
//        disconnect();
        menu = "LOGINERROR*"
                + "1. New User\n"
                + "2. Existing User\n"
                + "3. Disconnect";
        
    }
    
    public void disconnect() throws IOException {
        this.socket.close();
    }
    
    public void fileAccess(String inputId) throws IOException {
        String input;
        Account acc = null;
        
        acc = dao.getAccount(inputId);
        String fileList = acc.getfiles();
        String menu = "1. Download\n"
                + "2. Upload\n"
                + "3. File List\n"
                + "4. Disconnect";
        boolean filedownload = true;
        //dos.writeUTF("Login successfull!");
        
        while (filedownload) {

            //String fileList=acc.getfiles();
            dos.writeUTF(fileList + menu);
            
            input = dis.readUTF();
            
            switch (input) {
                case "1":
                    if (!download(inputId)) {
                        fileList = "FILEDOWNLOADFAILED*";
                    } else {
                        fileList = "";
                    }
                    ;
                    break;
                case "2":
                    upload(inputId);
                    break;
                case "3":
                    fileList(inputId);
                    break;
                case "4":
                    disconnect();
                    break;
                
            }
        }        
        
    }
    
    public boolean download(String inputId) throws IOException {
        String fileName;
        DFile file;
        dos.writeUTF("Enter the name of the file to download: ");
        
        fileName = dis.readUTF();
        
        file = dao.getFileDao().getFile(fileName);
        
        if (file == null) {

            //dos.writeUTF("FILEDOWNLOADFAILED");
            return false;
        } else {
            dos.writeUTF("FOUND");
            if (dis.readUTF().equals("READY")) {
                dos.writeUTF(file.getContents());
            }
            if (dis.readUTF().equals("DOWNLOADCOMPLETED")) {
                return true;                
            }
            
        }
        return true;
        
    }
    
    public void upload(String id) throws IOException {
        dos.writeUTF("READY");
        
        String fileContent;
        dos.writeUTF("Enter filename and contents separated by *: ");
        
        fileContent = dis.readUTF();
        
        dao.uploadFile(fileContent, id);
        dos.flush();
    }
    
    public void fileList(String id) throws IOException {
        String fileList = dao.getAccount(id).getfiles();
        dos.writeUTF(fileList);
    }
}
