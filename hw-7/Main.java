import java.util.Scanner;


public class Main {
    static int n;
    static int[] legendres;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        buildCodes();
    }


    private static void buildCodes() {
        buildLegendreTable();
        int[][] adamarMatrix = pelyConstruction();
        int[][] codes = buildAdamarCodes(adamarMatrix);
        printCodes(codes);
    }


    private static int[][] pelyConstruction() {
        int[][] res = new int[n][n];

        // fill the first line and column with "1"
        res[0][0] = 1;
        for (int i = 1; i < n; i++) {
            res[0][i] = 1;
            res[i][0] = 1;
        }

        // other lines and columns
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (i != j) {
                    res[i][j] = legendres[sub(i, j)];
                } else {
                    res[i][j] = -1;
                }
            }
        }

        return res;
    }


    private static int[][] buildAdamarCodes(int[][] adamarMatrix) {
        int[][] res = new int[2 * n][n];

        // change "-1" into "0"
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adamarMatrix[i][j] != -1) {
                    res[i][j] = adamarMatrix[i][j];
                } else {
                    res[i][j] = 0;
                }
            }
        }

        // next n lines
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i + n][j] = 1 - res[i][j];
            }
        }

        return res;
    }


    private static void buildLegendreTable() {
        legendres = new int[n - 1];
        legendres[0] = 0;
        for (int i = 1; i < n - 1; i++) {
            legendres[mult(i, i)] = 1;
        }
        for (int i = 1; i < n - 1; i++) {
            if (legendres[i] != 1) {
                legendres[i] = -1;
            }
        }
    }


    private static void printCodes(int[][] m) {
        int n = m.length;
        int k = m[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                System.out.print(m[i][j]);
            }
            System.out.println();
        }
    }


    private static int mult(int x, int y) {
        return (x * y) % (n - 1);
    }

    private static int sub(int x, int y) {
        return (n - 1 + x - y) % (n - 1);
    }
}
