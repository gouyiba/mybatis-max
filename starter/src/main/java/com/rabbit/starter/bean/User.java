package com.rabbit.starter.bean;

import cn.hutool.json.JSONUtil;
import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import com.rabbit.core.constructor.BaseAbstractWrapper;
import com.rabbit.core.constructor.QueryWrapper;
import com.rabbit.core.enumation.MySqlColumnType;
import com.rabbit.core.enumation.PrimaryKey;
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

    public static void main(String[] args) {
        QueryWrapper queryWrapper=new QueryWrapper(new User());
        /*queryWrapper.where("stu_name","马化腾");
        queryWrapper.where("stu_age",20);
        queryWrapper.or("sex",1);
        queryWrapper.between("stu_age",20,40);
        queryWrapper.like("stu_name","杜晓宇");
        queryWrapper.in("stu_age",12,30,40,50);
        queryWrapper.isNotNull("sex");
        queryWrapper.isNull("sex");
        queryWrapper.isNotNullOrEqual("stu_name");
        queryWrapper.isNullOrEqual("stu_name");
        queryWrapper.notEqual("sex",80);
        queryWrapper.notIn("stu_age",90,80,70);
        queryWrapper.orderBy("stu_age","desc");
        queryWrapper.setColumn("stu_uid,stu_name,stu_age");
        Map<String,Object> sqlMap=queryWrapper.mergeSqlMap();*/

        queryWrapper.where("stu_age",1001).where("sex",1).like("stu_name","马");
        queryWrapper.mergeSqlMap();
    }
}
