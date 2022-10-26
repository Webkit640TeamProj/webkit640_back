package com.example.webkit640.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *  NOT USE - writer 송민규
 */
@Slf4j
public class Ziptest {
    public String filesToZip() {
        File deleteFile = new File("/Users/songmingyu/Webkit/WebkitApplicant.zip");
        if (deleteFile.exists()) {
            deleteFile.delete();
        }
        String where = "/Users/songmingyu/Webkit/";
        File file_ = new File(where);
        File[] fileList = file_.listFiles();

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        try {
            fos = new FileOutputStream(where + "WebkitApplicant.zip");
            zipOut = new ZipOutputStream(fos);

            for(File fileToZip : fileList) {
                fis = new FileInputStream(fileToZip);
                for(File temp : fileList) {
                    log.info(temp.getName());
                }
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[104857600];
                int length;
                while((length = fis.read(bytes))>= 0) {
                    zipOut.write(bytes, 0, length);
                }

                fis.close();
                zipOut.closeEntry();
            }

            zipOut.close();
            fos.close();
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            try { if(fis != null)fis.close(); } catch (IOException e1) {System.out.println(e1.getMessage());/*ignore*/}
            try { if(zipOut != null)zipOut.closeEntry();} catch (IOException e2) {System.out.println(e2.getMessage());/*ignore*/}
            try { if(zipOut != null)zipOut.close();} catch (IOException e3) {System.out.println(e3.getMessage());/*ignore*/}
            try { if(fos != null)fos.close(); } catch (IOException e4) {System.out.println(e4.getMessage());/*ignore*/}
        }
        return where + "WebkitApplicant.zip";
    }
    public File zipDownloader(String path, ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource(path);
            return resource.getFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
