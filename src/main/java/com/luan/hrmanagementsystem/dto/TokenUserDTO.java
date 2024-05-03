package com.luan.hrmanagementsystem.dto;

import lombok.Data;

@Data
public class TokenUserDTO {
    private Long id;
    private Boolean is_logged_out;
    private Long user_id;
    private String username;
    private String token;
}
