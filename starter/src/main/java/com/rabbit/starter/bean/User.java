package com.rabbit.starter.bean;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import com.rabbit.core.constructor.BaseAbstractWrapper;
import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.enumation.Sex;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试User-bean
 */
@Data
@Table(value = "t_user")
public class User implements Serializable {

    @Id(generateType = PrimaryKey.OBJECTID)
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    @Column(typeHandler = BaseAbstractWrapper.class,columnType = MySqlColumnType.TINYINT)
    private Sex sex;

    private Date createDate;

    private Date updateDate;

    private Integer delFlag;
}
