package com.example.demo.worldcup.service;

import com.example.demo.domain.worldcup.model.entity.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.model.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class WorldCupGameServiceTest {

    @InjectMocks
    private WorldCupGameService worldCupGameService;

    @Mock
    private WorldCupGameRepository worldCupGameRepository;

    @Test
    @DisplayName("월드컵 리스트 조회 - 성공")
    public void 월드컵_리스트_조회_성공() {
        WorldCupDateRange dateRange = WorldCupDateRange.YEAR;
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        String worldCupGameKeyword = "TestKeyword";
        Pageable pageable = Pageable.ofSize(25);

        Page<GetWorldCupGamePageProjection> response = worldCupGameService.findWorldCupByPageable(
                pageable,
                dateRange,
                worldCupGameKeyword
        );

        then(worldCupGameRepository)
                .should(times(1))
                .getWorldCupGamePage(startDate, endDate, pageable);
    }
}
