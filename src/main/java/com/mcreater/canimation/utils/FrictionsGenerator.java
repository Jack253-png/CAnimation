package com.mcreater.canimation.utils;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;

import java.util.Arrays;
import java.util.Vector;

public final class FrictionsGenerator {
    public static double[] generate1(int iterations){
        Vector<Double> vec = new Vector<>();
        for (double index = 1; index < iterations; index++) {
            vec.add(1 / index);
        }
        vec.removeIf(aDouble -> aDouble <= 0.05);
        for (double a0 = 0.05; a0 > 0.0001; a0 -= 0.001) {
            vec.add(a0);
        }
        vec.add(0D);

        return toDoubleArray(vec);
    }
    public static double[] generate2() {
        Vector<Double> vec = new Vector<>();
        for (double i = 1; i >= 0; i -= 0.025) {
            vec.add(i);
        }
        vec.add(0D);
        return toDoubleArray(vec);
    }
    private static double[] toDoubleArray(Vector<Double> vec){
        double[] result = new double[vec.size()];
        for (int i = 0; i < vec.size(); i++) {
            result[i] = vec.get(i);
        }
        return result;
    }
}
