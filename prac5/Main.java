package OS.prac5;
import java.util.*;
/*
 * Created by Rebecca D'souza on 27/02/18
 */
public class Main{
    private static int noProcesses;
    private static int noResources;
    private static int available[];
    private static int allocation[][];
    private static int need[][];

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes and number of types of resources");
        noProcesses = sc.nextInt();
        noResources = sc.nextInt();

        available = new int[noResources];
        allocation = new int[noProcesses][noResources];
        int[][] maximum = new int[noProcesses][noResources];
        need = new int[noProcesses][noResources];
        int[] request = new int[noResources];
        /*
        * Reading in number of resources and processes
        * available maximum and work matrix from the user
        * */
        System.out.println("Enter the allocation of "+noProcesses+" processes");
        for(int i = 0;i<noProcesses;i++)
            for(int j = 0;j<noResources;j++)
                allocation[i][j] = sc.nextInt();

        System.out.println("Enter the maximum of "+noProcesses+" processes");
        for(int i = 0;i<noProcesses;i++)
            for(int j = 0;j<noResources;j++)
                maximum[i][j] = sc.nextInt();
        /*
        * Calculation of need matrix
        * */
        for(int i = 0;i<noProcesses;i++)
            for(int j = 0;j<noResources;j++)
                need[i][j] = maximum[i][j]-allocation[i][j];

        System.out.println("Enter the available/work vector");
        for(int i = 0;i<noResources;i++)
            available[i] = sc.nextInt();

        if(safetyCheck()) {
            /*
            * Taking in resource requests from user
            * */
            System.out.println("Enter number of resource requests");
            int number = sc.nextInt();
            while (number--!=0) {
                System.out.println("Enter the process id and resource request.");
                int id = sc.nextInt();
                for (int i = 0; i < noResources; i++)
                    request[i] = sc.nextInt();
                if (validateRequest(id, request)) {
                    System.out.println("\nRequest is valid. Safety of resource allocation is being determined.");
                    resourceRequest(id, request);
                    restoreState(id, request);
                }
            }
        }
        else
            System.out.println("Safe sequence not possible");
        sc.close();
    }
    private static boolean validateRequest(int id, int request[]){
        /*
        *  Checking whether the new request for a process is valid or not.
        *  If valid, then print valid request, else not valid request.
        * */
        for(int i = 0;i<noResources;i++){
            if(need[id][i]<request[i])
                return false;
            if(available[i]<request[i])
                return false;
        }
        return true;
    }
    private static void resourceRequest(int id, int request[]){
        for(int i = 0;i<noResources;i++){
            allocation[id][i] += request[i];
            need[id][i] -= request[i];
            available[i] -= request[i];
        }
        System.out.println("\n*******************************************************************");
        System.out.print("\nNew allocation of process "+id+" is : ");
        printVector(allocation[id]);
        System.out.print("\nNew need of process "+id+" is : ");
        printVector(need[id]);
        System.out.print("\nNew available of all processes is : ");
        printVector(available);
        System.out.println("\n\n*******************************************************************");
        if(safetyCheck())
            System.out.println("\nSafe sequence is possible.");
        else System.out.println("\n\nSafe sequence is not possible\n\n");
    }
    private static void restoreState(int id, int request[]){
        for(int i = 0;i<noResources;i++){
            allocation[id][i] -= request[i];
            need[id][i] += request[i];
            available[i] += request[i];
        }
    }
    /*
    * Safety algorithm to check prevalence of safe sequence or not
    * */
    private static boolean safetyCheck(){
        ArrayList<Integer> order = new ArrayList<>();
        boolean executed [] = new boolean[noProcesses];
        boolean safe = true;
        int tempAvailable[] = new int[noResources];
        System.arraycopy(available,0,tempAvailable,0,noResources);
        for(int i = 0;i<noProcesses;i++)
            executed[i]=false;
        while(order.size()<noProcesses && safe){
            safe = false;
            for(int i = 0;i<noProcesses;i++){
                if(!executed[i]){
                    boolean flag = true;
                    for(int j = 0;j<noResources;j++){
                        if(need[i][j]>tempAvailable[j]){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        executed[i]=true;
                        order.add(i);
                        safe = true;
                        System.out.print("\nSINCE NEED VECTOR IS : ");
                        printVector(need[i]);
                        System.out.println("\nPROCESS "+i+" HAS BEEN EXECUTED.");
                        System.out.print("\nNEW AVAILABLE VECTOR IS : ");
                        for(int k = 0;k<noResources;k++){
                            tempAvailable[k] += allocation[i][k];
                            System.out.print(tempAvailable[k]+" ");
                        }
                    }
                }
            }
        }
        if(safe){
            System.out.print("\nORDER OF EXECUTION IS : ");
            for(int x : order)
                System.out.print("P"+x+" ---> ");
        }
        System.out.println();
        return safe;
    }
    private static void printVector(int arr[]){
        for(int i = 0;i<noResources;i++)
            System.out.print(arr[i]+" ");
    }
}
/*
Enter number of processes and number of types of resources
3 3
Enter the allocation of 3 processes
0 0 1
3 2 0
2 1 1
Enter the maximum of 3 processes
8 4 3
6 2 0
3 3 3
Enter the available/work vector
3 2 2

SINCE NEED VECTOR IS : 3 0 0
PROCESS 1 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 6 4 2
SINCE NEED VECTOR IS : 1 2 2
PROCESS 2 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 8 5 3
SINCE NEED VECTOR IS : 8 4 2
PROCESS 0 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 8 5 4
ORDER OF EXECUTION IS : P1 ---> P2 ---> P0 --->
Enter number of resource requests
2
Enter the process id and resource request.
0
0 0 2

Request is valid. Safety of resource allocation is being determined.

*******************************************************************

New allocation of process 0 is : 0 0 3
New need of process 0 is : 8 4 0
New available of all processes is : 3 2 0

*******************************************************************

SINCE NEED VECTOR IS : 3 0 0
PROCESS 1 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 6 4 0


Safe sequence is not possible


Enter the process id and resource request.
1
2 0 0

Request is valid. Safety of resource allocation is being determined.

*******************************************************************

New allocation of process 1 is : 5 2 0
New need of process 1 is : 1 0 0
New available of all processes is : 1 2 2

*******************************************************************

SINCE NEED VECTOR IS : 1 0 0
PROCESS 1 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 6 4 2
SINCE NEED VECTOR IS : 1 2 2
PROCESS 2 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 8 5 3
SINCE NEED VECTOR IS : 8 4 2
PROCESS 0 HAS BEEN EXECUTED.

NEW AVAILABLE VECTOR IS : 8 5 4
ORDER OF EXECUTION IS : P1 ---> P2 ---> P0 --->

Safe sequence is possible.

Process finished with exit code 0
*/