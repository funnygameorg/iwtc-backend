package com.example.demo.domain.etc.repository;

import com.example.demo.domain.etc.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaFileJpaRepository extends JpaRepository<MediaFile, Long> {
}
