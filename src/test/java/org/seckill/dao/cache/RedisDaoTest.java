package org.seckill.dao.cache;

import junit.runner.BaseTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SecKillDao;
import org.seckill.entity.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by weihuap on 2017/6/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long id = 1002;
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SecKillDao secKillDao;

    @Test
    public void testSeckill() throws Exception {
        //get and put
        SecKill seckill = redisDao.getSeckill(id);
        if (seckill == null )
        {
            seckill = secKillDao.queryById(id);
            if(seckill != null)
            {
               String result =  redisDao.putSeckill(seckill);
                System.out.println("result is "+result);
                seckill = redisDao.getSeckill(id);
                System.out.println("seckill is "+seckill);
            }
        }

    }


}