package com.hjucook.recommend.service.util;

import com.hjucook.recommend.model.dto.target.SortModel;

import java.util.Map;
import java.util.Objects;

/**
 * ${DESCRIPTION}
 *
 * @author zhengjian
 * @date 2018-12-13 16:08
 */
public class MapUtil {
    /**
     * 给map中存在的key加分
     * @param map
     * @param key
     * @param weight
     * @return
     */
    public static void injectMap(Map<String, SortModel> map, String key, Double weight) {
        if (Objects.isNull(weight)) {
            return;
        }
        if (map.containsKey(key)) {
            SortModel sortModel = map.get(key);
            double formerWeight = sortModel.getWeight();
            sortModel.setWeight(formerWeight + weight);
        } else {
            map.put(key, new SortModel(key, weight));
        }
    }
}
