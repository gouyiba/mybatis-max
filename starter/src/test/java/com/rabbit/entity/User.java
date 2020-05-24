package com.rabbit.entity;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import com.rabbit.core.typehandler.IEnumTypeHandler;
import com.rabbit.enumatioon.Sex;
import lombok.Data;

@Data
@Table("t_user")
public class User extends BaseBean {

    private String id;

    @Id
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    @Column(typeHandler = IEnumTypeHandler.class)
    private Sex sex;

    //private String type;
}
