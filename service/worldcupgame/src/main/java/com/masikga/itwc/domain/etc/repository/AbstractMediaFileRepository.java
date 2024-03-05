package com.masikga.itwc.domain.etc.repository;

import com.masikga.itwc.domain.etc.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractMediaFileRepository extends JpaRepository<MediaFile, Long> {
}
