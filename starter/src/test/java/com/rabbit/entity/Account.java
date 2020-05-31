package com.rabbit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Account extends BaseEntity {
    private String userName;

    private String pass;
}
