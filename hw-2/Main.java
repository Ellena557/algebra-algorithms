import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        doTrick(n);
    }


    private static void doTrick(int n) {
        ArrayList<Integer> xVals = new ArrayList<>();
        ArrayList<Integer> yVals = new ArrayList<>();
        int zeroGate = doTrickForFirst(n, xVals, yVals);
        for (int i = 0; i < n - 1; i++) {
            doTrickForBytes(n, i + 1, xVals, yVals);
        }

        int outGate = 0;
        // output for X

        for (int i = 0; i < n; i++) {
            printOutput(outGate, xVals.get(i));
            outGate += 1;
        }

        printOutput(outGate, zeroGate);
        outGate += 1;

        printOutput(outGate, zeroGate);
        outGate += 1;

        for (int i = 0; i < n; i++) {
            printOutput(outGate, yVals.get(i));
            outGate += 1;
        }
    }


    private static int doTrickForFirst(int n, ArrayList<Integer> xVals, ArrayList<Integer> yVals) {
        int numA = 0;
        int numB = numA + n;
        int numC = numA + 2 * n;
        int firstGate = 3 * n;

        // a_i and b_i: gate_1
        printAnd(firstGate, numA, numB);

        // a_i or b_i: gate_2
        printOr(firstGate + 1, numA, numB);

        // gate_2 and c_i: gate_3
        printAnd(firstGate + 2, firstGate + 1, numC);

        // gate_1 or gate_3: gate_4
        printOr(firstGate + 3, firstGate, firstGate + 2);

        // Gate_4 is OUTPUT for y: its number
        yVals.add(firstGate + 3);

        // not gate_1 : gate_5
        printNot(firstGate + 4, firstGate);

        // gate_5 and gate_2: gate_6
        printAnd(firstGate + 5, firstGate + 4, firstGate + 1);

        // gate_6 or c_i: gate_7
        printOr(firstGate + 6, firstGate + 5, numC);

        // not gate_3: gate_8
        printNot(firstGate + 7, firstGate + 2);

        // gate_8 or gate_1: gate_9
        printOr(firstGate + 8, firstGate + 7, firstGate);

        // gate_9 and gate_7: gate_10
        printAnd(firstGate + 9, firstGate + 8, firstGate + 6);

        // Gate_10 is OUTPUT for x
        xVals.add(firstGate + 9);

        // create a zero-gate
        printAnd(firstGate + 10, firstGate, firstGate + 4);

        return firstGate + 10;
    }


    private static void doTrickForBytes(int n, int numPair, ArrayList<Integer> xVals, ArrayList<Integer> yVals) {
        int numA = numPair;
        int numB = numA + n;
        int numC = numA + 2 * n;
        int firstGate = 3 * n + 1 + 10 * numPair; 

        // a_i and b_i: gate_1
        printAnd(firstGate, numA, numB);

        // a_i or b_i: gate_2
        printOr(firstGate + 1, numA, numB);

        // gate_2 and c_i: gate_3
        printAnd(firstGate + 2, firstGate + 1, numC);

        // gate_1 or gate_3: gate_4
        printOr(firstGate + 3, firstGate, firstGate + 2);

        // Gate_4 is OUTPUT for y: its number
        yVals.add(firstGate + 3);

        // not gate_1 : gate_5
        printNot(firstGate + 4, firstGate);

        // gate_5 and gate_2: gate_6
        printAnd(firstGate + 5, firstGate + 4, firstGate + 1);

        // gate_6 or c_i: gate_7
        printOr(firstGate + 6, firstGate + 5, numC);

        // not gate_3: gate_8
        printNot(firstGate + 7, firstGate + 2);

        // gate_8 or gate_1: gate_9
        printOr(firstGate + 8, firstGate + 7, firstGate);

        // gate_9 and gate_7: gate_10
        printAnd(firstGate + 9, firstGate + 8, firstGate + 6);

        // Gate_10 is OUTPUT for x
        xVals.add(firstGate + 9);
    }


    private static void printOr(int gateNum, int in1, int in2) {
        String res = "GATE " + gateNum + " OR " + in1 + " " + in2;
        System.out.println(res);
    }


    private static void printAnd(int gateNum, int in1, int in2) {
        String res = "GATE " + gateNum + " AND " + in1 + " " + in2;
        System.out.println(res);
    }


    private static void printNot(int gateNum, int in1) {
        String res = "GATE " + gateNum + " NOT " + in1;
        System.out.println(res);
    }


    private static void printOutput(int outNum, int valNum) {
        String res = "OUTPUT " + outNum + " " + valNum;
        System.out.println(res);
    }
}
