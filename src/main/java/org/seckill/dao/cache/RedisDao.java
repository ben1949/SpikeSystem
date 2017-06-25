package org.seckill.dao.cache;

/**
 * Created by weihuap on 2017/6/11.
 */
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.SecKill;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
public class RedisDao {
    private final JedisPool jedisPool;
    private Logger logger = LoggerFactory.getLogger((this.getClass()));
    public RedisDao(String ip,int port)
    {
        jedisPool = new JedisPool(ip,port);
    }
    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);
    public SecKill getSeckill(long seckillId)
    {   //缓存 redis 操作逻辑
        try
        {
            Jedis jedis = jedisPool.getResource();
            try
            {
                String key = "seckill:"+seckillId;
                //并没有实现内部序列化操作
                //get->byte[]->反序列化->Object(SecKill)
                //采用自定义序列化
                //class protostuff
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null)
                {
                    SecKill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    //Seckill 被反序列化
                    return seckill;
                }

            }
            finally
            {
                jedis.close();
            }
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(),e);

        }
        return null;
    }
    public String putSeckill(SecKill seckill)
    {  // set Object(SecKill ->序列化->byte[]

        try
        {
            Jedis jedis = jedisPool.getResource();
            try
            {
                String key = "seckill:" + seckill.getSeckillid();
                byte [] bytes = ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;
                jedis.setex(key.getBytes(),timeout,bytes);
            }
            finally {
                jedis.close();
            }

        }
        catch (Exception e)
        {
            logger.error(e.getMessage(),e);

        }
        return null;
    }



}
