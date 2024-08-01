package com.atguigu.common.exception;

/**
 * ClassName: BizCodeEnume
 * Description:
 *
 * @Create 2024/7/31 上午2:38
 */
public enum BizCodeEnume {
    UNKNOWN_EXCEPTION(10000,"系統未知異常"),
    VALID_EXCEPTION(10001,"參數格式校驗失敗");
    private int code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}
