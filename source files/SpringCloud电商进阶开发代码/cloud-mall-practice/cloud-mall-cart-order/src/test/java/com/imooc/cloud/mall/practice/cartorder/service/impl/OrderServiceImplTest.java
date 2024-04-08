package com.imooc.cloud.mall.practice.cartorder.service.impl;

import com.imooc.cloud.mall.practice.cartorder.feign.ProductFeignClient;
import com.imooc.cloud.mall.practice.cartorder.model.pojo.Product;
import com.imooc.cloud.mall.practice.cartorder.model.vo.CartVO;
import com.imooc.cloud.mall.practice.common.common.Constant.SaleStatus;
import com.imooc.cloud.mall.practice.common.exception.ImoocMallException;
import com.imooc.cloud.mall.practice.common.exception.ImoocMallExceptionEnum;
import junit.framework.TestCase;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 描述：     TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest{

    @Mock
    ProductFeignClient productFeignClient;

    @Test
    public void validSaleStatusAndStockTest() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        CartVO cartVO = new CartVO();
        cartVO.setProductId(27);
        cartVO.setQuantity(1);
        Product fakeProduct = new Product();
        fakeProduct.setStatus(1);
        fakeProduct.setStock(4);
        Thread.sleep(1000);
        watch.split();
        System.out.println("split time:" + watch.getSplitTime());

        Thread.sleep(1500);
        Mockito.when(productFeignClient.detailForFeign(27)).thenReturn(fakeProduct);
        Product product = productFeignClient.detailForFeign(cartVO.getProductId());
        long time = watch.getTime();
        System.out.println("time elapsed:" + time);

        watch.suspend();
        //判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(SaleStatus.NOT_SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        Thread.sleep(3000);
        //判断商品库存
        if (cartVO.getQuantity() > product.getStock()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
        watch.resume();
        Thread.sleep(500);
        System.out.println("time elapsed:" + watch.getTime());

        Assert.assertNotNull(product);
        Assert.assertEquals(1, (int)product.getStatus());
        Assert.assertTrue(product.getStock() > cartVO.getQuantity());
    }

}