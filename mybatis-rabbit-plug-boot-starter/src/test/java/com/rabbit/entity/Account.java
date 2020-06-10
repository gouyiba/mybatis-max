package com.rabbit.entity;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.typehandler.IEnumTypeHandler;
import com.rabbit.enumatioon.Sex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Account extends BaseEntity {
    private String userName;

    private String pass;

    @Column(typeHandler = IEnumTypeHandler.class)
    private Sex sex;
}
