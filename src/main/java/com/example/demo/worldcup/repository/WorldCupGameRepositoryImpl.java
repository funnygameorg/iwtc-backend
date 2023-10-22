package com.example.demo.worldcup.repository;

import com.example.demo.worldcup.model.WorldCupGameQueryRepository;
import com.example.demo.worldcup.model.projection.FindWorldCupGamePageProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class WorldCupGameRepositoryImpl implements WorldCupGameQueryRepository {
    @Autowired
    private EntityManager em;
}
