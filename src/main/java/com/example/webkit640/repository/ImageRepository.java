package com.example.webkit640.repository;

import com.example.webkit640.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findById(int imageId);

    List<Image> findAll();
}
