import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class Main {
    private static int[][] hasEdges;
    private static int[][] matrix;
    static int n;
    private static int myPrime = 2557;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = Integer.parseInt(scanner.nextLine());
        n = 0;

        hasEdges = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                hasEdges[i][j] = 0;
            }
        }

        for (int i = 0; i < m; i++) {
            String nums = scanner.nextLine();
            String[] realNums = nums.split("\\s+");
            if (Integer.parseInt(realNums[0]) > n) {
                n = Integer.parseInt(realNums[0]);
            }
            if (Integer.parseInt(realNums[1]) > n) {
                n = Integer.parseInt(realNums[1]);
            }
            hasEdges[Integer.parseInt(realNums[0])][Integer.parseInt(realNums[1])] = 1;
        }
        n += 1;
        matrix = new int[n][n];

        estimatePps();
    }


    private static void fillMatrix() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (hasEdges[i][j] == 0) {
                    matrix[i][j] = 0;
                } else {
                    matrix[i][j] = ThreadLocalRandom.current().nextInt(1, myPrime);
                }
            }
        }
    }


    private static void estimatePps() {
        int timesToPass = 4;
        int res = 0;

        for (int i = 0; i < timesToPass; i++) {
            fillMatrix();
            if (isInvertible() > 0) {
                res = 1;
            }
        }

        if (res == 1) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }


    private static int isInvertible() {
        int idx;
        int tmp;
        int curRow = 0;
        for (int j = 0; j < n; j++) {
            idx = curRow;
            for (int i = curRow; i < n; i++) {
                if (matrix[i][j] != 0) {
                    idx = i;
                }
            }

            if (matrix[idx][j] != 0) {
                if (idx != curRow) {
                    for (int i = 0; i < n; i++) {
                        tmp = matrix[curRow][i];
                        matrix[curRow][i] = matrix[idx][i];
                        matrix[idx][i] = tmp;
                    }
                }

                int inv = findInversed(matrix[curRow][j]);
                int factor;

                for (int i = curRow + 1; i < n; i++) {
                    if (matrix[i][j] != 0) {
                        factor = doMult(matrix[i][j], inv);
                        for (int k = 0; k < n; k++) {
                            int s = doSub(matrix[i][k], doMult(factor, matrix[curRow][k]));
                            matrix[i][k] = s;
                        }
                    }
                }
                curRow += 1;
            }
        }

        for (int i = 0; i < n; i++) {
            if (matrix[n - 1][i] > 0) {
                return 1;
            }
        }

        return 0;
    }


    private static int doSub(int n1, int n2) {
        if (n1 - n2 >= 0) {
            return n1 - n2;
        } else {
            return myPrime + n1 - n2;
        }
    }


    private static int doMult(int n1, int n2) {
        return (n1 * n2) % myPrime;
    }


    private static int findInversed(int a) {
        for (int i = 0; i < myPrime; i++) {
            if (doMult(i, a) == 1) {
                return i;
            }
        }
        return 1;
    }
}
