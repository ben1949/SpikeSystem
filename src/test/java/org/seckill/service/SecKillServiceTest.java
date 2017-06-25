package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillCloseExecution;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Created by weihuap on 2017/5/28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SecKillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecKillService seckKillService;

    @Test
    public void testGetKillSecList() throws Exception {
        List<SecKill>list = seckKillService.getKillSecList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetById() throws Exception {
        long id = 1000;
        SecKill seckill = seckKillService.getById(id);
        logger.info("seckill ={}",seckill);

    }

    @Test
    public void SeckillLogic() throws Exception
    {
        long id = 1001;
        Exposer exposer = seckKillService.exportSecKillUrl(id);
        if(exposer.isExposed())
        {
            logger.info("exposer={}",exposer);
            long phone=135111223l;
            String md5=exposer.getMd5();
            try {
                SecKillExecution secKillExecution = seckKillService.executeSecKill(id, phone, md5);
                logger.info("result={}", secKillExecution);

            }
            catch(RepeatKillException e){
                logger.info(e.getMessage());
            }
            catch(SecKillCloseExecution e) {
                logger.info(e.getMessage());
            }
        }
    }
    @Test
    public void testExportSecKillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckKillService.exportSecKillUrl(id);
        logger.info("exposer={}",exposer);

    }

    @Test
    public void testExecuteSecKill() throws Exception {
        long id = 1000;
        long phone=135111222l;
        String md5="6b2f74196ebe5190147c7eb4e9577689";
        try {
            SecKillExecution secKillExecution = seckKillService.executeSecKill(id, phone, md5);
            logger.info("result={}", secKillExecution);

        }
        catch(RepeatKillException e){
            logger.info(e.getMessage());
        }
        catch(SecKillCloseExecution e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public  void testExecuteSecKillProcedure() throws Exception
    {
        long seckillId = 1001;
        long phone = 135111222l;
        Exposer exposer = seckKillService.exportSecKillUrl(seckillId);
        if(exposer.isExposed())
        {
            String md5 = exposer.getMd5();
            SecKillExecution secKillExecution = seckKillService.executeSecKillProcedure(seckillId,phone,md5);
            logger.info(secKillExecution.getStateInfo());
        }

    }
}