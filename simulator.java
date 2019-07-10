//import simulator.*;
//time will be calculated 
import java.util.Random;

public class simulator {

    public static void main(String[] args) {

        System.out.println("Hello World"); // prints Hello World
        Building building = new Building();
        building.run1();

        // return false;
    }
}

class Building {
    Person[] queue = new Person[10];
    Elevator[] elevators = new Elevator[4];
    final int NumberOfFloors = 10;

    final double[] interFloorDistance = {10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0};
    //eg - interFloorDistance[i] = Distance from i-1 floor to i floor

    double spendTime;

    Building(){
        spendTime = 0.0;
    }

    public void run1() {
        //Initializing queue
        Random rand = new Random(); 
        for(int i=0; i<10; i++){
            Person p = new Person();
            p.arriveTime = i*10;
            p.floor = rand.nextInt(9) + 1;
            queue[i] = p;
        }

        
        for (Person p : queue) {
            System.out.println(p.floor);
        }

        //Create elevator set
        int q=0;
        for(int i=0; i<1; i++){
            Elevator e = new Elevator(10, NumberOfFloors, interFloorDistance);
            elevators[q++] = e;
        }

        elevators[0].addPersons(queue);
        elevators[0].showPersons();
        double runTime = elevators[0].run();
        System.out.println("runTime " + runTime);

        for (Person p : queue) {
            System.out.println("Floor: " + p.floor + ",  waitTime: " + p.waitTime);
        }

        //Running main simulation
        boolean isCompelte = false;
        // while(false){
            
        // }

    }
}