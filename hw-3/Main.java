import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static ArrayList<String> resStr;
    private static List<List<Integer>> allCodes;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        resStr = new ArrayList<>();
        findUniversalPoluses(n);
        for (String s : resStr) {
            System.out.println(s);
        }
    }


    private static void codeNums(int n) {
        int strLen = (int) Math.pow(2, n);
        int padLen = strLen / 2;
        ArrayList<Integer> curStr = new ArrayList<>();

        for (int i = 0; i < padLen; i++) {
            curStr.add(0);
            curStr.add(1);
        }
        allCodes.add(curStr);

        for (int i = 0; i < n - 1; i++) {
            curStr = new ArrayList<>();
            for (int j = 0; j < strLen / (padLen * 2); j++) {
                for (int k = 0; k < padLen; k++) {
                    curStr.add(0);
                }
                for (int k = 0; k < padLen; k++) {
                    curStr.add(1);
                }
            }
            allCodes.add(curStr);
            padLen /= 2;
        }

        for (int i = n; i < 2 * n; i++) {
            curStr = new ArrayList<>();
            for (int j = 0; j < strLen; j++) {
                curStr.add(1 - allCodes.get(i - n).get(j));
            }
            allCodes.add(curStr);
        }
    }


    private static void findUniversalPoluses(int n) {
        // current gate position: 0..n-1 are input ones
        int curPos = n;
        for (int i = 0; i < n; i++) {
            printNot(curPos, i);
            curPos += 1;
        }

        int formulaLen = (int) Math.pow(2, Math.pow(2, n));
        allCodes = new ArrayList<>(formulaLen);
        codeNums(n);

        int andPos = doAndGates(n);
        doOrGates(n, andPos);

        for (int i = 0; i < formulaLen; i++) {
            printOutput(i, i);
        }
    }


    private static ArrayList<Integer> doAndFunction(List<Integer> code1, List<Integer> code2) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < code1.size(); i++) {
            if (code1.get(i) == 1 && code2.get(i) == 1) {
                res.add(1);
            } else {
                res.add(0);
            }
        }
        return res;
    }


    private static int doAndGates(int n) {
        // current gate position
        int curPos = 2 * n;

        printAnd(curPos, 0, n);
        allCodes.add(doAndFunction(allCodes.get(0), allCodes.get(n)));
        curPos += 1;

        for (int dnfSize = 0; dnfSize < n; dnfSize++) {
            int curLen = curPos;
            for (int i = 0; i < curLen - 1; i++) {
                for (int j = i + 1; j < curLen; j++) {
                    ArrayList<Integer> curDnf = doAndFunction(allCodes.get(i), allCodes.get(j));

                    if (!allCodes.contains(curDnf)) {
                        printAnd(curPos, i, j);
                        curPos += 1;
                        allCodes.add(curDnf);
                    }
                }
            }
        }

        return curPos - 1;
    }


    private static ArrayList<Integer> doOrFunction(List<Integer> code1, List<Integer> code2) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < code1.size(); i++) {
            if (code1.get(i) == 0 && code2.get(i) == 0) {
                res.add(0);
            } else {
                res.add(1);
            }
        }
        return res;
    }


    private static void doOrGates(int n, int lastAndPos) {
        int curPos = lastAndPos + 1;
        int treeHeight = n / 2 + 1;
        for (int height = 0; height < treeHeight; height++) {
            int curLen = curPos;

            for (int i = 0; i < curLen - 1; i++) {
                for (int j = i + 1; j < curLen; j++) {
                    ArrayList<Integer> allPair = doOrFunction(allCodes.get(i), allCodes.get(j));
                    if (!allCodes.contains(allPair)) {
                        printOr(curPos, i, j);
                        curPos += 1;
                        allCodes.add(allPair);
                    }
                }
            }
        }
    }


    private static void printOr(int gateNum, int in1, int in2) {
        String res = "GATE " + gateNum + " OR " + in1 + " " + in2;
        resStr.add(res);
    }


    private static void printAnd(int gateNum, int in1, int in2) {
        String res = "GATE " + gateNum + " AND " + in1 + " " + in2;
        resStr.add(res);
    }


    private static void printNot(int gateNum, int in1) {
        String res = "GATE " + gateNum + " NOT " + in1;
        resStr.add(res);
    }


    private static void printOutput(int outNum, int valNum) {
        String res = "OUTPUT " + outNum + " " + valNum;
        resStr.add(res);
    }
}
