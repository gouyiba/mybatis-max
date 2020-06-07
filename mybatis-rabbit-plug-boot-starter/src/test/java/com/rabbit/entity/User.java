package com.rabbit.entity;

import com.rabbit.core.annotation.Column;
import com.rabbit.core.annotation.Id;
import com.rabbit.core.annotation.Table;
import com.rabbit.core.enumation.PrimaryKey;
import com.rabbit.core.typehandler.IEnumTypeHandler;
import com.rabbit.enumatioon.Sex;
import lombok.Data;

@Data
@Table("t_user")
public class User extends BaseBean {

    private String id;

    @Id(isKeyGenerator = true,generateType = PrimaryKey.UUID32)
    private String stuUid;

    private String stuName;

    private Integer stuAge;

    @Column(typeHandler = IEnumTypeHandler.class)
    private Sex sex;

    public User(String stuUid,String stuName,Integer stuAge,Sex sex){
        this.stuUid=stuUid;
        this.stuName=stuName;
        this.stuAge=stuAge;
        this.sex=sex;
    }

    public User(){}
}
