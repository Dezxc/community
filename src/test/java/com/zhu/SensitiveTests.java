package com.zhu;

import com.zhu.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以赌博，这里可以嫖娼，这里可以吸毒，这里可以开票，哈哈哈哈";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        text = "这里可以☆赌☆博☆，这里可以☆嫖☆娼☆，这里可以☆吸☆毒☆，这里可以☆开☆票☆，可以吃喝嫖赌，哈哈哈哈";
        System.out.println(sensitiveFilter.filter(text));

    }
}
