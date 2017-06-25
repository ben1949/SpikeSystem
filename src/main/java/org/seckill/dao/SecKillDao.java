package org.seckill.dao;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SecKill;
import java.util.Date;
import java.util.List;
/**
 * Created by weihuap on 2017/5/21.
 */
public interface SecKillDao {
    /**
     * 减库存
     * @param secKillId
     * @param killTime
     * @return 如果影响行数大于1或者2
     int reduceNumber(long seckillId,Date killTime);

     */
    int reduceNumber(@Param("seckillId")long seckillId,@Param("killTime")Date killTime);

    /**
     * @param offset
     * @return
     * @param seckillId
     * @return
     */
    SecKill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<SecKill> queryAll(@Param("offset") int offset , @Param("limit")int limit);

    /**
     * 使用存储过程执行秒杀
     */
    void killByProcedure(Map<String,Object>paramMap);
}
