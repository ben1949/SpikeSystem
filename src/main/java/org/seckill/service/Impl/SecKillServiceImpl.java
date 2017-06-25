package org.seckill.service.Impl;

import com.sun.net.httpserver.Authenticator;
import org.apache.commons.collections.MapUtils;
import org.seckill.dao.KillSuccessDao;
import org.seckill.dao.SecKillDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SecKillCloseExecution;
import org.seckill.exception.SecKillException;
import org.seckill.service.SecKillService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;


/**
 * Created by weihuap on 2017/5/28.
 */
//@Ccomponent  @Service @Dao @Controller

@Service
public class SecKillServiceImpl implements SecKillService {

    private Logger logger = LoggerFactory.getLogger((this.getClass()));
    //注入Service依赖
    @Autowired
    private SecKillDao seckillDao;

    @Autowired
    private KillSuccessDao successKilledDao;

    @Autowired
    private RedisDao redisDao;
    //md5盐值
    private final String slat="abjjkaasdfasjsfiieoejqwet@33###11111aa";


    public List<SecKill> getKillSecList() {
        return seckillDao.queryAll(0,4);
    }

    public SecKill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSecKillUrl(long seckillId) {
        //优化点：缓存优化
        /**
         *get from redis cache
         * if null,
         *    get it from db
         * else
         *     put cache
         */
        //1 访问redis
        SecKill seckill = null;
        try {
             seckill = redisDao.getSeckill(seckillId);
        }
        catch(Exception e)
        {
            //logger(e.getMessage(),e);
            System.out.println("redis server was not ready!!!");
        }
        finally {

            if (seckill == null) {
                //2 redis中不存在，则访问数据库
                seckill = seckillDao.queryById(seckillId);
                if (seckill == null) {
                    //3 数据中中不存在，则表示秒杀单不存在
                    return new Exposer(false, seckillId);
                } else {
                    redisDao.putSeckill(seckill);
                }

            }
        }
        Date startTime = seckill.getStartTime();
        Date nowTime = new Date();

        Date endTime = seckill.getEndTime();

        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() >endTime.getTime())
        {
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
    private String getMD5(long seckillId)
    {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


    @Transactional
    /**
     *使用注解事务方法的优点
     * 开发团队达成一致的约定，明确标注事务方法的编程风格
     * 保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离事务方法外部
     * 不是所有的方法都需要事务
     *
     */
    public SecKillExecution executeSecKill(long seckillId, long userPhone, String md5) throws SecKillException, RepeatKillException, SecKillCloseExecution {
        if(md5 == null || ! md5.equals(getMD5(seckillId)))
        {
            throw new SecKillException("seckill data rewrite");
        }
        //执行秒杀逻辑
        Date nowTime = new Date();
        try
        {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if (insertCount <= 0)
            {
                throw new RepeatKillException("seckill repeated");

            }
            else
            {
                //减库存，减少商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount <= 0)
                {
                    //秒杀结束
                    throw new SecKillCloseExecution("seckill is closed");
                }
                else
                {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSecKill(seckillId,userPhone);
                    return new SecKillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }

            }
        }
        catch(SecKillCloseExecution e1)
        {
            throw e1;
        }
        catch(RepeatKillException e2)
        {
            throw e2;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(),e);
            throw new SecKillException("seckill inner error:"+e.getMessage());
        }

    }
    public SecKillExecution executeSecKill2(long seckillId, long userPhone, String md5) throws SecKillException, RepeatKillException, SecKillCloseExecution {
        if(md5 == null || ! md5.equals(getMD5(seckillId)))
        {
            throw new SecKillException("seckill data rewrite");
        }
        //执行秒杀逻辑
        Date nowTime = new Date();
        try
        {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);

            if(updateCount <= 0)
            {
                //秒杀结束
                throw new SecKillCloseExecution("seckill is closed");
            }
            else
            {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if (insertCount <= 0)
                {
                    throw new RepeatKillException("seckill repeated");

                }
                else
                {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSecKill(seckillId,userPhone);
                    return new SecKillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }

            }
        }
        catch(SecKillCloseExecution e1)
        {
            throw e1;
        }
        catch(RepeatKillException e2)
        {
            throw e2;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(),e);
            throw new SecKillException("seckill inner error:"+e.getMessage());
        }

    }


    public SecKillExecution executeSecKillProcedure(long seckillId, long phone, String md5)
    {
        if(md5 == null || ! md5.equals(getMD5(seckillId)))
        {
            return new SecKillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String,Object>map = new HashMap<String,Object>();
        map.put("seckillId",seckillId);
        map.put("phone",phone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1)
            {
                SuccessKilled sk = successKilledDao.queryByIdWithSecKill(seckillId,phone);
                return new SecKillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
            }
            else
            {
                return new SecKillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(),e);
            return new SecKillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }

}
