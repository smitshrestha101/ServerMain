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

    public Handler(Socket socket, DataInputStream dis, DataOutputStream dos, Dao dao) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        this.dao = dao;

    }

    @Override
    public void run() {
        String receive;
        while (true) {
            try {
                dos.writeUTF("Select an option:\n"
                        + "1. New User\n"
                        + "2. Existing User\n"
                        + "3. Disconnect");

                receive = dis.readUTF();

                switch (receive) {
                    case "1":
                        newUser();
                        break;
                    case "2":
                        existingUser();
                        break;
                    case "3":
                        disconnect();

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public void newUser() throws IOException {
        String input;
        Helper help = new Helper();

        boolean exists = true;
        String id = "";
        String pw = "";
        while (exists == true) {
            dos.writeUTF("Enter new id and password separated by *: ");

            input = dis.readUTF();

            String[] idpw = input.split("*");
            id = idpw[0];
            pw = idpw[1];

            boolean keyExist = dao.getMap().containsKey(id);
            if (keyExist = false) {
                exists = false;
            }
        }

        dao.addUser(id, pw);

    }

    public void existingUser() throws IOException {
        String inputId, inputPw;
        int trials = 0;

        if (trials < 3) {

            dos.writeUTF("Enter id: ");

            inputId = dis.readUTF();

            dos.writeUTF("Enter Password: ");
            inputPw = dis.readUTF();

            boolean keyExist = dao.getMap().containsKey(inputId);

            Account acc = null;

            if (keyExist) {
                acc = dao.getAccount(inputId);

                if (inputPw.equals(acc.getPassword())) {
                    fileAccess(inputId);

                } else {
                    dos.writeUTF("UserID and Password do not match.");
                    trials++;
                }

            }

        } else {
            dos.writeUTF("You failed the 3 trials, the connection will be terminated!");
            disconnect();
        }

    }

    public void disconnect() throws IOException {
        this.socket.close();
    }

    public void fileAccess(String inputId) throws IOException {
        String input;
        dos.writeUTF("Login successfull!");
        
        Account acc=null;
        
        acc=dao.getAccount(inputId);
        
        String fileList=acc.getfiles();
        
        dos.writeUTF(fileList);
        dos.writeUTF("Select an option:\n"
                + "1. Download\n"
                + "2. Upload\n"
                + "3. File List\n"
                + "4. Disconnect");
        
        input=dis.readUTF();
        
        
    }
}
