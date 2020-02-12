package com.rabbit.starter.enumation;

import com.rabbit.core.enumation.IEnum;

public enum Sex implements IEnum<Integer> {

    WOMEN(1),
    MAN(2);

    private Integer val;

    private Sex(Integer val){
        this.val=val;
    }

    /**
     * 获取value对应枚举
     *
     * @param value
     * @return
     */
    public static Sex valueConvertEnum(Integer value) {
        for (Sex sex : Sex.values()) {
            if (sex.val==value) {
                return sex;
            }
        }
        return null;
    }

    @Override
    public Integer getValue() {
        return this.val;
    }
}
