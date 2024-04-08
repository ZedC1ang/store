package top.zeds1aw.store.cartorder.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 描述：     生成订单No工具类
 */
public class OrderCodeFactory {

    //使用ThreadLocal升级SimpleDateFormat
    public static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            // 每个线程第一次获取的时候都会创建这个对象，且线程与线程之间是不共享的
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    private static String getDateTime() {
//        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // 使用ThreadLocal升级后就不需要创建对象了，直接使用get方法即可
        SimpleDateFormat sdf = simpleDateFormatThreadLocal.get();
        return sdf.format(new Date());
    }

    private static int getRandom(Long n) {
        Random random = new Random();
        // 获取5位随机数
        return (int) (random.nextDouble() * (90000)) + 10000;
    }

    public static String getOrderCode(Long userId) {
        return getDateTime() + getRandom(userId);
    }
}
