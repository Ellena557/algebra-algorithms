import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(in);
        String s = scanner.nextLine();
        int n = (s.length() + 1) / 2;
        int bigN = getBigN(n);
        int[][] matrix = new int[bigN][bigN];
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

        ArrayList<int[][]> res = findNvp(matrix, bigN, bigN);
        printPartMatrix(res.get(0), n);
        printPartMatrix(res.get(1), n);
        printPartMatrix(res.get(2), n);
    }


    private static void printPartMatrix(int[][] m, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j < n - 1) {
                    out.print(m[i][j] + " ");
                } else {
                    out.print(m[i][j] + "");
                }
            }
            out.println();
        }
    }


    private static int[][] constructP(int size, int colNum) {
        int[][] matP = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matP[i][j] = 1;
                } else {
                    matP[i][j] = 0;
                }
            }
        }
        matP[0][0] = 0;
        matP[colNum][colNum] = 0;
        matP[0][colNum] = 1;
        matP[colNum][0] = 1;
        return matP;
    }


    private static int[][] getPrimeMat(int n) {
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    mat[i][j] = 1;
                } else {
                    mat[i][j] = 0;
                }
            }
        }
        return mat;
    }


    private static int[][] invertP(int[][] matP) {
        int p = matP.length;
        int[][] res = new int[p][p];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < p; j++) {
                res[i][j] = matP[j][i];
            }
        }
        return res;
    }


    private static int[][] minusMatrix(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res[i][j] = sub2(0, mat[i][j]);
            }
        }
        return res;
    }


    private static int isZeroMatrix(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] != 0) {
                    return 0;
                }
            }
        }
        return 1;
    }


    private static int[][] invertMatrix(int[][] mat) {
        int n = mat.length;
        int[][] res = new int[n][n];
        if (n == 1) {
            return mat;
        } else {
            int m2 = n / 2;
            int m1 = n - m2;
            int[][] mat11 = new int[m1][m1];
            int[][] mat12 = new int[m2][m2];
            int[][] mat21 = new int[m2][m2];
            int[][] mat22 = new int[m1][m1];
            for (int i = 0; i < m1; i++) {
                for (int j = 0; j < m1; j++) {
                    mat11[i][j] = mat[i][j];
                    mat22[i][j] = mat[i + m2][j + m2];
                }
            }
            for (int i = 0; i < m2; i++) {
                for (int j = 0; j < m2; j++) {
                    mat21[i][j] = mat[i + m1][j];
                    mat12[i][j] = mat[i][j + m1];
                }
            }

            int[][] invMat11 = invertMatrix(mat11);
            int[][] invMat22 = invertMatrix(mat22);

            int isUpperTriangle = isZeroMatrix(mat21);
            for (int i = 0; i < m1; i++) {
                for (int j = 0; j < m1; j++) {
                    res[i][j] = invMat11[i][j];
                    res[i + m2][j + m2] = invMat22[i][j];
                }
            }
            if (isUpperTriangle == 1) {
                int[][] resMat = minusMatrix(matrixMult(matrixMult(invMat11, mat12), invMat22));
                for (int i = 0; i < m2; i++) {
                    for (int j = 0; j < m2; j++) {
                        res[i][j + m1] = resMat[i][j];
                        res[i + m1][j] = mat21[i][j];
                    }
                }
            } else {
                int[][] resMat = minusMatrix(matrixMult(matrixMult(invMat22, mat21), invMat11));
                for (int i = 0; i < m2; i++) {
                    for (int j = 0; j < m2; j++) {
                        res[i][j + m1] = mat12[i][j];
                        res[i + m1][j] = resMat[i][j];
                    }
                }
            }

            return res;
        }
    }


    private static ArrayList<int[][]> findNvp(int[][] mat, int m, int p) {
        ArrayList<int[][]> answer = new ArrayList<>();
        int[][] matL = new int[m][m];
        int[][] matU = new int[m][p];
        int[][] matP;

        if (m == 1) {
            matL[0][0] = 1;

            if (mat[0][0] == 0) {
                int idx = 0;
                for (int i = 0; i < p; i++) {
                    if (mat[0][i] == 1) {
                        idx = i;
                        break;
                    }
                }
                matP = constructP(p, idx);
            } else {
                matP = getPrimeMat(p);
            }
            matU = matrixMult(mat, matP);
        } else {
            int m2 = m / 2;
            int m1 = m - m2;
            int[][] matB = new int[m1][p];
            int[][] matC = new int[m2][p];

            for (int i = 0; i < m1; i++) {
                for (int j = 0; j < p; j++) {
                    matB[i][j] = mat[i][j];
                }
            }

            for (int i = 0; i < m2; i++) {
                arraycopy(mat[i + m1], 0, matC[i], 0, p);
            }

            ArrayList<int[][]> prevRes = findNvp(matB, m1, p);
            int[][] matL1 = prevRes.get(0);
            int[][] matU1 = prevRes.get(1);
            int[][] matP1 = prevRes.get(2);

            int[][] matD = matrixMult(matC, invertP(matP1));
            int[][] matE = new int[m1][m1];
            int[][] matF = new int[m2][m1];

            for (int i = 0; i < m1; i++) {
                for (int j = 0; j < m1; j++) {
                    matE[i][j] = matU1[i][j];
                }
            }

            for (int i = 0; i < m2; i++) {
                for (int j = 0; j < m1; j++) {
                    matF[i][j] = matD[i][j];
                }
            }

            int[][] matG = subMatrix2(matD, matrixMult(matrixMult(matF, invertMatrix(matE)), matU1));
            int[][] matGg = new int[m2][p - m1];
            for (int i = 0; i < m2; i++) {
                for (int j = 0; j < p - m1; j++) {
                    matGg[i][j] = matG[i][j + m1];
                }
            }

            ArrayList<int[][]> curRes = findNvp(matGg, m2, p - m1);
            int[][] matL2 = curRes.get(0);
            int[][] matU2 = curRes.get(1);
            int[][] matP2 = curRes.get(2);

            int[][] matP3 = new int[p][p];
            for (int i = 0; i < m1; i++) {
                matP3[i][i] = 1;

            }
            for (int i = 0; i < p - m1; i++) {
                for (int j = 0; j < p - m1; j++) {
                    matP3[i + m1][j + m1] = matP2[i][j];
                }
            }

            int[][] matH = matrixMult(matU1, invertP(matP3));

            // construct L
            for (int i = 0; i < m1; i++) {
                for (int j = 0; j <= i; j++) {
                    matL[i][j] = matL1[i][j];
                }
            }
            int[][] multL = matrixMult(matF, invertMatrix(matE));
            for (int i = 0; i < m2; i++) {
                for (int j = 0; j < m1; j++) {
                    matL[i + m1][j] = multL[i][j];
                }
            }
            for (int i = 0; i < m2; i++) {
                for (int j = 0; j <= i; j++) {
                    matL[i + m1][j + m1] = matL2[i][j];
                }
            }

            // construct U
            for (int i = 0; i < m1; i++) {
                for (int j = i; j < p; j++) {
                    matU[i][j] = matH[i][j];
                }
            }
            for (int i = 0; i < m2; i++) {
                for (int j = i; j < p - m1; j++) {
                    matU[i + m1][j + m1] = matU2[i][j];
                }
            }

            // construct P
            matP = matrixMult(matP3, matP1);
        }

        answer.add(matL);
        answer.add(matU);
        answer.add(matP);
        return answer;
    }


    private static int getBigN(int n) {
        int k = (int) ((Math.log(n)) / Math.log(2) + 1);
        for (int i = 0; i < k; i++) {
            if (n == Math.pow(2, i)) {
                return n;
            }
        }
        return (int) Math.pow(2, k);
    }


    private static int[][] expandMatrix(int[][] a, int bigN) {
        int[][] matA = new int[bigN][bigN];

        int curN = a.length;
        int curM = a[0].length;
        int n = curN;
        if (curM > curN) {
            n = curM;
        }

        for (int i = 0; i < curN; i++) {
            for (int j = 0; j < curM; j++) {
                matA[i][j] = a[i][j];
            }
        }

        if (n < bigN) {
            for (int i = 0; i < bigN; i++) {
                for (int j = 0; j < bigN; j++) {
                    if (i >= curN || j >= curM) {
                        if (i == j) {
                            matA[i][j] = 1;
                        } else {
                            matA[i][j] = 0;
                        }
                    }
                }
            }
        }

        return matA;
    }


    private static int[][] matrixMult(int[][] a, int[][] b) {
        int curN = a.length;
        int curM = a[0].length;
        int n = curN;
        if (curM > curN) {
            n = curM;
        }
        int bigN = getBigN(n);

        int curK = b.length;
        int curT = b[0].length;
        int m = curK;
        if (curT > curK) {
            m = curT;
        }
        int bigM = getBigN(m);

        if (bigN < bigM) {
            bigN = bigM;
        } else {
            bigM = bigN;
        }

        int[][] matA = expandMatrix(a, bigN);

        int[][] matB = expandMatrix(b, bigM);

        int[][] mult = doStrassen(matA, matB);
        int[][] res = new int[curN][curT];
        for (int i = 0; i < curN; i++) {
            for (int j = 0; j < curT; j++) {
                res[i][j] = mult[i][j];
            }
        }
        return res;
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
                    a12[i][j] = a[i][j + curN / 2];
                    a21[i][j] = a[i + curN / 2][j];
                    a22[i][j] = a[i + curN / 2][j + curN / 2];

                    b11[i][j] = b[i][j];
                    b12[i][j] = b[i][j + curN / 2];
                    b21[i][j] = b[i + curN / 2][j];
                    b22[i][j] = b[i + curN / 2][j + curN / 2];
                }
            }

            int[][] p1 = doStrassen(sumMatrix2(a11, a22), sumMatrix2(b11, b22));
            int[][] p2 = doStrassen(sumMatrix2(a21, a22), b11);
            int[][] p3 = doStrassen(a11, subMatrix2(b12, b22));
            int[][] p4 = doStrassen(a22, subMatrix2(b21, b11));
            int[][] p5 = doStrassen(sumMatrix2(a11, a12), b22);
            int[][] p6 = doStrassen(subMatrix2(a21, a11), sumMatrix2(b11, b12));
            int[][] p7 = doStrassen(subMatrix2(a12, a22), sumMatrix2(b21, b22));

            for (int i = 0; i < curN / 2; i++) {
                for (int j = 0; j < curN / 2; j++) {
                    c[i][j] = sum2(sum2(p1[i][j], p4[i][j]), sub2(p7[i][j], p5[i][j]));
                    c[i][j + curN / 2] = sum2(p3[i][j], p5[i][j]);
                    c[i + curN / 2][j] = sum2(p2[i][j], p4[i][j]);
                    c[i + curN / 2][j + curN / 2] = sum2(sub2(p1[i][j], p2[i][j]), sum2(p3[i][j], p6[i][j]));
                }
            }
        } else {
            c = new int[1][1];
            c[0][0] = a[0][0] * b[0][0];
            return c;
        }
        return c;
    }


    private static int sum2(int x, int y) {
        return (x + y) % 2;
    }


    private static int sub2(int x, int y) {
        return (2 + x - y) % 2;
    }


    private static int[][] sumMatrix2(int[][] x, int[][] y) {
        int[][] c = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                c[i][j] = sum2(x[i][j], y[i][j]);
            }
        }
        return c;
    }


    private static int[][] subMatrix2(int[][] x, int[][] y) {
        int[][] c = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                c[i][j] = sub2(x[i][j], y[i][j]);
            }
        }
        return c;
    }
}
