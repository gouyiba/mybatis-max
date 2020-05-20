package com.rabbit.core.entity;

import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import lombok.Data;

@Data
@Table("t_user")
public class User extends BaseBean{

    private String id;

    @Id
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    private String type;
}
