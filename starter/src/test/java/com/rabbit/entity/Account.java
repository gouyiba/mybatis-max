package com.rabbit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account extends BaseEntity {
    private String userName;

    private String pass;
}
