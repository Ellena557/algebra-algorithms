import java.util.Scanner;

public class Main {
    private static int[][] matrix;
    static int n;
    private static int bigN;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        n = (s.length() + 1) / 2;
        bigN = getBigN();
        matrix = new int[bigN][bigN];
        for (int i = 0; i < n; i++) {
            matrix[0][i] = Integer.parseInt(s.substring(2 * i, 2 * i + 1));
        }
        if (n > 1) {
            for (int i = 0; i < n - 1; i++) {
                String p = scanner.nextLine();
                for (int j = 0; j < n; j++) {
                    matrix[i + 1][j] = Integer.parseInt(p.substring(2 * j, 2 * j + 1));
                }
            }
        }

        if (n < bigN) {
            for (int i = 0; i < bigN; i++) {
                for (int j = 0; j < bigN; j++) {
                    if (i >= n || j >= n) {
                        if (i == j) {
                            matrix[i][j] = 1;
                        } else {
                            matrix[i][j] = 0;
                        }
                    }
                }
            }
        }
        int[][] res = getStrassenPow2(n);
        printMatrix(res);
    }


    private static int[][] getStrassenPow2(int m) {
        if (m == 1) {
            return matrix;
        }
        int curN = (int) (Math.log(m) / Math.log(2)) + 1;
        int k = m;
        int[] powers = new int[curN];
        for (int i = 0; i < curN; i++) {
            powers[i] = k % 2;
            k = k / 2;
        }
        int[][] res = new int[bigN][bigN];
        if (powers[0] == 1) {
            res = matrix;
        } else {
            for (int i = 0; i < bigN; i++) {
                for (int j = 0; j < bigN; j++) {
                    if (i == j) {
                        res[i][j] = 1;
                    } else {
                        res[i][j] = 0;
                    }
                }
            }
        }
        int[][] curMatrix = matrix;
        if (curN > 1) {
            for (int i = 1; i < curN; i++) {
                curMatrix = doStrassen(curMatrix, curMatrix);
                if (powers[i] == 1) {
                    res = doStrassen(res, curMatrix);
                }
            }
        }
        return res;
    }


    private static int getBigN() {
        int k = (int) ((Math.log(n)) / Math.log(2) + 1);
        for (int i = 0; i < k; i++) {
            if (n == Math.pow(2, i)) {
                return n;
            }
        }
        return (int) Math.pow(2, k);
    }


    private static void printMatrix(int[][] m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j < n - 1) {
                    System.out.print(m[i][j] + " ");
                } else {
                    System.out.print(m[i][j] + "");
                }
            }
            System.out.println();
        }
    }


    private static int[][] doStrassen(int[][] a, int[][] b) {
        int curN = a.length;
        int[][] c;

        if (curN > 1) {
            c = new int[curN][curN];
            int[][] a11 = new int[curN / 2][curN / 2];
            int[][] a12 = new int[curN / 2][curN / 2];
            int[][] a21 = new int[curN / 2][curN / 2];
            int[][] a22 = new int[curN / 2][curN / 2];
            int[][] b11 = new int[curN / 2][curN / 2];
            int[][] b12 = new int[curN / 2][curN / 2];
            int[][] b21 = new int[curN / 2][curN / 2];
            int[][] b22 = new int[curN / 2][curN / 2];


            for (int i = 0; i < curN / 2; i++) {
                for (int j = 0; j < curN / 2; j++) {
                    a11[i][j] = a[i][j];
                    a12[i][j] = a[i + curN / 2][j];
                    a21[i][j] = a[i][j + curN / 2];
                    a22[i][j] = a[i + curN / 2][j + curN / 2];

                    b11[i][j] = b[i][j];
                    b12[i][j] = b[i + curN / 2][j];
                    b21[i][j] = b[i][j + curN / 2];
                    b22[i][j] = b[i + curN / 2][j + curN / 2];
                }
            }

            int[][] p1 = doStrassen(sumMatrix8(a11, a22), sumMatrix8(b11, b22));
            int[][] p2 = doStrassen(sumMatrix8(a21, a22), b11);
            int[][] p3 = doStrassen(a11, subMatrix8(b12, b22));
            int[][] p4 = doStrassen(a22, subMatrix8(b21, b11));
            int[][] p5 = doStrassen(sumMatrix8(a11, a12), b22);
            int[][] p6 = doStrassen(subMatrix8(a21, a11), sumMatrix8(b11, b12));
            int[][] p7 = doStrassen(subMatrix8(a12, a22), sumMatrix8(b21, b22));

            for (int i = 0; i < curN / 2; i++) {
                for (int j = 0; j < curN / 2; j++) {
                    c[i][j] = sum8(sum8(p1[i][j], p4[i][j]), sub8(p7[i][j], p5[i][j]));
                    c[i + curN / 2][j] = sum8(p3[i][j], p5[i][j]);
                    c[i][j + curN / 2] = sum8(p2[i][j], p4[i][j]);
                    c[i + curN / 2][j + curN / 2] = sum8(sub8(p1[i][j], p2[i][j]), sum8(p3[i][j], p6[i][j]));
                }
            }
        } else {
            c = new int[1][1];
            c[0][0] = a[0][0] * b[0][0];
            return c;
        }
        return c;
    }


    private static int sum8(int x, int y) {
        return (x + y) % 9;
    }


    private static int sub8(int x, int y) {
        if ((x - y) % 9 < 0) {
            return 9 + ((x - y) % 9);
        }
        return (x - y) % 9;
    }


    private static int[][] sumMatrix8(int[][] x, int[][] y) {
        int[][] c = new int[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                c[i][j] = sum8(x[i][j], y[i][j]);
            }
        }
        return c;
    }


    private static int[][] subMatrix8(int[][] x, int[][] y) {
        int[][] c = new int[x.length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                c[i][j] = sub8(x[i][j], y[i][j]);
            }
        }
        return c;
    }
}
