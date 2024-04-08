package com.imooc.cloud.mall.practice.user.service.impl;

import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 描述：     TODO
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceImplTest {
//    @Mock
//    HashMap myHashMap;
    @Test
    public void myFirstMock() {
        HashMap mockHashMap = Mockito.mock(HashMap.class);
        Mockito.when(mockHashMap.size()).thenReturn(5);
        System.out.println(mockHashMap.size());
        mockHashMap.put(new Object(), new Object());
        System.out.println(mockHashMap.size());

    }

    @Spy
    HashMap<String, String> hashMap = new HashMap<>();

    @Test
    public void myFirstSpy() {
        HashMap<String, String> spy = Mockito.spy(hashMap);
        spy.put("1","2");
        System.out.println(spy.size());
        spy.put("2","3");
        Mockito.when(spy.size()).thenReturn(10);
        System.out.println(spy.size());

    }
}