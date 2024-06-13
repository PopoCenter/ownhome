package com.tencent.wxcloudrun.controller;

import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * counter控制器
 */
@RestController

public class CounterController extends BaseController {

    final CounterService counterService;
    final Logger logger;

    public CounterController(@Autowired CounterService counterService) {
        this.counterService = counterService;
        this.logger = LoggerFactory.getLogger(CounterController.class);
    }


    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/api/count")
    ApiResponse get() {
        logger.info("/api/count get request start mybatis plus");
        Optional<Counter> counter = counterService.getCounter(1);
        Integer count = 0;
        if (counter.isPresent()) {
            count = counter.get().getCount();
        }
        logger.info("/api/count get request end");
        return ApiResponse.ok(count);
    }


    /**
     * 更新计数，自增或者清零
     *
     * @param request {@link CounterRequest}
     * @return API response json
     */
    @PostMapping(value = "/api/count")
    ApiResponse create(@RequestBody CounterRequest request, @RequestHeader HttpHeaders headers) {
        logger.info("/api/count post request, action: {}", request.getAction());


        String openId = getOpenId(headers);
        String testToken = headers.getFirst("TOKEN");
        logger.info("/api/count header test tag, token={}, openId={}", testToken, openId);


        Optional<Counter> curCounter = counterService.getCounter(1);
        if (request.getAction().equals("inc")) {
            Integer count = 1;
            if (curCounter.isPresent()) {
                count += curCounter.get().getCount();
            }
            Counter counter = new Counter();
            counter.setId(1);
            counter.setCount(count);
            counterService.upsertCount(counter);
            return ApiResponse.ok(count);
        } else if (request.getAction().equals("clear")) {
            if (!curCounter.isPresent()) {
                return ApiResponse.ok(0);
            }
            counterService.clearCount(1);
            return ApiResponse.ok(0);
        } else {
            return ApiResponse.error("参数action错误");
        }
    }


//    public static void main(String[] args) {
//        // n个小学生
//        int n = 5;
//        // p正整数
//        int p = 10;
//        //小学生数字
//        List<Integer> positiveList = Lists.newArrayList(1, 2, 3, 4, 5);
//        List<Integer> numberList = Lists.newArrayList(-1, -1, -1, -1, -1);
//        cal(5, 997, positiveList);
//        cal(5, 7, numberList);
//    }
//
//    public static long cal(int n, int p, List<Integer> numberList) {
//        //小学生特征值计算逻辑
//        List<Integer> featureList = Lists.newArrayList();
//        for (int i = 0; i < numberList.size(); i++) {
//            //当前学生数字
//            int number = numberList.get(i);
//            int feature;
//            if (i == 0) {
//                feature = number;
//                featureList.add(feature);
//                continue;
//            }
//            int max = numberList.get(0);
//            for (int j = 1; j <= i; j++) {
//                int currentSum = numberList.get(j) + max;
//                if (currentSum > max) {
//                    max = currentSum;
//                }
//            }
//            feature = max;
//            featureList.add(feature);
//        }
//
//        // 小学生分数计算逻辑
//        List<Integer> scoreList = Lists.newArrayList();
//        for (int i = 0; i < featureList.size(); i++) {
//            int score;
//            if (i == 0) {
//                score = featureList.get(0);
//                scoreList.add(score);
//                continue;
//            }
//            int max = featureList.get(0) + featureList.get(0);
//            for (int j = 1; j <= i; j++) {
//                // 上一个分数
//                int beforeScore = scoreList.get(i - 1);
//                // 上一个特征值
//                int beforeFuture = featureList.get(i - 1);
//                if ((beforeScore + beforeFuture) > max) {
//                    max = beforeScore + beforeFuture;
//                }
//            }
//            score = max;
//            scoreList.add(score);
//        }
//
//        // 所有分数的最大值
//        int max = scoreList.get(0);
//        for (int i = 0; i < scoreList.size(); i++) {
//            int score = scoreList.get(i);
//            if (score > max) {
//                max = score;
//            }
//        }
//
//        System.out.println("max=" + max);
//        //取模
//        int result = max % p;
//        System.out.println("featureList=" + featureList);
//        System.out.println("scoreList=" + scoreList);
//        System.out.println("result=" + result);
//        return result;
//    }

}