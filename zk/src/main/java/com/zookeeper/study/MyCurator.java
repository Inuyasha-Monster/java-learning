package com.zookeeper.study;

import org.apache.curator.framework.CuratorFramework;

/**
 * @author djl
 * @create 2021/2/17 9:58
 */
public class MyCurator {
    private CuratorFramework curatorFramework;

    public MyCurator(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }
}
