package com.example.demo.domain.worldcup.repository;

import com.example.demo.domain.worldcup.model.WorldCupGameQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WorldCupGameRepositoryImpl implements WorldCupGameQueryRepository {
    @Autowired
    private EntityManager em;
}
