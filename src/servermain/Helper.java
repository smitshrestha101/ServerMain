/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smits
 */
public class Helper {

    public void write(String fileName, String toWrite) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(toWrite);

        writer.close();
    }
    
    public void update(String fileName,String toWrite) throws IOException{
        File file=new File(fileName);
        FileWriter fw =new FileWriter(file.getAbsoluteFile(),true);
        BufferedWriter bw=new BufferedWriter(fw);
        
        bw.write(toWrite);
        
        bw.close();
        fw.close();
    }

    public List<String> read(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        List<String> list = new ArrayList<>();
        
        while ((line = reader.readLine()) != null) {
            
           list.add(line);
        }
        return list;

    }

}
