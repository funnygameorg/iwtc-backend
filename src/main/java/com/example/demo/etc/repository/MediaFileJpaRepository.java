package com.example.demo.etc.repository;

import com.example.demo.etc.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaFileJpaRepository extends JpaRepository<MediaFile, Long> {
}
