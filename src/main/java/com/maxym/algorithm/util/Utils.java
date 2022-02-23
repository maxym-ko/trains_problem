package com.maxym.algorithm.util;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils(){
    }

    public static List<List<Integer>> generatePerm(List<Integer> original) {
        if (original.isEmpty()) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        Integer firstElement = original.remove(0);
        List<List<Integer>> returnValue = new ArrayList<>();
        List<List<Integer>> permutations = generatePerm(original);

        for (List<Integer> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<Integer> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }
}
