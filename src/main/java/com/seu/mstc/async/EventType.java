package com.seu.mstc.async;

//本类用于枚举各个事件的类型
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    DELETE(4),
    CALCULATE(5),
    RANKSCORE(6);

    private int value;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
