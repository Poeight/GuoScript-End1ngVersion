package org.formidable.guoscript.wrapper;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class WeightedRandomWrapper<T> {

    private HashMap<T, Double> resultWeightMap = new HashMap<>();
    private double weightSum = 0;
    private Random random = new Random();

    public void clear() {
        resultWeightMap.clear();
        weightSum = 0;
    }

    public void add(T result, double weight) {
        weightSum += weight;
        if (resultWeightMap.containsKey(result)) {
            resultWeightMap.put(result, resultWeightMap.get(result) + weight);
            return;
        }
        resultWeightMap.put(result, weight);
    }

    public T nextResult() {
        double n = random.nextDouble() * weightSum;
        double m = 0;
        for (T result : resultWeightMap.keySet()) {
            double weight = resultWeightMap.get(result);
            if (m <= n && n < m + weight) {
                return result;
            }
            m = m + weight;
        }
        return null;
    }

    public Set<T> getResults() {
        return resultWeightMap.keySet();
    }

    public boolean isEmpty() {
        return resultWeightMap.isEmpty();
    }

}