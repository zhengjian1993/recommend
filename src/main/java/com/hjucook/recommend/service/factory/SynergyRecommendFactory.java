package com.hjucook.recommend.service.factory;

import com.hjucook.recommend.core.Recommend;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 协同过滤推荐
 *
 * @author zhengjian
 * @date 2019-01-22 14:13
 */
@Service
public class SynergyRecommendFactory extends BaseRecommendFactory{
    private static final int SIMILARITY_USER_LIMIT_NUM = 100;

    private Recommender recommender;

    /**
     * 控制初始化用户记录加载
     */
    private static final boolean IS_POST_CONSTRUCT = false;

    @PostConstruct
    private void init() {
        if (IS_POST_CONSTRUCT) {
            build();
        }
    }

    @Override
    public List<TargetResponse> recommend(Integer userId, Integer recommendNum) {
        try {
            List<RecommendedItem> recommendations = recommender.recommend(userId, recommendNum);
            return recommendations.stream()
                    .map(v -> new TargetResponse((int) v.getItemID(), "health-article", (double) v.getValue()))
                    .collect(Collectors.toList());
        } catch (TasteException e) {
            logger.error("user-id :" + userId + "synergy recommend failed because of exception");
        } catch (Exception e) {
            logger.error("synergy recommend failed because of data");
        }
        return new ArrayList<>();
    }

    private void build() {
        final String filePath = "ua.base";
        URL url = Recommend.class.getClassLoader().getResource(filePath);
        if (Objects.isNull(url)) {
            return;
        }
        File modelFile = new File(url.getFile());
        if (!modelFile.exists()) {
            logger.error("Please, specify name of file, or put file 'input.csv' into current directory!");
            return;
        }
        try {
            DataModel model = new FileDataModel(modelFile);
            //UserSimilarity 实现给出两个用户之间的相似度
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            //UserNeighborhood 实现   明确与给定用户最相似的一组用户
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(SIMILARITY_USER_LIMIT_NUM, similarity, model);
            //生成推荐引擎
            recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        } catch (Exception e) {
            logger.error("synergy is building wrong");
        }
    }
}
