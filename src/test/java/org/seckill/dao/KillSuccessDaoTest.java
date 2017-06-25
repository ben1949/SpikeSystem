package org.seckill.dao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.seckill.entity.SecKill;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import javax.annotation.Resource;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by weihuap on 2017/5/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class KillSuccessDaoTest {
    @Resource
    private KillSuccessDao killSucessDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {

        int count = killSucessDao.insertSuccessKilled(1001l,13730655837l);
        System.out.print("count :"+ count);
    }

    @Test
    public void testQueryByIdWithSecKill() throws Exception {
        SuccessKilled succesKilled = killSucessDao.queryByIdWithSecKill(1001l,13730655837l);
        System.out.println(succesKilled);
    }
}