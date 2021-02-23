import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.in;

public class Main {
    private static int numVertices;
    private static int numEdges;

    public static void main(String[] args) {
        ArrayList<Integer> verticeWeights = new ArrayList<>();

        Scanner scanner = new Scanner(in);
        numVertices = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numVertices; i++) {
            verticeWeights.add(Integer.parseInt(scanner.nextLine()));
        }

        numEdges = Integer.parseInt(scanner.nextLine());

        int[][] edges = new int[numEdges][2];

        for (int i = 0; i < numEdges; i++) {
            String nums = scanner.nextLine();
            String[] realNums = nums.split("\\s+");
            int num1 = Integer.parseInt(realNums[0]);
            int num2 = Integer.parseInt(realNums[1]);

            edges[i][0] = num1;
            edges[i][1] = num2;
        }

        ArrayList<Integer> vertexCover = findVertexCover(verticeWeights, edges);
        printVertexCover(vertexCover);
    }


    /*
    Algorithm #2 is realized here
     */
    private static ArrayList<Integer> findVertexCover(ArrayList<Integer> weights, int[][] edges) {
        int[] res = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            res[i] = 0;
        }

        int currentY = 0;

        // visit all edges 
        for (int i = 0; i < numEdges; i++) {
            int ver1 = edges[i][0];
            int ver2 = edges[i][1];

            currentY = findMin(weights.get(ver1) - res[ver1], weights.get(ver2) - res[ver2]);

            res[ver1] += currentY;
            res[ver2] += currentY;
        }

        ArrayList<Integer> vertexCover = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            if (weights.get(i) == res[i]) {
                vertexCover.add(i);
            }
        }

        return vertexCover;
    }


    private static int findMin(int val1, int val2) {
        if (val1 <= val2) {
            return val1;
        } else {
            return val2;
        }
    }


    private static void printVertexCover(ArrayList<Integer> vertexCover) {
        for (int i = 0; i < vertexCover.size(); i++) {
            if (i < vertexCover.size() - 1) {
                System.out.print(vertexCover.get(i) + " ");
            } else {
                System.out.println(vertexCover.get(i));
            }
        }
    }
}
