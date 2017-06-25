package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entity.SecKill;
import org.seckill.exception.SecKillCloseExecution;
import org.seckill.exception.SecKillException;
import org.seckill.exception.RepeatKillException;
import java.util.List;

/**
 * 业务接口:站在使用者角度上去实现
 * 三个方面:方法定义粒度,参数 返回类型
 * Created by weihuap on 2017/5/21.
 */
public interface SecKillService {
    /**
     * 查询所有的查询秒杀接口
     * @return
     */
    List<SecKill>getKillSecList();

    /**
     * 查询单个秒杀
     * @return
     */
    SecKill getById(long seckillId);

    /**
     * 秒杀开启是输出秒杀接口地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSecKillUrl(long seckillId);


    /**
     * 执行秒杀类型
     * @param seckillId
     * @param phone
     * @param md5
     */
    SecKillExecution executeSecKill(long seckillId, long phone, String md5) throws SecKillException,RepeatKillException,SecKillCloseExecution;
    /**
     * 执行秒杀类型 by 存储过程
     * @param seckillId
     * @param phone
     * @param md5
     */
    SecKillExecution executeSecKillProcedure(long seckillId, long phone, String md5) throws SecKillException,RepeatKillException,SecKillCloseExecution;


}
