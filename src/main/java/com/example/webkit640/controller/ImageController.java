package com.example.webkit640.controller;

import com.example.webkit640.dto.response.BoardImageResponseDTO;
import com.example.webkit640.entity.Image;
import com.example.webkit640.service.FileService;
import com.example.webkit640.service.ImageService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"})
@RequestMapping("/image")
@Slf4j
@RestController
public class ImageController {

    private final MemberService memberService;

    private final FileService fileService;

    private final ImageService imageService;

    public ImageController(MemberService memberService, FileService fileService, ImageService imageService) {
        this.memberService = memberService;
        this.fileService = fileService;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal int id , @RequestParam MultipartFile image) {
        try {
            log.info("ENTER /image/upload - Accessor : " + memberService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            log.info("USER EXCEPTION /image/upload - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body("USER EXCEPTION /image/upload - Accessor : "+memberService.findByid(id).getEmail());
        }

        try{
            String img = fileService.saveImage(image);
            log.info("LEAVE /image/upload - Accessor : " + memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(img);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveImage(@AuthenticationPrincipal int id, @RequestParam("image")MultipartFile image) {
        log.info("ENTER USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
        try {
            Image img = imageService.ImageSave(image, memberService.findByid(id));
            if (img != null) {
                BoardImageResponseDTO res = BoardImageResponseDTO.builder().path(img.getImagePath()+"/"+img.getImageName()).build();
                log.info("LEAVE USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
                return ResponseEntity.ok().body(res);
            } else {
                log.error("EXCEPTION USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
                throw new RuntimeException();
            }
        } catch (IOException ie) {
            log.error("EXCEPTION USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
            log.error("EXCEPTION : "+ie.getStackTrace());
            throw new RuntimeException();
        }
    }


    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal int id, @PathVariable("imageId") int imageId ) {
        Image image = imageService.getImageId(imageId);
        if(image.getMember().getId() == id) {
            imageService.deleteImage(imageId);
        }
        return ResponseEntity.ok().body("delete ok");
    }


}
