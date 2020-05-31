package com.rabbit.entity;

import com.rabbit.core.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;

@Getter
@Setter
@ToString
public class SuperEntity {
    @Id
    private String id;

    @Transient
    private String value;
}
