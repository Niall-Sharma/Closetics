package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findById(int id);
}
