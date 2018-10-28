import java.util.Arrays;
import java.util.Scanner;

public class HMM1 {

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

    public double[][] forward(int[] o, double[][] B, double[][] A, double [][] pi) {
        double[][] alpha = new double[o.length][A.length];
            for (int k = 0; k < B.length; k++) {
                alpha[0][k] = pi[0][k] * B[k][o[0]];
            }

        for(int n = 1; n < o.length; n++) {
            for (int i = 0; i < A.length; i++) {
                double sum = 0;
                for (int j = 0; j < A.length; j++) {
                    sum += A[j][i] * alpha[n-1][j];
                }
                alpha[n][i] = sum * B[i][o[n]];
            }
        }

       return alpha;

    }

    public void getProb(double[][] alpha){
        double sum = 0;
        for(int i = 0; i < alpha[0].length; i++) {
                sum += alpha[alpha.length - 1][i];
            }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        HMM1 hmm1 = new HMM1();
        double [][] A = hmm1.getMatrix();
        double [][] B = hmm1.getMatrix();
        double [][] pi = hmm1.getMatrix();
        int [] o = hmm1.getSeq();
        double[][] oi = hmm1.forward(o, B, A, pi);
        hmm1.getProb(oi);
    }
}

