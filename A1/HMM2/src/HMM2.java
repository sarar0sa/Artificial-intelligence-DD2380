import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HMM2 {

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

    public int[] getSeq() {
        int numOfelements = sc.nextInt();
        int[] seq = new int[numOfelements];

        for(int i = 0; i < numOfelements; i++){
            seq[i] = sc.nextInt();
        }
        return seq;
    }


    public void viterbi(double[][] A, double[][] B, double[][] pi, int[] o){
        double[] delta = new double[o.length];
        double[] maxProb = new double[o.length];
        int[][] argMaxState = new int[A.length][o.length];

        for(int i = 0; i < pi[0].length; i++){
            delta[i] = pi[0][i] * B[i][o[0]];
        }

        for(int j = 1; j < o.length; j++){
            for(int k = 0; k < A.length; k++){
                double result = 0;
                double max = 0;
                for(int l = 0; l < A.length; l++){
                    result = A[l][k] * delta[l] * B[k][o[j]];
                    if (result > max){
                        max = result;
                        argMaxState[k][j] = l;
                        maxProb[k] = max;
                    }
                }
            }

            delta = Arrays.copyOf(maxProb, maxProb.length);
            maxProb = new double[o.length];
        }


        double max = 0;
        int startIndex = 0;
        for(int i = 0; i < delta.length; i++){
             if(delta[i] > max){
                 max = delta[i];
                 startIndex = i;
             }
        }

        getStates(argMaxState, startIndex);
    }

    public void getStates(int[][] argMaxState, int startIndex) {
        int prevState;
        ArrayList<Integer> result = new ArrayList<>();
        result.add(startIndex);

        for(int i = argMaxState[0].length - 1; i > 0; i--){
            prevState = argMaxState[startIndex][i];
            result.add(prevState);
            startIndex = prevState;
        }

        for(int i = result.size() - 1; i >= 0; i--){
            System.out.print(result.get(i) + " ");
        }
    }

    public static void main(String[] args) {
        HMM2 hmm2 = new HMM2();
        double [][] A = hmm2.getMatrix();
        double [][] B = hmm2.getMatrix();
        double [][] pi = hmm2.getMatrix();
        int [] o = hmm2.getSeq();
        hmm2.viterbi(A,B,pi,o);
    }
}
