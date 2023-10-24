package com.example.demo.domain.worldcup.model.repository;

import com.example.demo.domain.worldcup.model.projection.GetWorldCupGamePageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface WorldCupGameQueryRepository {

    Page<GetWorldCupGamePageProjection> getWorldCupGamePage(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}
