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
    String menu = "1. New User "
            + "2. Existing User "
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
                    case "1":                       //NEW USER
                        loop=!newUser();                      
                        if(!loop){
                            disconnect();
                        }
                        break;
                    case "2":                       //EXISTING USER
                        loop = !existingUser();
                        if(!loop){
                            disconnect();
                        }
                        break;
                    case "3":                       //DISCONNECT
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

    public boolean newUser() throws IOException {
        String input = "";
        Helper help = new Helper();

        boolean exists = true;
        String id = "";
        String pw = "";
        dos.writeUTF("READY");
        while (exists == true) {
            
            dos.writeUTF("READY*Enter new id and password separated by *: ");

            input = dis.readUTF();
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
        dao.addUser(id, pw);

        return fileAccess(id);

    }

    public boolean existingUser() throws IOException {
        String inputId, inputPw, inputIdPw;
        boolean exit = false;
        int trials = 0;
        dos.writeUTF("READY");

        while (trials < 3 && !exit) {

            dos.writeUTF("Enter id and password separated by *: ");
            inputIdPw = dis.readUTF();

            String[] input = inputIdPw.split("\\*");
            inputId = input[0];
            inputPw = input[1];

            boolean keyExist = dao.getMap().containsKey(inputId);

            Account acc = null;

            if (keyExist) {
                acc = dao.getAccount(inputId);

                if (inputPw.equals(acc.getPassword())) {

                    dos.writeUTF("LOGINSUCCESS");
                    exit = fileAccess(inputId);

                } else {
                    dos.writeUTF("LOGINERROR");
                    trials++;
                }

            } else {
                dos.writeUTF("LOGINERROR");
                trials++;
            }

        }
        

        menu = "LOGINERROR*"
                + "1. New User "
                + "2. Existing User "
                + "3. Disconnect";
        return exit;

    }

    public void disconnect() throws IOException {
       
        this.socket.close();
        dao.decreaseTcount();
    }

    public boolean fileAccess(String inputId) throws IOException {
        String input;
        Account acc = null;

        acc = dao.getAccount(inputId);
        String fileList = acc.getfiles() + "*";
        String menu = "1. Download "
                + "2. Upload "
                + "3. File List "
                + "4. Disconnect";
        boolean filedownload = true;

        while (filedownload) {

            dos.writeUTF(fileList + menu);

            input = dis.readUTF();

            switch (input) {
                case "1":
                    if (!download(inputId)) {
                        fileList = "FILEDOWNLOADFAILED*";
                    } else {
                        fileList = "";
                    }
                    
                    break;
                case "2":
                    upload(inputId);
                    fileList = "";
                    break;
                case "3":
                    fileList(inputId);
                    fileList = "";
                    
                    break;
                case "4":
                    filedownload=false;
                    return true;

            }
        }
        return false;
    }

    public boolean download(String inputId) throws IOException {
        String fileName;
        
        dos.writeUTF("Enter the name of the file to download: ");

        fileName = dis.readUTF();
        DFile file = dao.getFileDao().getFile(fileName);
        String[] fileList=dao.getAccount(inputId).getfiles().split(" ");
        for (String currFile : fileList){
            if(!currFile.equals(fileName)){
                file=null;
            }
            
        }

        if (file == null) {
                
            dos.writeUTF("FILEDOWNLOADFAILED");
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

        String fileName = "";

        dos.writeUTF("Enter file name: ");

        fileName = dis.readUTF();
        if (fileName.equals("ERROR")) {
            //break;
        } else {
            fileName = dao.verifiedString(fileName);

            dos.writeUTF("CONTINUE");
            dos.writeUTF("Enter contents of the file: ");
            String fileContent;

            fileContent = dis.readUTF();

            dao.uploadFile(fileName, fileContent, id);
            dos.flush();
        }
    }

    public void fileList(String id) throws IOException {
        String fileList = dao.getAccount(id).getfiles();
        dos.writeUTF(fileList);
        dis.readUTF();

    }
}
