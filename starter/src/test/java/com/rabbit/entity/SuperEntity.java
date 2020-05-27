package com.rabbit.entity;

import com.rabbit.core.annotation.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperEntity {
    @Id
    private String id;
}
