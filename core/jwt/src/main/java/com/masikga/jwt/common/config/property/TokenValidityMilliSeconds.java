package com.masikga.jwt.common.config.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidityMilliSeconds {
    Long access;
    Long refresh;
}
