package com.rabbit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEntity extends SuperEntity{
    private String createdBy;
}
