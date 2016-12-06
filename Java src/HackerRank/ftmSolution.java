
/**
 * 
 * Matrix 4x4 ex
 * A B B A
 * C D D C
 * C D D C
 * A B B A
 * 
**/
import java.io.*;
import java.util.*;
import java.util.function.*;

public class ftmSolution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        BiFunction<Integer, Integer, Integer> max = (c1, c2) -> {
            if (c1 > c2) {
                return c1;
            } else {
                return c2;
            }
        };
        while (q != 0) {
            int n = in.nextInt();
            int sizeOfMatrix = 2 * n;

            int[][] matrix = createMatrix(sizeOfMatrix, in);
            int ans = 0;
            //System.out.printf("btw %d and %d, %d is the max", matrix[0][0], matrix[0][1], max.apply(matrix[0][0], matrix[0][1]));
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // System.out.printf("%d , %d---------------------------- \n",i, j);
                    //System.out.printf("%d , %d , %d \n",i, j, matrix[i][j]);
                    //System.out.printf("%d , %d , %d \n",i, sizeOfMatrix-j-1, matrix[i][sizeOfMatrix-j-1]);
                    //System.out.printf("%d , %d , %d \n", sizeOfMatrix-i-1, j, matrix[sizeOfMatrix-i-1][j]);
                    //System.out.printf("%d , %d , %d \n",sizeOfMatrix-i-1, sizeOfMatrix-j-1, matrix[sizeOfMatrix-i-1][sizeOfMatrix-j-1]);   
                    //System.out.printf("Local max %d\n", h);
                    ans += max.apply(matrix[i][j], 
                                 max.apply(matrix[sizeOfMatrix - i - 1][j],
                                     max.apply(matrix[i][sizeOfMatrix - j - 1], matrix[sizeOfMatrix - i - 1][sizeOfMatrix - j - 1])));
                }
            }
            System.out.println(ans);

            //System.out.println(Arrays.deepToString(matrix));
            q--;
        }
    }

    public static int[][] createMatrix(int size, Scanner in) {
        int[][] matrix = new int[size][size];
        int i = 0;
        in.nextLine();
        //System.out.println("starting loop");
        while (i != size) {
            //System.out.printf("in loop iterations {%d}...\n", i);
            String s = in.nextLine();
            matrix[i] = Arrays.stream(s.split(" ")).mapToInt(Integer::parseInt).toArray();
            i++;
            //System.out.println("Ending loop");
        }
        return matrix;
    }
}
