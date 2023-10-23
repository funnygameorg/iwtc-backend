package com.example.demo.worldcup.model.projection;

import com.example.demo.worldcup.controller.dto.response.GetWorldCupsResponse;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.Optional;

public interface FindWorldCupGamePageProjection {
    Long getId();
    String getTitle();
    String getDescription();
    String getContentsName1();
    String getFilePath1();
    String getContentsName2();
    String getFilePath2();
}