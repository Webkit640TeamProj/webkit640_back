package com.example.webkit640.service;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileService {
    @Value("${file.dir}")
    private String fileDir;
    @Value("${file.imageUploadPath}")
    private String imageUploadPath;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    ResourceLoader resourceLoader;
    public void modifyBoardFile(FileEntity fileEntity) {
        fileRepository.save(fileEntity);
    }

    public File multipartToFile(MultipartFile file) {
        try {
            File resultFile = new File(file.getOriginalFilename());
            file.transferTo(resultFile);
            return resultFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public FileEntity saveFile(MultipartFile files, Applicant applicant, Member member) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        File Folder = new File(fileDir);
        LocalDate ld = LocalDate.now();
        String year = Integer.toString(ld.getYear());
        File yearFolder = new File(fileDir+year);
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
                yearFolder.mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        if (!yearFolder.exists()) {
            try {
                yearFolder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info(fileDir);
        String originalName = files.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = applicant.getMember().getEmail() + "_apply_" + extension;
        String savedPath = fileDir+year+"/" + savedName;
        FileEntity dbFile = FileEntity.builder()
                .applicant(applicant)
                .fileExtension(extension)
                .fileName(savedName)
                .filePath(fileDir+year+"/")
                .fileType("APPLY")
                .member(member)
                .build();
        files.transferTo(new File(savedPath));
        FileEntity savedFile = fileRepository.save(dbFile);
        return savedFile;
    }

    public FileEntity findByMemberId(Member member) {

        List<FileEntity> files = fileRepository.findByMember_Id(member.getId());
        FileEntity returnFile = null;
        for (FileEntity file : files) {
            if (file.getFileType().equals("APPLY")) {
                returnFile = file;
            }
        }
        return returnFile;
    }

    public String filesToZip() {
        String year = Integer.toString(LocalDate.now().getYear()) + "/";
        File deleteFile = new File(fileDir+year+"WebkitApplicant.zip");
        if (deleteFile.exists()) {
            deleteFile.delete();
        }

        String where = fileDir+year;
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

    public FileEntity saveBoardFile(MultipartFile files, Board board, Member member) throws IOException {
        if (files.isEmpty()) {
            return null;
        }
        File Folder = new File(fileDir);
        LocalDate ld = LocalDate.now();
        String year = Integer.toString(ld.getYear());
        File yearFolder = new File(fileDir+year+"/board");
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
                yearFolder.mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        if (!yearFolder.exists()) {
            try {
                yearFolder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info(fileDir);
        String originalName = files.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedPath = fileDir+year+"/board/" + originalName;
        FileEntity dbFile = FileEntity.builder()
                .board(board)
                .fileExtension(extension)
                .fileName(originalName)
                .filePath(fileDir+year+"/board/")
                .fileType("BOARD")
                .member(member)
                .build();
        files.transferTo(new File(savedPath));
        FileEntity savedFile = fileRepository.save(dbFile);
        return savedFile;
    }

    public List<FileEntity> findByBoardId(Board board) {
        List<FileEntity> files = fileRepository.findByBoard_Id(board.getId());
        return files;
    }

    public String saveImage(MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        File Folder = new File(imageUploadPath);
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,_".split(",");

        for (int i=0 ; i<50; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }

        log.info(imageUploadPath);
        String originalName = files.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = buffer.toString() + extension;
        String savedPath = imageUploadPath + savedName;

        files.transferTo(new File(savedPath));

        return "/imagePath/"+savedName;
    }
}
