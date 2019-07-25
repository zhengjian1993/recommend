package com.hjucook.recommend.service.factory;

import com.hjucook.recommend.mapper.target.DimTargetTagRelationMapper;
import com.hjucook.recommend.model.entity.target.DimTargetTagRelation;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 其他推荐
 * 目前随机
 * @author zhengjian
 * @date 2018-12-28 14:09
 */
@Service
public class OtherRecommendFactory extends BaseRecommendFactory {
    @Autowired
    private DimTargetTagRelationMapper dimTargetTagRelationMapper;

    @Override
    public List<TargetResponse> recommend(Integer userId, Integer recommendNum) {
        return new ArrayList<>();
    }

}
