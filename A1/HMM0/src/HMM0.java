import java.util.Arrays;
import java.util.Scanner;

public class HMM0 {

    Scanner sc = new Scanner(System.in);

    public double[][] getMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();

        double[][] matrix = new double[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = sc.nextDouble();
                }
            }

            return matrix;
        }

        public double[][] matrixMul(double[][] matrix1, double[][] matrix2) {
            double[][] result = new double[matrix1.length][matrix2[0].length];
            for (int i = 0; i < matrix1.length; i++) {
                for (int j = 0; j < matrix2[0].length; j++) {
                    for (int k = 0; k < matrix1[0].length; k++) {
                        result[i][j] += matrix1[i][k] * matrix2[k][j];
                    }
                }
            }
            return result;
        }


        public void printResult(double[][] result){
            System.out.print(result.length + " " + result[0].length);
            for(int i = 0; i < result.length; i++){
                for(int j = 0; j < result[0].length; j++){
                    System.out.print(" " + result[i][j]);
                }
            }

        }

    public static void main(String[] args) {
        HMM0 hmm0 = new HMM0();
        double [][] matrixTrans = hmm0.getMatrix();
        double [][] matrixEmiss = hmm0.getMatrix();
        double [][] matrixInit = hmm0.getMatrix();



         //double[][] res = hmm0.matrixMul(matrixInit, matrixTrans);
        /*for(int i = 0; i < res.length; i++){
            System.out.println(Arrays.toString(res[0]));
        }*/


        double [][] result = hmm0.matrixMul(hmm0.matrixMul(matrixInit, matrixTrans), matrixEmiss);
        hmm0.printResult(result);
    }
}
