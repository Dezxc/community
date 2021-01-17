package com.zhu;

import com.zhu.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionTests {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1() {
        String o = (String) alphaService.save1();
        System.out.println(o);
    }

    @Test
    public void testSave2() {
        String o = (String) alphaService.save2();
        System.out.println(o);
    }
}
