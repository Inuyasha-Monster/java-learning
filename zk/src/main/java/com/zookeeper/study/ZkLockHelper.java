package com.zookeeper.study;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * @author djl
 * @create 2021/2/17 9:58
 */
@Slf4j
public class ZkLockHelper {
    public static final int TRY_COUNT_MAX = 10;

    private CuratorFramework curator;

    /**
     * 锁的名称空间
     */
    private static final String DISTRIBUTED_LOCKS_NAMESPACE = "distributed-locks-nameSpace";

    /**
     * 分布式锁总节点
     */
    private static final String ZK_DISTRIBUTED_LOCK = "distributed-locks";

    private static  final String SEPARATOR = "/";

    public ZkLockHelper(CuratorFramework curator) {
        this.curator = curator;
    }


    /**
     * @Description: 初始化节点
     * @Param: []
     * @return: void
     * @Author: wxh
     * @Date: 2018/10/1
     */
    private void init (){

        // 定义命名空间
        curator.usingNamespace("ZKLock-NameSpace");

        try {

            if (curator.checkExists().forPath(SEPARATOR + ZK_DISTRIBUTED_LOCK) == null) {
                //创建总节点
                curator.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(SEPARATOR + ZK_DISTRIBUTED_LOCK);
            }

            //创建watcher事件监听总节点
            addWatcherToLock(SEPARATOR + ZK_DISTRIBUTED_LOCK);

        } catch (Exception e) {
            log.error("初始化化失败！", e);
        }
    }


    /**
     * @Description: 获取锁
     * @Param: [lock]
     * @return: void
     * @Author: wxh
     * @Date: 2018/10/1
     */
    public boolean tryLock(String lock) {

        int count = 0;

        while (true) {

            try {
                curator.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(SEPARATOR + ZK_DISTRIBUTED_LOCK + SEPARATOR + lock);

                log.info("获取分布式锁成功");
                return true;

            } catch (Exception e) {

                log.error("获取分布式锁锁失败");

                try {
                    //睡眠50毫秒继续获取锁
                    Thread.sleep(50);
                    ++count;

                    if (count > TRY_COUNT_MAX) {
                        return false;
                    }
                } catch (InterruptedException e1) {
                }

            }
        }
    }

    /**
     * @Description: 释放锁
     * @Param: [lock]
     * @return: boolean
     * @Author: wxh
     * @Date: 2018/10/1
     */
    public boolean releaseLock(String lock){

        try{
            if(curator.checkExists().forPath(SEPARATOR + ZK_DISTRIBUTED_LOCK + SEPARATOR + lock) != null){
                curator.delete().forPath(SEPARATOR + ZK_DISTRIBUTED_LOCK + SEPARATOR + lock);
            }
        }catch (Exception e){
            log.error("释放锁失败！", e);
            return false;
        }

        log.info("分布式锁释放成功！");
        return true;
    }


    private void addWatcherToLock(String path) throws Exception {

        final PathChildrenCache pathChildrenCache = new PathChildrenCache(curator, path, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
                    String path = event.getData().getPath();
                    log.info("上一个会话已结束，已释放锁路径：{}" , path);
                }
            }
        });

    }
}
