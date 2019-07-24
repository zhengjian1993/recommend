package com.hjucook.recommend.rest;

import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.vo.response.RecommendResponse;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import com.hjucook.recommend.service.RecommendService;
import com.hjucook.recommend.service.factory.BaseRecommendFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 推薦
 *
 * @author zhengjian
 * @date 2018-12-20 17:03
 */
@RestController
@RequestMapping("recommend")
public class RecommendController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RecommendService recommendService;
    @Autowired
    private BaseRecommendFactory synergyRecommendFactory;

    /**
     * 根据用户推荐
     * @param userId 用户id
     * @param num 推荐数
     */
    @GetMapping(value="article/{userId}/{num}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RecommendResponse articleRecommend(@PathVariable("userId") Integer userId,
                                              @PathVariable("num") Integer num) {
        return recommendService.articleRecommend(userId, num);
    }

    /**
     * 根据行为记录推荐
     * @param targetClicks 用户行为记录
     */
    @PostMapping(value="article",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TargetResponse> articleRecommend(@Valid @RequestBody List<TargetClick> targetClicks) {
        long startTime = System.currentTimeMillis();
        List<TargetResponse> targetResponses = recommendService.articleRecommend(targetClicks);
        long endTime = System.currentTimeMillis();
        logger.info("推荐耗时：" + (endTime - startTime));
        return targetResponses;
    }

    /**
     * 根据用户推荐
     * @param userId 用户id
     * @param num 推荐数
     */
    @GetMapping(value="synergy/{userId}/{num}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TargetResponse> synergyRecommend(@PathVariable("userId") Integer userId,
                                              @PathVariable("num") Integer num) {
        return synergyRecommendFactory.recommend(userId, num);
    }

}
