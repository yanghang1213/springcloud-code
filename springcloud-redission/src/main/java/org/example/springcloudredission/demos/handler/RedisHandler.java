package org.example.springcloudredission.demos.handler;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

public class RedisHandler {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 加锁
     * @param key
     * @return
     */
    public void lock(String key){
        //1.获取锁对象
        RLock lock = redissonClient.getLock(key);
        //2.尝试获取锁
        boolean flag = lock.tryLock();
        if(flag){
            //执行业务方法
            try {
                System.out.println("执行业务流程");
            }finally {
                if(lock != null){
                   lock.unlock();
                }
            }
        }
    }

}
