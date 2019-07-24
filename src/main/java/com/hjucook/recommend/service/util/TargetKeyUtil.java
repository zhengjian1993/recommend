package com.hjucook.recommend.service.util;

import com.hjucook.recommend.model.dto.target.SortModel;
import com.hjucook.recommend.model.vo.response.TargetResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * target key util
 *
 * @author zhengjian
 * @date 2018-12-13 14:24
 */
public class TargetKeyUtil {
    private static final String TARGET_SEPARATOR = "_";

    private static final String REDIS_KEY_PREFIX = "recommend:targets";

    public static String createKey(String targetType, Integer targetId) {
        return targetType + TARGET_SEPARATOR + targetId;
    }

    /**
     * 从key获取数据库的targetType
     * @param key target全局key
     * @return
     */
    public static String getTargetType(String key) {
        String[] targets = key.split(TARGET_SEPARATOR);
        if (targets.length == 2) {
            return targets[0];
        }
        return "";
    }

    /**
     * 从key获取数据库targetId
     * @param key target全局key
     * @return
     */
    public static Integer getTargetId(String key) {
        String[] targets = key.split(TARGET_SEPARATOR);
        if (targets.length == 2) {
            return Integer.valueOf(targets[1]);
        }
        return 0;
    }

    /**
     * key转换为target对象
     * @param key
     * @return
     */
    public static TargetResponse getTargetResponse(String key) {
        String[] targets = key.split(TARGET_SEPARATOR);
        if (targets.length == 2) {
            return new TargetResponse(Integer.valueOf(targets[1]), targets[0]);
        }
        return null;
    }

    /**
     * key转换为target对象
     * @param sortModel
     * @return
     */
    public static TargetResponse getTargetResponse(SortModel sortModel) {
        TargetResponse targetResponse = getTargetResponse(sortModel.getKey());
        if (Objects.nonNull(targetResponse)){
            targetResponse.setWeight(sortModel.getWeight());
        }
        return targetResponse;
    }

    /**
     * 获取推荐列表的redis key
     * @param userId
     * @return
     */
    public static String getRecommendListRedisKey(Integer userId) {
        return REDIS_KEY_PREFIX + ":fashion:" + userId;
    }

    /**
     * 获取已推荐列表的redis key
     * @param userId
     * @return
     */
    public static String getAlreadyRecommendListRedisKey(Integer userId) {
        return REDIS_KEY_PREFIX + ":outmoded:" + userId;
    }

    /**
     * 获取用户build推荐列表分布式锁
     * @param userId 如果数字则是用户id，如果是字符串则字符串自己代表的意思
     * @return
     */
    public static String getBuildLockRedisKey(String userId) {
        return REDIS_KEY_PREFIX + ":lock:" + userId;
    }

    /**
     * 获取用户推荐次数redis key
     * @param userId
     * @return
     */
    public static String getRecommendNumRedisKey(Integer userId) {
        return REDIS_KEY_PREFIX + ":num:" + userId;
    }

    /**
     * 获取排行榜的redis key
     * @return
     */
    public static String getRankingListRedisKey() {
        return REDIS_KEY_PREFIX + ":ranking";
    }

    /**
     * 获取用户读取排行榜的下标
     * @param userId
     * @return
     */
    public static String getRankingReadIndexRedisKey(Integer userId) {
        return getRankingReadIndexPrefix() + ":" + userId;
    }

    /**
     * 获取排行榜读取下标key的前缀
     * @return
     */
    public static String getRankingReadIndexPrefix() {
        return REDIS_KEY_PREFIX + ":read:index";
    }

    public static void main(String[] args) {
        String time = "1547447973071";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(time);
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        long endTime = todayEnd.getTime().getTime();
        long diffTime = endTime - lt;
        System.out.println(diffTime);
        Date date = new Date(lt);
        System.out.println(simpleDateFormat.format(date));
    }

}
