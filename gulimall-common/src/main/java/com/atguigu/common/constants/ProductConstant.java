package com.atguigu.common.constants;

/**
 * ClassName: ProductConstant
 * Description:
 *
 * @Create 2024/8/3 上午1:19
 */
public class ProductConstant {
    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本屬性"),ATTR_TYPE_SALE(0,"銷售屬性");
        private int code;
        private String msg;

        AttrEnum(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
