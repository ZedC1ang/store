package top.zeds1aw.store.common.exception;

/**
 * 描述：     异常枚举
 */
public enum StoreExceptionEnum {

    REQUEST_PARAM_ERROR(10001, "参数有误，请重试"),
    NO_ENUM(10002, "找不到对应枚举"),
    NEED_USER_NAME(10003, "请输入用户名"),
    NEED_PASSWORD(10004, "请输入密码"),
    PASSWORD_TOO_SHORT(10005, "密码长度不能少于8位"),
    NAME_EXISTED(10006, "不允许重名"),
    INSERT_FAILED(10007, "注册失败，请重试"),
    WRONG_PASSWORD(10008, "用户名或密码错误"),
    UPDATE_FAILED(10009, "更新个性签名失败，请重试"),
    NEED_LOGIN(10010, "请登录后尝试"),
    NEED_ADMIN(10011, "非管理员，无法操作"),
    CREATE_FAILED(10012, "创建失败，请重试"),
    DELETE_FAILED(10013, "删除失败，请重试"),
    MKDIR_FAILED(10014, "创建文件夹失败，请重试"),
    UPLOAD_FAILED(10015, "上传失败，请重试"),
    NOT_SALE(10016, "商品未上架"),
    NOT_ENOUGH(10017, "商品库存不足"),
    CART_EMPTY(10018, "购物车为空，请添加商品"),
    NO_ORDER(10019, "订单不存在，请重试"),
    NOT_YOUR_ORDER(10020, "不能操作他人的订单"),
    WRONG_ORDER_STATUS(10021, "订单状态不符"),
    SYSTEM_ERROR(20000, "系统异常");

    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    StoreExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
