package com.rabbit.entity;

import com.rabbit.core.annotation.Create;
import com.rabbit.core.annotation.Delete;
import com.rabbit.core.annotation.Update;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * example:
 * 公用字段类需要自行创建，存放所有bean的公用字段,子类继承
 *
 * @author duxiaoyu
 * @date 2020-02-05
 */
@Data
public class BaseBean implements Serializable {

    private Date createDate;

    private Date updateDate;

    @Delete(physicsDel = false,value = 6)
    private Integer delFlag;

    /**
     * 新增时，字段填充策略
     */
    @Create
    public void add() {
        this.createDate = new Date();
        this.delFlag = 1;
    }

    /**
     * 修改时，字段填充策略
     */
    @Update
    public void update() {
        this.updateDate = new Date();
    }

}
