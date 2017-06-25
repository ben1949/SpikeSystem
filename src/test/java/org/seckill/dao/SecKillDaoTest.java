package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.seckill.entity.SecKill;
import org.springframework.test.context.ContextConfiguration;
import javax.annotation.Resource;
import java.util.List;
import static org.junit.Assert.*;
import java.util.Date;

/**
 * 配置spring 和 junit 整合,junit 启动时候加载spring IOC容器
 * spring-test
 * Created by weihuap on 2017/5/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {
    @Resource
    private SecKillDao seckillDao;
    @Test
    public void testReduceNumber() throws Exception {
        Date date = new Date();
        int updateNumber = seckillDao.reduceNumber(1004L,date);
        System.out.println("updateNumber is "+updateNumber);
    }

    /**
     * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [1, 0, param1, param2]
     * @throws Exception
     */
    @Test
    public void testQueryById() throws Exception {
        long id=1000;
        SecKill secKill = seckillDao.queryById(id);
        System.out.println("seckill is"+secKill);
    }

    /**
     * seckill is SecKill{seckillid=1000, name='1000 iphone6', number=100, startTime=Mon May 01 00:00:00 CST 2017, endTime=Tue May 02 00:00:00 CST 2017, createTime=Sun May 21 10:21:23 CST 2017}
     seckill is SecKill{seckillid=1001, name='500 ipad', number=100, startTime=Mon May 01 00:00:00 CST 2017, endTime=Tue May 02 00:00:00 CST 2017, createTime=Sun May 21 10:21:23 CST 2017}
     seckill is SecKill{seckillid=1002, name='300 xiaomi4', number=100, startTime=Mon May 01 00:00:00 CST 2017, endTime=Tue May 02 00:00:00 CST 2017, createTime=Sun May 21 10:21:23 CST 2017}
     seckill is SecKill{seckillid=1003, name='200 hongminote', number=100, startTime=Mon May 01 00:00:00 CST 2017, endTime=Tue May 02 00:00:00 CST 2017, createTime=Su
     * @throws Exception
     *  List<SecKill> queryAll(@Param("offset") int offset , @Param("limit")int limit);
     */
    @Test
    public void testQueryAll() throws Exception {
        List<SecKill>seckills = seckillDao.queryAll(0,100);
        for (SecKill seckill:seckills)
        {
            System.out.println("seckill is "+seckill);

        }

    }
}