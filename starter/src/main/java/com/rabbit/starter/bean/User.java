package com.rabbit.starter.bean;

import cn.hutool.json.JSONUtil;
import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Delete;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import com.rabbit.core.constructor.BaseAbstractWrapper;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.starter.enumation.Sex;
import com.rabbit.starter.typehandler.IEnumTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 测试User-bean
 */
@Data
@Table(value = "t_user")
public class User extends BaseBean implements Serializable {

    //@Id(isIncrementColumn = true)
    private Integer id;

    @Id
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    @Column(typeHandler = IEnumTypeHandler.class,columnType = MySqlColumnType.TINYINT)
    private Sex sex;

    //@Delete(physicsDel = false,value = 5)
    //private Integer delFlag;

    /*private Date createDate;

    private Date updateDate;

    private Integer delFlag;*/
}
