package com.hjucook.recommend.core;

import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

import java.io.File;
import java.net.URL;

/**
 * 评估查准率和查全率
 *
 * @author zhengjian
 * @date 2018-11-19 14:28
 */
public class RecommendCheck {
    public static void main(String[] args) throws Exception {
        final String filePath = "ua.base";
        URL url = Recommend.class.getClassLoader()
                .getResource(filePath);
        File file = new File(url.getFile());
        RandomUtils.useTestSeed();
        DataModel model = new FileDataModel(file);
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder builder = (DataModel dataModel) -> {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
            return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
        };
        IRStatistics statistics = evaluator.evaluate(builder, null,
                model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.println(statistics.getPrecision());
        System.out.println(statistics.getRecall());
    }
}
