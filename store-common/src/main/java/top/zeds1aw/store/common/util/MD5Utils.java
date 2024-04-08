package top.zeds1aw.store.common.util;

import org.apache.tomcat.util.codec.binary.Base64;
import top.zeds1aw.store.common.common.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5密码保护工具
 */
public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));
    }

    /* 用这个方法测试生成的MD5的值 */
    public static void main(String[] args) {
        String md5 = null;
        try {
            md5 = getMD5Str ("1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(md5);
    }
}
