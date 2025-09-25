package com.example.sortviz;

import java.util.ArrayList;
import java.util.List;

public class SortingAlgorithms {

    // —— Bubble Sort ——
    public static List<Operation> bubbleOps(int[] arr) {
        int[] a = arr.clone();
        List<Operation> ops = new ArrayList<>();
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                ops.add(Operation.hilite(j, j + 1));
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                    ops.add(Operation.swap(j, j + 1));
                }
            }
        }
        ops.add(Operation.clearHilite());
        return ops;
    }

    // —— Quick Sort（原地快排，记录 swap 与对比高亮） ——
    public static List<Operation> quickOps(int[] arr) {
        int[] a = arr.clone();
        List<Operation> ops = new ArrayList<>();
        quick(a, 0, a.length - 1, ops);
        ops.add(Operation.clearHilite());
        return ops;
    }

    private static void quick(int[] a, int l, int r, List<Operation> ops) {
        if (l >= r) return;
        int i = l, j = r;
        int pivot = a[(l + r) >>> 1];
        while (i <= j) {
            while (a[i] < pivot) {
                ops.add(Operation.hilite(i, -1));
                i++;
            }
            while (a[j] > pivot) {
                ops.add(Operation.hilite(j, -1));
                j--;
            }
            if (i <= j) {
                if (i != j) {
                    swap(a, i, j);
                    ops.add(Operation.swap(i, j));
                }
                i++;
                j--;
            }
        }
        quick(a, l, j, ops);
        quick(a, i, r, ops);
    }

    // —— Merge Sort（自顶向下，记录写回 SET 动作） ——
    public static List<Operation> mergeOps(int[] arr) {
        int[] a = arr.clone();
        int[] temp = new int[a.length];
        List<Operation> ops = new ArrayList<>();
        mergeSort(a, 0, a.length - 1, temp, ops);
        ops.add(Operation.clearHilite());
        return ops;
    }

    private static void mergeSort(int[] a, int l, int r, int[] temp, List<Operation> ops) {
        if (l >= r) return;
        int m = (l + r) >>> 1;
        mergeSort(a, l, m, temp, ops);
        mergeSort(a, m + 1, r, temp, ops);
        merge(a, l, m, r, temp, ops);
    }

    private static void merge(int[] a, int l, int m, int r, int[] temp, List<Operation> ops) {
        for (int i = l; i <= r; i++) temp[i] = a[i];
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) {
            ops.add(Operation.hilite(i, j));
            if (temp[i] <= temp[j]) {
                a[k] = temp[i];
                ops.add(Operation.set(k, temp[i]));
                i++;
            } else {
                a[k] = temp[j];
                ops.add(Operation.set(k, temp[j]));
                j++;
            }
            k++;
        }
        while (i <= m) {
            a[k] = temp[i];
            ops.add(Operation.set(k, temp[i]));
            i++;
            k++;
        }
        while (j <= r) {
            a[k] = temp[j];
            ops.add(Operation.set(k, temp[j]));
            j++;
            k++;
        }
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
