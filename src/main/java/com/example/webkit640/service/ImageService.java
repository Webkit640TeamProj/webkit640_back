package com.example.webkit640.service;

import com.example.webkit640.entity.Image;
import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
@Slf4j
public class ImageService {

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    ResourceLoader resourceLoader;

    public Image getImageId (int imageId){ return imageRepository.findById(imageId); }

    public void deleteImage(int id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("id does not exist");
        }
        return;
    }

    public Image ImageSave(MultipartFile file, Member member) throws IOException {
        String where = fileDir+"image";
        File file_ = new File(where);
        if (!file_.exists()) {
            log.info("SERVER : NOT EXIST DIRECTORY, make dir");
            file_.mkdir();
            File memberDir = new File(where+"/"+member.getEmail());
            memberDir.mkdir();
        }
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        if (!(extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".gif"))) {
            return null;
        }
        String savedName = member.getEmail() + "_image_"+ LocalDate.now() +extension;
        String savedPath = where + "/" + member.getEmail() + "/" + savedName;

        file.transferTo(new File(savedPath));
        log.info("SERVER : Save FileEntity");

        return imageRepository.save(Image.builder()
                .member(member)
                .imageName(savedName)
                .imageExtension(extension)
                .imagePath(where + "/" + member.getEmail())
                .build());
    }

}
