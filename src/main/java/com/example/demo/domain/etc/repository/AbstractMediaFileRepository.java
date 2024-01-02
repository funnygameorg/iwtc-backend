package com.example.demo.domain.etc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.etc.model.MediaFile;

public interface AbstractMediaFileRepository extends JpaRepository<MediaFile, Long> {
}
