
import java.util.*;
import java.io.*;

public class ExamSeatingSystem {
    static String[][][] seat = new String[2][2][3];
    static String[] name = new String[1000];
    static int[] room = new int[1000], row = new int[1000], col = new int[1000];
    static Queue<String> q = new LinkedList<>();
    static int id = 101;
    static Scanner sc = new Scanner(System.in);
    static int[][] graph = {{0,1},{1,0}};

    // Seat allocation
    static void allocate() {
        System.out.print("Enter name: ");
        String n = sc.nextLine();
        if(n.trim().isEmpty() || n.contains("|")) {
            System.out.println("Invalid name"); return;
        }
        int k = id - 101;
        name[k] = n;
        String sid = "E" + id++;
        room[k] = -1;
        for(int r=0;r<2;r++)
            for(int i=0;i<2;i++)
                for(int j=0;j<3;j++)
                    if(seat[r][i][j]==null) {
                        seat[r][i][j] = sid;
                        room[k]=r; row[k]=i; col[k]=j;
                        System.out.println("Allocated: "+sid+" | "+n+ " | Room "+(r+1)+" | Seat "+(char)('A'+r)+(i*3+j+1));
                        return;
                    }
        q.add(sid);
        System.out.println("No seats available. Added to waiting queue.");
    }

    // Display seats
    static void display() {
        for(int r=0; r<2; r++) {
            System.out.println("Room "+(r+1));
            for(int i=0;i<2;i++) {
                for(int j=0;j<3;j++)
                    System.out.print((seat[r][i][j]==null?"Empty":seat[r][i][j])+" ");
                System.out.println();
            }
        }
    }

    // Search student
    static void search() {
        System.out.print("Enter ID: ");
        String s=sc.nextLine().toUpperCase();
        try {
            int k=Integer.parseInt(s.substring(1))-101;
            if(k<0 || k>=1000 || name[k]==null) {
                System.out.println("Student "+s+" not found."); return;
            }
            System.out.println(s+" | "+name[k]);
            if(room[k]==-1) System.out.println("In waiting queue");
            else System.out.println("Room "+(room[k]+1)+" Seat "+
                (char)('A' + room[k])+( row[k] * 3 + col[k]+1));
        } catch(Exception e) { System.out.println("Student not found."); }
    }

    // Display graph
    static void showGraph() {
        for(int i=0; i<2; i++) System.out.println(Arrays.toString(graph[i]));
    }

    // Save and load data
    static void save() {
        try {
            PrintWriter p=new PrintWriter("exam_seating.txt");
            for(int k=0;k<id-101;k++)
                if(name[k]!=null)
                    p.println("E"+(k+101)+"|"+name[k]+"|"+room[k]+"|"+row[k]+"|"+col[k]);
           
        } catch(Exception e) { System.out.println("File error"); }
    }

    static void load() {
        try {
            Scanner f=new Scanner(new File("exam_seating.txt"));
            while(f.hasNextLine()) {
                String[] d=f.nextLine().split("\\|");
                int k=Integer.parseInt(d[0].substring(1))-101;
                if(k<0 || k>=1000 || name[k]!=null) continue;
                int r=Integer.parseInt(d[2]), i=Integer.parseInt(d[3]), j=Integer.parseInt(d[4]);
                if(r!=-1 && (r<0 || r>=2 || i<0 || i>=2 || j<0 || j>=3 || seat[r][i][j]!=null)) continue;
                name[k]=d[1]; room[k]=r; row[k]=i; col[k]=j;
                if(r==-1) q.add(d[0]); else seat[r][i][j]=d[0];
                id=Math.max(id,k+102);
            }
           
        } catch(Exception e) { System.out.println("Starting new system"); }
    }

    public static void main(String[] args) {
        load();
        int ch;
        do {
            System.out.println("\n1.Allocate  2.Display  3.Search");
            System.out.println("4.Waiting Queue  5.Graph  6.Exit");
            ch=sc.nextInt(); sc.nextLine();
            switch(ch) {
                case 1: allocate(); break;
                case 2: display(); break;
                case 3: search(); break;
                case 4: System.out.println(q); break;
                case 5: showGraph(); break;
                case 6: save(); System.out.println("Data saved"); break;
                default: System.out.println("Invalid choice");
            }
        } while(ch!=6);
    }
}
