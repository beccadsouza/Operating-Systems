package OS.prac3;
import java.util.*;

/*
* Created by Rebecca D'souza on 16/02/18
* */

public class Sudoku {
    public static ArrayList<ArrayList<Integer>> grid = new ArrayList<ArrayList<Integer>>();
    /*
    * FLAG is used to determine the validity of the sudoku grid solution
    * If at any instant while checking, the grid proves to be wrong then
    * the FLAG is set as false.
    * */
    public static boolean FLAG = true;
    public static void main(String args[]){
    /*
    * Correct Sudoku solution with rows columns and boxes all correct.
    * */
        int arr[][]= {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        /*
         * Incorrect Sudoku solution with rows correct but columns and
         * consequently boxes incorrect as well. Validation stops once
         * columns check has been proved to be false.
         * */

//        int arr[][]= {
//            {5, 3, 4, 6, 7, 8, 9, 1, 2},
//            {6, 7, 2, 1, 9, 5, 3, 4, 8},
//            {1, 9, 8, 3, 4, 2, 5, 6, 7},
//            {8, 5, 9, 7, 6, 1, 4, 2, 3},
//            {4, 2, 6, 8, 5, 3, 7, 9, 1},
//            {7, 1, 3, 9, 2, 4, 8, 5, 6},
//            {9, 6, 1, 5, 3, 7, 2, 8, 4},
//            {2, 8, 7, 4, 1, 9, 6, 3, 5},
//            {3, 4, 9, 2, 8, 6, 1, 7, 5}
//        };

        /*
         * Incorrect Sudoku solution with rows as well as columns correct
         * but boxes incorrect. This shows that boxes validation is required
         * along with rows and columns validation.
         * */

//        int arr[][] = {
//                {1, 2, 3, 4, 5, 6, 7, 8, 9},
//                {2, 3, 4, 5, 6, 7, 8, 9, 1},
//                {3, 4, 5, 6, 7, 8, 9, 1, 2},
//                {4, 5, 6, 7, 8, 9, 1, 2, 3},
//                {5, 6, 7, 8, 9, 1, 2, 3, 4},
//                {6, 7, 8, 9, 1, 2, 3, 4, 5},
//                {7, 8, 9, 1, 2, 3, 4, 5, 6},
//                {8, 9, 1, 2, 3, 4, 5, 6, 7},
//                {9, 1, 2, 3, 4, 5, 6, 7, 8}
//        };

        System.out.println("\nPossible Solution : \n");
        /*
        * Conversion of 2D array to 2D arraylist ie an
        * arraylist of arraylists.
        * */
        for(int i = 0;i<9;i++){
            ArrayList<Integer> al = new ArrayList<>();
            for(int j = 0;j<9;j++) {
                al.add(arr[i][j]);
                System.out.print(arr[i][j]+"  ");
            }
            grid.add(al);
            System.out.println();
        }
        System.out.println();
        /*
        * Thread created to validate all 9 rows.
        * */
        rowChecker R = new rowChecker();
        R.run();
        if(FLAG){
            /*
            * Since the row validation proved to be correct
            * another thread is created to validate all 9
            * columns.
            * */
            colChecker C = new colChecker();
            C.run();
        }
        if(FLAG){
            /*
             * Since the row as well as column validation proved
             * to be correct 9 other threads are created to validate all 9
             * boxes.
             * */
            boxChecker B1 = new boxChecker(0,0);
            B1.run();
            boxChecker B2 = new boxChecker(0,3);
            B2.run();
            boxChecker B3 = new boxChecker(0,6);
            B3.run();
            boxChecker B4 = new boxChecker(3,0);
            B4.run();
            boxChecker B5 = new boxChecker(3,3);
            B5.run();
            boxChecker B6 = new boxChecker(3,6);
            B6.run();
            boxChecker B7 = new boxChecker(6,0);
            B7.run();
            boxChecker B8 = new boxChecker(6,3);
            B8.run();
            boxChecker B9 = new boxChecker(6,6);
            B9.run();
        }
        if(FLAG)
            System.out.println("\nSudoku Solution is correct.");
        else System.out.println("\nSudoku Solution is incorrect.");
    }
}

class rowChecker extends Thread{
    @Override
    public void run() {
        /*
        * Rowchecker initialises a hashset which it fills
        * with all the elements in each consecutive arraylist.
        * If the hashset doesn't contain 9 distinct elements then
        * the FLAG of the sudoku grid is set to false and the loop
        * is broken. If however 9 distinct elements are present then
        * the loop verifies that the elements belong to the range {0-9}
        * else it breaks the loop and sets FLAG as false.
        * */
        boolean flag = true;
        for(ArrayList<Integer> al : Sudoku.grid){
            HashSet<Integer> hs = new HashSet<Integer>(al);
            if(hs.size()!=9){
                flag = false;
                break;
            }
            for(int dig : al)
                if(dig>9 || dig<1){
                    flag = false;
                    break;
                }
        }
        System.out.println("Row check :"+flag);
        Sudoku.FLAG = flag;
    }
}

class colChecker extends Thread{
    @Override
    public void run(){
        /*
         * Columnchecker initialises a hashset which it fills
         * with the same indexed element from each consecutive arraylist.
         * If the hashset doesn't contain 9 distinct elements then
         * the FLAG of the sudoku grid is set to false and the loop
         * is broken. If however 9 distinct elements are present then
         * the loop verifies that the elements belong to the range {0-9}
         * else it breaks the loop and sets FLAG as false.
         * */
        boolean flag = true;
        for(int num = 0;num<9;num++){
            ArrayList<Integer> al = new ArrayList<Integer>();
            for(ArrayList<Integer> x : Sudoku.grid){
                ArrayList<Integer> temp = new ArrayList<Integer>(x);
                al.add(temp.get(num));
            }
            HashSet<Integer> hs = new HashSet<Integer>(al);
            if(hs.size()!=9){
                flag = false;
                break;
            }
            for(int dig : al)
                if(dig>9 || dig<1){
                    flag = false;
                    break;
                }
            if(!flag)
                break;
        }
        System.out.println("Column check :"+flag);
        Sudoku.FLAG = flag;
    }
}

class boxChecker extends Thread{
    private int r, c;
    static boolean flag = true;
    boxChecker(int r, int c){
        this.r = r;
        this.c = c;
    }
    @Override
    public void run(){
        /*
         * Gridchecker initialises a hashset which it fills
         * with all the elements in each box. It accesses these
         * elements with the help of sublists and indices. These
         * indices are data members of the boxChecker objects.
         * If the hashset doesn't contain 9 distinct elements then
         * the FLAG of the sudoku grid is set to false and the loop
         * is broken. If however 9 distinct elements are present then
         * the loop verifies that the elements belong to the range {0-9}
         * else it breaks the loop and sets FLAG as false.
         * */
        ArrayList<Integer> A,B,C;
        A = Sudoku.grid.get(r);
        B = Sudoku.grid.get(r+1);
        C = Sudoku.grid.get(r+2);
        Set<Integer> hs = new HashSet<>(A.subList(c,c+3));
        hs.addAll(B.subList(c,c+3));
        hs.addAll(C.subList(c,c+3));
        if(hs.size()!=9)
            flag = false;
        if(flag)
            for(int dig : hs)
                if(dig>9 || dig<1){
                    flag = false;
                    break;
                }
        System.out.println("Box check :"+flag);
        Sudoku.FLAG = flag;
    }
}
