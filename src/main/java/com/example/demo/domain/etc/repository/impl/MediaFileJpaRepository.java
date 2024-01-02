package com.example.demo.domain.etc.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.etc.model.MediaFile;

public interface MediaFileJpaRepository extends JpaRepository<MediaFile, Long> {
}
