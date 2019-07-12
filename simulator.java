
//import simulator.*;
//time will be calculated 
import java.util.*;
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
    final int NumberOfFloors = 10;

    // Defining the distance between 2 floors
    final double[] interFloorDistance = { 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0 };
    // eg - interFloorDistance[i] = Distance from i-1 floor to i floor

    // Event type specification
    static final int person_arrived = 1;
    static final int elevator_arrived = 2;
    static final int elevator_leave = 3;

    double spendTime;

    Building() {
        spendTime = 0.0;
    }

    public void run1() {
        // Initializing queue
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.arriveTime = i * 10;
            p.floor = rand.nextInt(9) + 1;
            queue[i] = p;
        }

        // Create elevator set
        Elevator[] elevators = new Elevator[1];
        elevators[0] = new Elevator(10, NumberOfFloors, interFloorDistance);

        // ------------------ Test Code --------------------//
        // elevators[0].addPersons(queue);
        // elevators[0].showPersons();
        // double runTime = elevators[0].run();
        // System.out.println("runTime " + runTime);

        // for (Person p : queue) {
        // System.out.println("Floor: " + p.floor + ", waitTime: " + p.waitTime);
        // }
        // -------------------------------------------------//

        // Add person queue to event list
        LinkedList<Event> events = new LinkedList<Event>();
        for (Person p : queue) {
            Event e = new Event(p.arriveTime, person_arrived, p);
            sortedInsertEvent(events, e);
        }

        // Waiting persons list
        LinkedList<Person> waitingList = new LinkedList<Person>();

        // Running main simulation
        Iterator<Event> eventItr = events.iterator();

        while (eventItr.hasNext()) {
            Event e = eventItr.next();
            if (e.type == person_arrived)
                sortedInsertPerson(waitingList, e.getPerson());
            
            for (Elevator elevator : elevators) {
                if(elevator.isTripCompleted){
                    if(elevator.minimumCapacity <= waitingList.size()){
                        
                    }
                }
            }

            // sort after adding
            System.out.println("etime: " + e.time);
        }

        Iterator<Person> itr = waitingList.iterator();
        while (itr.hasNext()) {
            Person e = itr.next();
            System.out.println(e.arriveTime);
        }

    }

    private static void sortedInsertPerson(LinkedList<Person> list, Person p) {

        if (list.size() == 0) {
            list.add(p);
        } else if (list.get(0).arriveTime > p.arriveTime) {
            list.add(0, p);
        } else if (list.get(list.size() - 1).arriveTime < p.arriveTime) {
            list.add(list.size(), p);
        } else {
            int i = 0;
            while (list.get(i).arriveTime < p.arriveTime) {
                i++;
            }
            list.add(i, p);
        }

    }

    private static void sortedInsertEvent(LinkedList<Event> list, Event e) {

        if (list.size() == 0) {
            list.add(e);
        } else if (list.get(0).time > e.time) {
            list.add(0, e);
        } else if (list.get(list.size() - 1).time < e.time) {
            list.add(list.size(), e);
        } else {
            int i = 0;
            while (list.get(i).time < e.time) {
                i++;
            }
            list.add(i, e);
        }

    }
}