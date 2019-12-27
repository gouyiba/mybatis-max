package com.rabbit.core.enumation;

public enum Sex implements IEnum<Integer>{
    MAN(1),
    WOMEN(2);

    private Integer sex;

    private Sex(Integer sex){
        this.sex=sex;
    }

    @Override
    public Integer getValue() {
        return this.sex;
    }
}
