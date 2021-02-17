package com.zookeeper.study.service.impl;

import com.zookeeper.study.ZkLockHelper;
import com.zookeeper.study.service.PurcharseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author djl
 * @create 2021/2/17 10:00
 */
@Service
@Slf4j
public class PurcharseServiceImpl implements PurcharseService {

    /**
     * 购买业务锁
     */
    public static final String PURCHARSE_LOCK = "purcharse-lock";

    private static Map<String, Integer> stocks= new HashMap();

    static {

        stocks.put("商品1",10);
        stocks.put("商品2",10);
        stocks.put("商品3",10);

    }

    @Autowired
    private ZkLockHelper zkLockHelper;

    /**
     * @Description: 模拟购买服务
     * @Param: [stock, buyCount]
     * @return: java.lang.String
     * @Author: wxh
     * @Date: 2018/10/1
     */
    @Override
    public String purcharse(String stock, int buyCount){

        checkParam(stock);

        log.info("请求购买商品：{},购买数量：{}", stock, buyCount);

        boolean flag = zkLockHelper.tryLock(PURCHARSE_LOCK + stock);//获取锁
        if (!flag) {
            return "网络繁忙，请稍后重试";
        }

        try {
            // 1、校验库存
            Integer count = stocks.get(stock);
            if (count < buyCount) {
                log.warn("库存不足！用户购买：{}份，当前剩余：{}份",buyCount,count);
                return "库存不足！";
            }

            // 2、创建订单
            createOrder();

            // 3、扣件库存
            stocks.put(stock, count - buyCount);
            log.info("扣件库存成功，库存剩余：{}",stocks.get(stock));

        } finally {
            zkLockHelper.releaseLock(PURCHARSE_LOCK + stock);//释放锁
        }

        return "购买成功！";
    }

    private void createOrder() {
        log.info("正在购买。。。。");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("购买成功，订单号为：{}", UUID.randomUUID());
    }

    private void checkParam(String stock) {

        if (!stocks.containsKey(stock)) {
            throw new RuntimeException("不存在此商品");
        }

    }

}
