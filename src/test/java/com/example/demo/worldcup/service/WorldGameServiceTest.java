package com.example.demo.worldcup.service;

import com.example.demo.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.worldcup.model.WorldCupGameRepository;
import com.example.demo.worldcup.service.dto.WorldCupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class WorldGameServiceTest {

    @InjectMocks
    private WorldCupGameService sut;

    @Mock
    private WorldCupGameRepository worldCupGameRepository;

    @Test
    @DisplayName("월드컵 리스트 조회 - 성공")
    public void 월드컵_리스트_조회_성공() {
        WorldCupDateRange dateRange = WorldCupDateRange.MONTH;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        String worldCupGameKeyword = "TestKeyword";
        Pageable pageable = Pageable.ofSize(25);

        WorldCupResponse response = sut.findWorldCupByPageable(pageable, dateRange, worldCupGameKeyword);
        then(worldCupGameRepository)
                .should(times(1))
                .findWorldCupGamePage(worldCupGameKeyword, startDate, endDate, pageable);
    }
}
