package OS.prac2;

import java.util.*;

/**
 * Created by Rebecca D'souza on 02/02/18
 */

class process{

    public int id;
    public int arrTime;
    public int priority;
    public int burstTime;
    public int waitingTime;
    public int turnaroundTime;

    public process(int id, int arrTime, int burstTime, int priority){
        this.id = id;
        this.arrTime = arrTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

}

class idComparator implements Comparator{

    public int compare(Object o1, Object o2){
        process p1 = (process)o1;
        process p2 = (process)o2;
        return Integer.compare(p1.id, p2.id);
    }

}

class arrTimeComparator implements Comparator{

    public int compare(Object o1, Object o2){
        process p1 = (process)o1;
        process p2 = (process)o2;
        return Integer.compare(p1.arrTime, p2.arrTime);
    }

}

public class Priority_18{
    /*
    * the queue of processes is passed to sortByPriority()
    * here all the processes which clash at arrival are sorted
    * by priority ie a more important OS.prac2.process is placed before a
    * less important OS.prac2.process.
    * */
    private static ArrayList<process> sortByPriority(ArrayList<process> al){
        process arr[] = al.toArray(new process[0]);
        for(int i = 0;i<al.size()-1;i++){
            for(int j = i;j<al.size()-1;j++) {
                if (arr[j].arrTime == arr[j + 1].arrTime && arr[j].priority >=arr[j+1].priority){
                    process temp;
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return new ArrayList<>(Arrays.asList(arr));
    }
    /*
     * the previous sort only sorted the queue of processes by priority and arrival
     * here sortByReady() sorts the processes by availability
     * for example consider two processes having less importance being scheduled,
     * and a OS.prac2.process which is more important has been scheduled after them since it arrives later
     * however if the first OS.prac2.process has a burst time which surpasses the arrival time of the third important OS.prac2.process;
     * by the time the first OS.prac2.process finishes, we have both, the less important second OS.prac2.process and the higher importance
     * third OS.prac2.process in the ready states. Ergo the third OS.prac2.process will be served first and the second after it.
     * */
    private static ArrayList<process> sortByReady(ArrayList<process> al) {
        process arr[] = al.toArray(new process[0]);
        int time = arr[0].burstTime + arr[0].arrTime;
        for (int i = 1; i < al.size(); i++) {
            for (int j = i; j<al.size() -1;j++){
                if (arr[j].priority > arr[j + 1].priority) {
                    if (arr[j + 1].arrTime <= time && arr[j].arrTime <= time) {
                        process temp;
                        temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }
                time += arr[j].burstTime;
            }
        }
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static void main(String args[]){
        int TIME;
        double avgWT =0;
        double avgTT =0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes");
        int n = sc.nextInt();
        System.out.println("Enter OS.prac2.process id, arrival time, burst time and priority");
        /*
        * creates a queue of the processes which we shall change into a ready queue
        * */
        ArrayList<process> al = new ArrayList<>();
        for(int i = 0;i<n;i++)
            al.add(new process(sc.nextInt(),sc.nextInt(),sc.nextInt(),sc.nextInt()));

        /*
        *sorts the processes by arrival time
        * */
        Collections.sort(al,new arrTimeComparator());
        /*
         *sorts the processes by priority at arrival time clash
         * */
        al = sortByPriority(al);
        /*
         *sorts the processes by availability and ready states
         * */
        al = sortByReady(al);
        System.out.println("\nGNATT CHART");
        for(int i = 0; i < (int)al.size()*5 + 1;i++)
            System.out.print("-");
        System.out.println();
        for (process p : al)
            System.out.printf("| P%d ",p.id);
        System.out.println("|");
        for(int i = 0; i < (int)al.size()*5 + 1;i++)
            System.out.print("-");
        /*
        * calculation of waiting and turnaround time
        * by iterating through the entire ready queue
        * which is the actual order in which the
        * processes shall be scheduled
        * */
        TIME = al.get(0).arrTime;
        for (process p : al) {
            if (TIME > p.arrTime)
                p.waitingTime = TIME - p.arrTime;
            TIME += p.burstTime;
            if (p.waitingTime == 0)
                p.turnaroundTime = p.burstTime;
            else
                p.turnaroundTime = TIME - p.arrTime;
            avgWT += p.waitingTime;
            avgTT += p.turnaroundTime;
        }
        /*
         *sorts the processes by id for the sake of display
         * */
        Collections.sort(al,new idComparator());
        System.out.println('\n');
        System.out.println("Process Arrival Burst Priority Waiting  Turnaround");
        System.out.println(" ID      Time   Time            Time       Time");
        for(process x : al)
            System.out.printf(" %04d \t %04d \t%04d \t%04d \t%04d \t   %04d\n",x.id,x.arrTime,x.burstTime,x.priority,x.waitingTime,x.turnaroundTime);
        System.out.printf("\nAverage waiting time is = %.3f\n",avgWT/n);
        System.out.printf("\nAverage turnaround time is = %.3f\n",avgTT/n);


    }
}
