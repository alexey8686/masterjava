package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {


    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {

         class Multi implements Runnable {
            final int N;
            final int [][] a;
            final int [][] b;
            final int [][] c;
            final int i;
            final int j;

            public Multi(int N, int i, int j, int[][] a, int[][] b, int[][] c){
                this.N=N;
                this.i=i;
                this.j=j;
                this.a=a;
                this.b=b;
                this.c=c;
            }

            @Override
            public void run() {
                for(int k = 0; k < N; k++)
                    c[i][j] += a[i][k] * b[k][j];
            }
        }
        executor = Executors.newFixedThreadPool(10);
        int N = matrixA.length;
        final  int[][] matrixC = new int[1000][1000];
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                executor.submit(new Multi(N,i,j,matrixA,matrixB,matrixC));
            }
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);


        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
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
