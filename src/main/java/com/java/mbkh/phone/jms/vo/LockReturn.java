package com.java.mbkh.phone.jms.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xdd
 * @date 2018/8/20
 */
public class LockReturn implements java.io.Serializable{

    private static final long serialVersionUID = -5419801621969793692L;

    @JSONField(name = "UID")
    private String uid;
    @JSONField(name = "TYPE")
    private String type;
    @JSONField(name = "RET")
    private String ret;

    public LockReturn(String uid, String type, String ret) {
        this.uid = uid;
        this.type = type;
        this.ret = ret;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}