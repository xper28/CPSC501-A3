import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    public Scanner sc = new Scanner(System.in);
    ArrayList<Object> objects = new ArrayList<>();

    public void menu(){

        //Start the main menu
        System.out.println("Welcome to object serialiser! Choose a number option from below");
        int input = 1;
        while(input !=0) {
            System.out.print("1)Simple object w/primitive instance variables only\n" +
                    "2)Object that references other objects\n" +
                    "3)Object with an array of primitives\n" +
                    "4)Object with an array of object references\n" +
                    "5)Object with an instance of a Java collection\n" +
                    "6)Objects with circular references\n"+
                    "0)Exit\n");
            input = sc.nextInt();

            if(input == 1){
                createCoord();
            }else if(input == 2){
                createPlot();
            }else if(input == 3){
                createNums();
            }else if(input == 4){
                createGraph();
            }else if(input == 5){
                createCgraph();
            }else if(input == 6){
                createCircular();
            }
        }
    }

    private void createCircular() {
        Child c1 = new Child();
        System.out.println("Set child age: ");
        c1.setAge(sc.nextInt());
        Parent p = new Parent(c1);
        c1.setParent(p);
        objects.add(p);
    }

    private void createCgraph() {
        Cgraph cg = new Cgraph();
        for(int i=0; i<cg.cgraph.size(); i++){
            System.out.println("Set field values for plot " + i);
            System.out.println("Set x coordinate: ");
            cg.cgraph.get(i).point.setRow(sc.nextInt());
            System.out.println("Set y coordinate: ");
            cg.cgraph.get(i).point.setColumn(sc.nextInt());
        }
        objects.add(cg);
    }

    private void createGraph() {
        Graph g = new Graph();
        System.out.println("Set x coordinate: ");
        g.a.setRow(sc.nextInt());
        System.out.println("Set y coordinate: ");
        g.a.setColumn(sc.nextInt());
        objects.add(g);
    }

    private void createNums() {
        Numbers n = new Numbers();
        for(int i=0; i<n.nums.length; i++){
            System.out.println("Enter number at index " + i);
            n.nums[i] = sc.nextInt();
        }
        objects.add(n);
    }

    private void createPlot() {
        Coordinate c = new Coordinate();
        Plot p = new Plot(c);
        System.out.println("Set x coordinate: ");
        p.point.setRow(sc.nextInt());
        System.out.println("Set y coordinate: ");
        p.point.setColumn(sc.nextInt());
        objects.add(p);
    }

    private void createCoord() {
        Coordinate c = new Coordinate();
        System.out.println("Set x coordinate: ");
        c.setRow(sc.nextInt());
        System.out.println("Set y coordinate: ");
        c.setColumn(sc.nextInt());
        objects.add(c);
    }

}
