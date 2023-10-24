package com.example.demo.domain.worldcup.infra;

import com.example.demo.domain.worldcup.model.repository.WorldCupGameQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WorldCupGameRepositoryImpl implements WorldCupGameQueryRepository {
    @Autowired
    private EntityManager em;
}
