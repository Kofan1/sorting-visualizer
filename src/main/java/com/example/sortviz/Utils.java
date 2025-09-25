package com.example.sortviz;


import java.util.Random;

//random number
public class Utils {
    public static int[] randomArray(int n, int min, int max) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(max - min + 1) + min;
        return a;
    }


    public static int max(int[] a) {
        int m = Integer.MIN_VALUE;
        for (int v : a) if (v > m) m = v;
        return m <= 0 ? 1 : m;
    }
}