package com.example.teachingapp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String mail;
    private String password;
    private boolean isStudent;
}
