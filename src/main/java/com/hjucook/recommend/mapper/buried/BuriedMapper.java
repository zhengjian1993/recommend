package com.hjucook.recommend.mapper.buried;

import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.entity.buried.TargetDayClick;
import com.hjucook.recommend.model.entity.buried.TargetDuration;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 埋点操作
 *
 * @author zhengjian
 * @date 2018-12-12 16:47
 */
@Repository
public interface BuriedMapper {

    /**
     * 获取用户点击事件
     * @param userId null 查询所有
     * @param num 查询个数
     * @return
     */
    List<TargetClick> listTargetClick(@Param("userId") Integer userId,
                                      @Param("num") Integer num);

    /**
     * 统计用户点击事件
     * @param userId null 查询所有
     * @param num 查询个数
     * @return
     */
    List<TargetDayClick> listTargetDayClick(@Param("userId") Integer userId,
                                            @Param("num") Integer num);

    /**
     * 统计用户页面停留时间
     * @param userId null 查询所有
     * @param num
     * @return
     */
    List<TargetDuration> listTargetDuration(@Param("userId") Integer userId,
                                            @Param("num") Integer num);
}
