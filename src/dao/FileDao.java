/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import servermain.DFile;

/**
 *
 * @author smits
 */
public interface FileDao {
    
    public DFile getFile(String fileName);
    public void addFile(String fileName, String fileContent);
    public void createFileList();
}