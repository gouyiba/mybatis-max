package com.rabbit.starter.bean;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import com.rabbit.core.constructor.BaseAbstractWrapper;
import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.starter.typehandler.IEnumTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试User-bean
 */
@Data
@Table(value = "t_user")
public class User implements Serializable {

    @Id(isIncrementColumn = true)
    private Integer id;

    //@Id
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    // 自定义枚举转换器有问题，还需改进
    //@Column(typeHandler = IEnumTypeHandler.class,columnType = MySqlColumnType.TINYINT)
    private Integer sex;

    private Date createDate;

    private Date updateDate;

    private Integer delFlag;
}
