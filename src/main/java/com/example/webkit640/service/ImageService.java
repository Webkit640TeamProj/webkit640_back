package com.example.webkit640.service;

import com.example.webkit640.entity.Image;
import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    ResourceLoader resourceLoader;

    public Image getImageId (int imageId){ return imageRepository.findById(imageId); }

    public List<Image> getImageAll() {
        return imageRepository.findAll();
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImage(int id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("id does not exist");
        }
        return;
    }

    public Image ImageSave(String imagePath, Member member, String title) throws IOException {

        return imageRepository.save(Image.builder()
                .member(member)
                .title(title)
                .imagePath(imagePath)
                .build());
    }

}
