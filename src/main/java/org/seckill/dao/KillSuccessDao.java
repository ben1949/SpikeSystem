package org.seckill.dao;
import org.seckill.entity.SecKill;
import org.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;
/**
 * Created by weihuap on 2017/5/21.
 */
public interface KillSuccessDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSecKill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

}
