package com.masikga.worldcupgame.domain.etc.repository;

import com.masikga.worldcupgame.domain.etc.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractMediaFileRepository extends JpaRepository<MediaFile, Long> {
}
