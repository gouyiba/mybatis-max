package com.rabbit.enumatioon;

import com.rabbit.core.enumation.IEnum;

/**
 * @ClassName Sex
 * @ClassExplain: 说明
 * @Author Duxiaoyu
 * @Date 2020/5/24 10:50
 * @Since V 1.0
 */
public enum Sex implements IEnum<Integer> {
    MAX(1),
    WOMEN(2);

    private Integer value;

    Sex(Integer value){
        this.value=value;
    }

    public static Sex valueConvertEnum(Integer value) {
        for (Sex sex : Sex.values()) {
            if (sex.value.equals(value)) {
                return sex;
            }
        }
        return null;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
