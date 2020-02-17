package com.rabbit.starter.bean;

import com.rabbit.core.annotation.Create;
import com.rabbit.core.annotation.Delete;
import com.rabbit.core.annotation.FillingStrategy;
import com.rabbit.core.annotation.Update;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 公用字段类，存放所有bean的公用字段,子类继承
 * explain:
 * 1.需要将自定义的公用字段类注入到spring中
 * 2.使用@FillingStrategy进行标注声明该类是一个自定义公用字段类
 */
@Data
@Component
@FillingStrategy
public class BaseBean implements Serializable {

    private Date createDate;

    private Date updateDate;

    @Delete(physicsDel = false,value = 5)
    private Integer delFlag;

    /**
     * 新增时，字段填充策略
     */
    @Create
    public void add(){
        this.createDate=new Date();
        this.delFlag=1;
    }

    /**
     * 修改时，字段填充策略
     */
    @Update
    public void update(){
        this.updateDate=new Date();
    }

}
