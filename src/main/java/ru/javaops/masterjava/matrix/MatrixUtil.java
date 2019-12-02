package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import ru.javaops.masterjava.service.MailService.MailResult;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {

        class Calc implements Runnable {

            private int[][] arr1;
            private int[][] arr2;
            private int[][] summArr;
            private int begin;
            private int end;

            public Calc(int[][] arr1, int[][] arr2, int[][] summArr, int begin, int end) {
                this.arr1 = arr1;
                this.arr2 = arr2;
                this.summArr = summArr;
                this.begin = begin;
                this.end = end;
            }

            public void run() {


                final int columnsA = matrixA[0].length;
                final int rowsB = matrixB.length;

                int thatColumn[] = new int[rowsB];

                try {
                    for (int j = 0; ; j++) {
                        for (int k = 0; k < columnsA; k++) {
                            thatColumn[k] = matrixB[k][j];
                        }

                        for (int i = begin; i < end; i++) {
                            int thisRow[] = matrixA[i];
                            int summand = 0;
                            for (int k = 0; k < columnsA; k++) {
                                summand += thisRow[k] * thatColumn[k];
                            }
                            summArr[i][j] = summand;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                }


            }
        }


        final int rowCount = matrixA.length;             // Число строк результирующей матрицы.
        final int colCount = matrixB[0].length;         // Число столбцов результирующей матрицы.
        final int[][] result = new int[rowCount][colCount];  // Результирующая матрица.


        executor = Executors.newFixedThreadPool(MainMatrix.THREAD_NUMBER);

        for(int index = 0; index < 1000;) {


            executor.execute(new Calc(matrixA, matrixB, result, index, index += 100));

        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);



        return result;
    }



    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int columnsA = matrixA[0].length;
        final int rowsA = matrixA.length;
        final int columnsB = matrixB[0].length;
        final int rowsB = matrixB.length;

        final int[][] matrixC = new int[rowsA][columnsB];

        int thatColumn[] = new int[rowsB];

        try {
            for (int j = 0;; j++) {
                for (int k = 0; k < columnsA; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                for (int i = 0; i < rowsA; i++) {
                    int thisRow[] = matrixA[i];
                    int summand = 0;
                    for (int k = 0; k < columnsA; k++) {
                        summand += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = summand;
                }
            }
        } catch (IndexOutOfBoundsException e) { }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
