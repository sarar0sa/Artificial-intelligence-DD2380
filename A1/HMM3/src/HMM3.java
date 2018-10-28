import java.util.Scanner;
import java.lang.Math;

public class HMM3 {

    Scanner sc = new Scanner(System.in);

    double[] c;
    double[][][] digamma;
    double[][] gamma;
    double[][] alpha;
    double beta[][];

    int maxIters = 100;
    int iters = 0;
    double oldLogProb = Double.NEGATIVE_INFINITY;

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


    public void alphapass(int[] o, double[][] B, double[][] A, double [][] pi) {
        alpha = new double[o.length][A.length];
        c = new double[o.length];
        for (int k = 0; k < B.length; k++) {
            alpha[0][k] = pi[0][k] * B[k][o[0]];
            c[0] += alpha[0][k];
        }
        //Scaling alpha0
        c[0] = 1/c[0];
        for(int i = 0; i < alpha[0].length; i++){
            alpha[0][i] = c[0] * alpha[0][i];
        }

        for(int n = 1; n < o.length; n++) {
            c[n] = 0;
            for (int i = 0; i < A.length; i++) {
                double sum = 0;
                for (int j = 0; j < A.length; j++) {
                    sum += A[j][i] * alpha[n-1][j];
                }
                alpha[n][i] = sum * B[i][o[n]];
                c[n] += alpha[n][i];
            }

            // Scaling alphaN
            c[n] = 1/c[n];
            for(int k = 0; k < A.length; k++){
                alpha[n][k] = alpha[n][k] * c[n];
            }
        }
        betaPass(A, B, o, pi);
    }


    public void betaPass(double[][]A, double[][]B, int[] o, double[][] pi){
        beta = new double[o.length][A.length];

        for(int i = 0; i < A.length; i++){
            beta[o.length - 1][i] = c[o.length - 1];
        }

        for(int t = o.length - 2; t > 0; t--){
            for(int i = 0; i < A.length; i++){
                beta[t][i] = 0;
                for(int j = 0; j < A.length; j++){
                    beta[t][i] +=  A[i][j] * beta[t+1][j] * B[j][o[t+1]];
                }
                beta[t][i] = beta[t][i] * c[t];
            }
        }

        gamma(A, B, o, pi);
    }

    // Given the entire observations sequence and the current estimate of the HMM,
    // What is the probability that att time (t) the hidden state is (Xt = i) and at time (t+1)
    // the hidden state is (Xt = j)

    // Given the entire observations sequence and current estimate of the HMM, what is the
    // probability that at time (t) the hidden state is (Xt = i)

    public void gamma(double[][]A, double[][]B, int[] o, double[][] pi){
        digamma = new double[o.length][A.length][A.length];
        gamma  = new double[o.length][A.length];
        double denom = 0;

       for(int i = 0; i < alpha[0].length; i++){
            denom += alpha[alpha[0].length - 1][i];
        }

        for(int t = 0; t < o.length - 1; t++){
            for(int i = 0; i < A.length; i++){
                for(int j = 0; j < A.length; j++){
                    digamma[t][i][j] = (alpha[t][i] * A[i][j] * B[j][o[t+1]] * beta[t+1][j])/denom;
                    gamma[t][i] +=  digamma[t][i][j];
                }
            }

        }

        // Special case
        denom = 0;
        for(int i = 0; i < A.length; i++){
            denom += alpha[o.length-1][i];
        }

        for(int i = 0; i < A.length; i++){
            gamma[o.length -1][i] = gamma[o.length -1][i]/denom;
        }

        reEstimate(A, B, o, pi);

    }

    public void reEstimate(double[][]A, double[][]B, int[] o, double[][] pi){
        for(int i = 0; i < A.length; i++){
            pi[0][i] = gamma[0][i];
        }


        // Re-estimate A
        // numer = # transitions from state i to j
        // denom = # transitions from stat i to (don't care)
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A.length; j++){
                double numer = 0;
                double denom = 0;
                for(int t = 0; t < o.length - 1; t++){
                    numer += digamma[t][i][j];
                    denom += gamma[t][i];
                }
                A[i][j] = numer / denom;
            }
        }

        // Re-estimate B
        // numer = # emissions (k) from state (i)
        // denom = # of transistions from state (i) to state (don't care)
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < B[0].length; j++){
                double numer = 0;
                double denom = 0;
                for(int t = 0; t < o.length; t++){
                    if(o[t] == j){
                        numer += gamma[t][i];
                    }
                    denom += gamma[t][i];
                }
                    B[i][j] = numer / denom;
            }
        }

        // Initial state is gamma[1][i]
        computeLog(A, B, o, pi);
    }

    public void computeLog(double[][]A, double[][]B, int[] o, double[][] pi){
        double logProb = 0;
        for(int i = 0; i < o.length; i++){
            logProb += Math.log(c[i]);
        }

        logProb = -1 * logProb;
        iterateOrNot(A, B, o, pi, logProb);
    }

    public void iterateOrNot(double[][]A, double[][]B, int[] o, double[][] pi, double logProb){
        iters++;
        if((iters < maxIters) && (logProb > oldLogProb)){
            oldLogProb = logProb;
            alphapass(o, B, A, pi);
        }

        else {
            print(A, B, o, pi);

        }
    }

    public void print(double[][]A, double[][]B, int[] o, double[][] pi){
        System.out.print(A.length + " " + A[0].length + " ");
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                System.out.print(A[i][j] + " ");
            }
        }

        System.out.println();

        System.out.print(B.length + " " + B[0].length + " ");
        for(int i = 0; i < B.length; i++){
            for(int j = 0; j < B[0].length; j++){
                System.out.print(B[i][j] + " ");
            }
        }
    }

    public static void main(String[] args) {
        HMM3 hmm3 = new HMM3();
        double [][] A = hmm3.getMatrix();
        double [][] B = hmm3.getMatrix();
        double [][] pi = hmm3.getMatrix();
        int [] o = hmm3.getSeq();
        hmm3.alphapass(o, B, A, pi);

    }
}
