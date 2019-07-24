package com.hjucook.recommend.core;

import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.net.URL;

/**
 * 评估
 *
 * @author zhengjian
 * @date 2018-11-19 14:28
 */
public class RecommendAssess {
    public static void main(String[] args) throws Exception {
        //RandomUtils.useTestSeed();
        final String filePath = "ua.base";
        URL url = Recommend.class.getClassLoader()
                .getResource(filePath);
        File file = new File(url.getFile());
        DataModel model = new FileDataModel(file);
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder builder = (DataModel dataModel) -> {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, dataModel);
            return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
        };
        double score = evaluator.evaluate(builder, null, model, 0.8, 1.0);
        System.out.println(score);
    }
}
