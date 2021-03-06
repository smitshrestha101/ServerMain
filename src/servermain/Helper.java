/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smits
 */
public class Helper {

    //TO WRITE INTO FILES
    public void write(String fileName, String toWrite) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        writer.print(toWrite);

        writer.close();
    }

    //TO ADD WRITE FILE IN SPECIFIC FOLDER
    public void writeFile(String fileName, String toWrite, String username) throws IOException {
        File directory = new File(System.getProperty("user.dir") + "\\files\\" + username + "\\");
        if (!directory.exists()) {
            directory.mkdir();

        }
        File fullPath = new File(directory, fileName);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath)));
        writer.write(toWrite);

        writer.close();
    }

    //TO UPDATE THE FILES
    public void update(String fileName, String toWrite) throws IOException {
        File file = new File(fileName);
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(toWrite);

        bw.close();
        fw.close();
    }

    //TO READ THE FILES TO A LIST
    public List<String> read(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        List<String> list = new ArrayList<>();

        while ((line = reader.readLine()) != null) {

            list.add(line);
        }
        return list;

    }

    //TO READ ALL THE FILES
    public String readFiles(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = "";
        String content = "";

        while ((line = reader.readLine()) != null) {
            content += line;
        }
        return content;
    }

}
