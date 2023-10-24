package com.example.demo.domain.worldcup.infra;

import com.example.demo.domain.worldcup.model.entity.WorldCupGame;
import com.example.demo.domain.worldcup.model.projection.FindWorldCupGamePageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface WorldCupGameJpaRepository extends JpaRepository<WorldCupGame, Long> {

    @Query(
            value = "SELECT wcg.id AS id, wcg.title AS title, wcg.description AS description, " +
                    "wcgc_max.name AS contentsName1, mf_max.file_path AS filePath1, " +
                    "wcgc_min.name AS contentsName2, mf_min.file_path AS filePath2 " +
                    "FROM world_cup_game AS wcg " +
                    "INNER JOIN world_cup_game_contents AS wcgc_max ON wcgc_max.id = (" +
                        "SELECT MAX(inner_wcgc.id) AS id " +
                        "FROM world_cup_game_contents AS inner_wcgc " +
                        "WHERE inner_wcgc.world_cup_game_id = wcg.id " +
                        "AND inner_wcgc.id IN (" +
                            "SELECT inner_wcgc_2.id " +
                            "FROM world_cup_game_contents AS inner_wcgc_2 " +
                            "WHERE inner_wcgc_2.world_cup_game_id = wcg.id " +
                            "ORDER BY inner_wcgc_2.id DESC " +
                            "LIMIT 2" +
                        ")" +
                    ") " +
                    "INNER JOIN world_cup_game_contents AS wcgc_min ON wcgc_min.id = (" +
                        "SELECT MIN(inner_wcgc.id) AS id " +
                        "FROM world_cup_game_contents AS inner_wcgc " +
                        "WHERE inner_wcgc.world_cup_game_id = wcg.id " +
                        "AND inner_wcgc.id IN (" +
                            "SELECT inner_wcgc_2.id " +
                            "FROM world_cup_game_contents AS inner_wcgc_2 " +
                            "WHERE inner_wcgc_2.world_cup_game_id = wcg.id " +
                            "ORDER BY inner_wcgc_2.id DESC " +
                            "LIMIT 2" +
                        ")" +
                    ") " +
                    "INNER JOIN media_file AS mf_max on wcgc_max.media_file_id = mf_max.id " +
                    "INNER JOIN media_file AS mf_min on wcgc_min.media_file_id = mf_min.id " +
                    "WHERE DATE(wcg.created_at) BETWEEN :startDate AND :endDate " +
                    "GROUP BY wcg.id " +
                    "ORDER BY wcg.id desc ",
            countQuery = "SELECT COUNT(id) FROM world_cup_game",
            nativeQuery = true
    )
    Page<FindWorldCupGamePageProjection> findWorldCupGamePage(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
            );
}
