
/**
 * https://www.hackerrank.com/challenges/flipping-the-matrix
 * Miss leading name. This question is not a traditional flipping the matrix.
 * The question is about finding the maximum possible sum of the elements in the matrix's upper-left quadrant.
 * It presents a unique problem because it gives you guidelines in how the numbers in the matrix can be moved.
 * 
 * In the 4x4 matrix below the rules are illustrated, the rows and columns of the matrix can only be flipped, so
 * you cannot just find the highest numbers in the matrix. The key is to find the highest number across the 
 * corresponding spots. This means the matrix behaves a lot like a rubix cube in the since that a corner space will 
 * always be a corner, a center piece will always be a center and an edge will always be an edge.
 * 
 * In the example below, one must find the highest 'A','B','C', and 'D'. This will give the highest
 * sum for the top left quadrant.     
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
        Scanner in = new Scanner(System.in);
        //number of queries that will be processed
        int q = in.nextInt();
		//lambda funciton that finds the max of two digits, Experimenting with new Lambda functionality in Java8
        BiFunction<Integer, Integer, Integer> max = (c1, c2) -> {
			return c1 > c2 ? c1 : c2;            
        };
        //allows function to continue running until all matrices have been processed from STDIN
        while (q != 0) {
            int n = in.nextInt();
            int sizeOfMatrix = 2 * n;

            int[][] matrix = createMatrix(sizeOfMatrix, in);
            int ans = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    //Utilizing the lambda function created earlier to find the maximum value of the spaces that correspond to one another
                    //ex. all the A's [(0,0)(3,0)(0,3)(3,3)]
                    ans += max.apply(matrix[i][j], 
                                 max.apply(matrix[sizeOfMatrix - i - 1][j],
                                     max.apply(matrix[i][sizeOfMatrix - j - 1], matrix[sizeOfMatrix - i - 1][sizeOfMatrix - j - 1])));
                }
            }
            System.out.println(ans);
            //decrement the number of matrices that must be processed
            q--;
        }
    }

//Read in Matrix from STDIN
    public static int[][] createMatrix(int size, Scanner in) {
        int[][] matrix = new int[size][size];
        int i = 0;
        in.nextLine();
        
        while (i != size) {
            //Read in each line and turn it into a row of the matrix
            String s = in.nextLine();
            matrix[i] = Arrays.stream(s.split(" ")).mapToInt(Integer::parseInt).toArray();
            //number of lines read increase
            i++;
        }
        return matrix;
    }
}
