package com.ylink.inetpay.cbs.mrs.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.ylinkpay.framework.core.listener.SpringContextHolder;

import com.redrock.ips.support.cache.redis.single.RedisSingleConnectPool;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;

import redis.clients.jedis.Jedis;

public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

   // private RedisTemplate redisTemplate;
//    private RedisSentinelConnectPool redisPool;
    private RedisSingleConnectPool redisSingleConnectPool =  (RedisSingleConnectPool) SpringContextHolder.getApplicationContext().getBean("redisSingleConnectPool");
    
    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    /**
     * Lock key path.
     */
    private String lockKey;

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    private int expireMsecs = 60 * 1000;

    /**
     * 锁等待时间，防止线程饥饿
     */
    private int timeoutMsecs = 10 * 1000;

    private volatile boolean locked = false;

    public RedisLock(){
        redisSingleConnectPool.init();

    }
    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
     *
     * @param lockKey lock key (ex. account:1, ...)
     */
    public RedisLock( String lockKey) {
        this.lockKey = lockKey + "_lock";
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     *
     */
    public RedisLock( String lockKey, int timeoutMsecs) {
        this( lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }
    
    

    /**
     * Detailed constructor.
     *
     */
    public RedisLock(String lockKey, int timeoutMsecs, int expireMsecs) {
        this( lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * @return lock key
     */
    public String getLockKey() {
        return lockKey;
    }

    private String get(final String key) {
        Object obj = null;
        Jedis d =null;
        try {
        	 StringRedisSerializer serializer = new StringRedisSerializer();
        	  d = redisSingleConnectPool.getJedis();
        	 byte[] data =d.get(serializer.serialize(key));
        	  if (data == null) {
                  return null;
              }
        	  obj= serializer.deserialize(data);
        
        } catch (Exception e) {
            logger.error("get redis error, key : {}", key);
        }finally{
           redisSingleConnectPool.returnResource(d);
        }
        return obj != null ? obj.toString() : null;
    }

    private Boolean setNX(final String key, final String value) {
        Boolean success = null ;
        Jedis d =null;


        try {
            StringRedisSerializer serializer = new StringRedisSerializer();
            redisSingleConnectPool.init();
      	    d = redisSingleConnectPool.getJedis();
             success =d.setnx(serializer.serialize(key), serializer.serialize(value))==1L?Boolean.TRUE:Boolean.FALSE;
             return success;

        } catch (Exception e) {
            logger.error("setNX redis error, key : {}", key);
        }finally{
            redisSingleConnectPool.returnResource(d);
         }
        return success != null ? success : Boolean.FALSE;
    }

    private String getSet(final String key, final String value) {
        Object obj = null;
        Jedis d =null;

        try {
        	
        	
            StringRedisSerializer serializer = new StringRedisSerializer();
      	    d = redisSingleConnectPool.getJedis();

        	 byte[] ret = d.getSet(serializer.serialize(key), serializer.serialize(value));
        	 obj= serializer.deserialize(ret);

        } catch (Exception e) {
            logger.error("setNX redis error, key : {}", key);
        }finally{
            redisSingleConnectPool.returnResource(d);
         }
        return obj != null ? (String) obj : null;
    }

    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    public synchronized boolean lock() throws InterruptedException {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (this.setNX(lockKey, expiresStr)) {
                // lock acquired
                locked = true;
                return true;
            }

            String currentValueStr = this.get(lockKey); //redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired

                String oldValueStr = this.getSet(lockKey, expiresStr);
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受

                    //[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;

            /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
             */
            Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);

        }
        return false;
    }


    /**
     * Acqurired lock release.
     */
    public synchronized void unlock() {
        if (locked) {
        	redisSingleConnectPool.getJedis().del(lockKey);
            locked = false;
        }
    }
    
    public static String getPersonLockKey(String type, String number){
    	StringBuffer lockKey = new StringBuffer();
    	lockKey.append(PERSON_LOCK_SUFFIX).append(type).append("_").append(number);
    	return lockKey.toString();
    }
    public static String getOrganAssetLockKey(String type, String number){
    	StringBuffer lockKey = new StringBuffer();
    	lockKey.append(ORGAN_LOCK_SUFFIX).append(type).append("_").append(number);
    	return lockKey.toString();
    }
    private static String PERSON_LOCK_SUFFIX = "person_lock_key_";

    private static String ORGAN_LOCK_SUFFIX = "organ_lock_key_";

    public static String getOrganLockKey( String socialCreditCode, String organizeCode, String revenueCode, String businessLicence,
    		String organOtherCode){
    	StringBuffer lockKey = new StringBuffer();
    	if(StringUtil.isNEmpty(organizeCode)) {
    		lockKey.append(ORGAN_LOCK_SUFFIX).append(MrsCredentialsType.MCT_71.getValue()).append("_").append(organizeCode);
    	} else if(StringUtil.isNEmpty(businessLicence)) {
    		lockKey.append(ORGAN_LOCK_SUFFIX).append(MrsCredentialsType.MCT_73.getValue()).append("_").append(businessLicence);
    	} else if(StringUtil.isNEmpty(socialCreditCode)) {
    		lockKey.append(ORGAN_LOCK_SUFFIX).append(MrsCredentialsType.MCT_72.getValue()).append("_").append(socialCreditCode);
    	} else if(StringUtil.isNEmpty(revenueCode)) {
    		lockKey.append(ORGAN_LOCK_SUFFIX).append(MrsCredentialsType.MCT_74.getValue()).append("_").append(revenueCode);
    	} else{
    		lockKey.append(ORGAN_LOCK_SUFFIX).append(MrsCredentialsType.MCT_99.getValue()).append("_").append(organOtherCode);
    	}
    	return lockKey.toString();
    }
}
