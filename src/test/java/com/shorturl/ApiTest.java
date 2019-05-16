package com.shorturl;

import com.shorturl.utils.HttpHander;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Test
    public void test() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", "https://www.baidu.com");
        map.put("tag", "baidu");
        String result = HttpHander.postFromData("http://127.0.0.1:8080/shortUrl", map);
        System.out.println(result);
        try {
            result = HttpHander.get("http://127.0.0.1:8080/baidu");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
