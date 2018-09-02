package OS.prac6;
import java.util.*;
import java.io.*;
/*
 * Created by Rebecca D'souza on 27/3/18
 */
public class Main{
    private static HashMap<Integer,Integer> hm = new HashMap<>();
    private static Integer getFrame(int pgNo){
        for (Map.Entry pair : hm.entrySet())
            if ((Integer) pair.getValue() == pgNo)
                return (Integer) pair.getKey();
        return -1;
    }
    public static void main(String args[]){
        int frames = 0;
        try{
            FileReader fr = new FileReader("OS/prac6/logicalAddresses");
            BufferedReader br = new BufferedReader(fr);
            String logicAddr;
            while((logicAddr = br.readLine())!=null) {
                /*
                 * Calculation of offset and page number which constitute the last 16 bits
                 * offset is the rightmost 8 bits
                 * */
                int pgNo = Integer.parseInt(logicAddr)/256;
                int offset = Integer.parseInt(logicAddr)%256;
                /*
                 * Mapping of page number to appropriate frame
                 * */
                if(!hm.containsValue(pgNo))
                    hm.put(frames++,pgNo);
                System.out.print("\nPhysical Address for Logical Address "+Integer.parseInt(logicAddr)+" is ");
                System.out.print(getFrame(pgNo)*256 + offset);
            }
        }
        catch(Exception ignored){}
        System.out.println("\n\nPAGE TABLE");
        System.out.println("Frame"+"\t"+"Page Number");
        for(int i = 0;i<frames;i++) System.out.println(i+"\t\t"+hm.get(i));
    }
}