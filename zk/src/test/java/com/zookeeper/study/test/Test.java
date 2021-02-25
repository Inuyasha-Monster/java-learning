package com.zookeeper.study.test;

import com.zookeeper.study.service.PurcharseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author djl
 * @create 2021/2/17 10:13
 */
@Slf4j
public class Test extends BaseJunit4Test {


    @Autowired
    private PurcharseService purcharseService;

    @org.junit.Test
    public void test() {

        new Thread( () -> {

            String result = purcharseService.purcharse("商品1", 8);
            log.info(result);

        }).start();

        String result = purcharseService.purcharse("商品1", 10);
        log.info(result);



    }

}