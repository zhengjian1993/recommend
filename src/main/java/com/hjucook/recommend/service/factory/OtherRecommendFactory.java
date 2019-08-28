package com.hjucook.recommend.service.factory;

import com.hjucook.recommend.model.vo.response.TargetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 其他推荐
 * 目前随机
 * @author zhengjian
 * @date 2018-12-28 14:09
 */
@Service
public class OtherRecommendFactory extends BaseRecommendFactory {

    @Override
    public List<TargetResponse> recommend(Integer userId, Integer recommendNum) {
        return new ArrayList<>();
    }

}
