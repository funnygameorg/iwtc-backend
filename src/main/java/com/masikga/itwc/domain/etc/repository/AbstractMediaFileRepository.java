package com.masikga.itwc.domain.etc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masikga.itwc.domain.etc.model.MediaFile;

public interface AbstractMediaFileRepository extends JpaRepository<MediaFile, Long> {
}
