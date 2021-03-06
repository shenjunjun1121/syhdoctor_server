package com.syhdoctor.common.utils.EnumUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息类型
 */
public enum MessageTypeEnum implements CodeEnum {

    user(1, "用户"),
    doctor(2, "医生"),
    ;

    MessageTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public static MessageTypeEnum getValue(int code) {
        for (MessageTypeEnum c : MessageTypeEnum.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return MessageTypeEnum.user;
    }


    public static List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MessageTypeEnum c : MessageTypeEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getCode());
            map.put("value", c.getMessage());
            list.add(map);
        }
        return list;
    }
}




