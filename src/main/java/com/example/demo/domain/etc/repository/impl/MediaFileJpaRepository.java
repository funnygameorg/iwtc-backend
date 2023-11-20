package com.example.demo.domain.etc.repository.impl;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.model.StaticMediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaFileJpaRepository extends JpaRepository<MediaFile, Long> {
}
