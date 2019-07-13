
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
    // Number of Persons
    Person[] queue = new Person[1000];

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
        for (int i = 0; i < 1000; i++) {
            double arriveTime = i * 5;
            int floor = rand.nextInt(9) + 1;
            Person p = new Person(i, arriveTime, floor);
            queue[i] = p;
        }

        // Create elevator set
        Elevator[] elevators = new Elevator[3];
        int[] allowedFloors = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // Elevator[0] - allowed floors
        elevators[0] = new Elevator(10, NumberOfFloors, interFloorDistance, allowedFloors, 0);
        elevators[1] = new Elevator(10, NumberOfFloors, interFloorDistance, allowedFloors, 1);
        elevators[2] = new Elevator(10, NumberOfFloors, interFloorDistance, allowedFloors, 2);

        // ------------------ Test Code --------------------//
        // elevators[0].addPersons(queue);
        // elevators[0].showPersons();
        // double runTime = elevators[0].run();
        // System.out.println("runTime " + runTime);

        // for (Person p : queue) {
        // System.out.println("Floor: " + p.floor + ", waitTime: " + p.waitTime);
        // }
        // -------------------------------------------------//

        // Main event list
        ArrayList<Event> events = new ArrayList<Event>();

        // Add person queue to event list
        for (Person p : queue) {
            Event e = new Event(p.arriveTime, person_arrived, p);
            events.add(e);
            // sortedInsertEvent(events, e);
        }

        // -------------------- Test Code -------------------/
        // for (Event e : events) {
        // System.out.println("Person: " +e.p.id +" floor: " + e.p.floor + " arriveTime:
        // " + e.p.arriveTime);
        // }
        // ----------------------- End -----------------------/

        // Waiting persons list
        LinkedList<Person> waitingList = new LinkedList<Person>();

        // ListIterator<Event> eventItr = events.listIterator();
        double simulationTime = 0.0;
        // Running main simulation
        // while (eventItr.hasNext()) {

        for (int i = 0; i < events.size(); i++) {
            // Event e = eventItr.next();
            Event e = events.get(i);
            simulationTime = e.time;
            this.spendTime = e.time;

            if (e.type == person_arrived) {
                sortedInsertPerson(waitingList, e.getPerson());
            } else if (e.type == elevator_arrived) {
                System.out.println("Elevator " + e.e.id + " trip completed");
                e.e.isTripCompleted = true;
            } else if (e.type == elevator_leave) {
                System.out.println("Elevator " + e.e.id + " trip started");
                // e.e.isTripCompleted = true;
            }

            for (Elevator elevator : elevators) {
                if (elevator.isTripCompleted) { // Check is Elevator available4

                    if (waitingList.size() >= elevator.minimumCapacity) {

                        // List to add persons to assign to the elevator
                        ArrayList<Person> personElevatorSet = new ArrayList<Person>(elevator.personCapacity);
                        for (Person p : waitingList) { // Iterating through person waiting list
                            for (int floor : elevator.allowedFloors) {
                                if (p.floor == floor) { // Check the elevator allow to go to person's destination
                                                        // floor
                                    personElevatorSet.add(p);
                                }
                                if (personElevatorSet.size() >= elevator.personCapacity)
                                    break;
                            }
                        }
                        if (personElevatorSet.size() >= elevator.minimumCapacity) {
                            Person[] list = new Person[personElevatorSet.size()];
                            personElevatorSet.toArray(list);
                            elevator.addPersons(list); // Add selected Persons to elevator

                            for (Person person : list) {
                                person.waitTime = simulationTime - person.arriveTime;
                            }

                            removeFromWaitingList(waitingList, list); // remove persons set to elevator from
                                                                      // waitingList

                            Event elevatorStart = new Event(this.spendTime, elevator_leave, elevator);
                            elevator.isTripCompleted = false;
                            double elevatorTravelTime = elevator.run();
                            double elevatorTravelEndTime = this.spendTime + elevatorTravelTime; // Calculate when
                                                                                                // elevator
                                                                                                // comming down
                            Event elevatorEnd = new Event(elevatorTravelEndTime, elevator_arrived, elevator);

                            // Adding elevatorStart and elevatorEnd Events to Main Event List
                            sortedInsertEvent(events, elevatorStart);
                            sortedInsertEvent(events, elevatorEnd);

                            System.out.println("runTime " + elevatorTravelEndTime + "\n");
                        }
                    }
                }
            }

            // sort after adding
            // System.out.println("simulationTime: " + simulationTime);
        }

        // Remaining waiting list
        System.out.println("Remaining waitingList\n");
        Iterator<Person> itr = waitingList.iterator();
        while (itr.hasNext()) {
            Person e = itr.next();
            System.out.println(e.arriveTime);
        }

        double sum = 0.0;
        int count = 0;
        for (Person person : queue) {
            if (person.isFinish) {
                sum += person.waitTime;
                count++;
            }
            System.out.println("Person id: " + person.id + " waitTime: " + person.waitTime + " isFinish: "
                    + person.isFinish + " arriveTime: " + person.arriveTime + " journeyTime: " + person.journeyTime);
        }

        System.out.println("\naverage waitTime: " + sum / count);

        // Event list
        System.out.println("\nEvent List");
        Iterator<Event> eitr = events.iterator();
        while (eitr.hasNext()) {
            Event e = eitr.next();
            // static final int person_arrived = 1;
            // static final int elevator_arrived = 2;
            // static final int elevator_leave = 3;
            String eType = "NOT type";
            if (e.type == 1)
                eType = "pA";
            else if (e.type == 2)
                eType = "eA";
            else if (e.type == 3)
                eType = "eL";

            String per = "*";
            String elevator = "*";
            if (e.p != null) {
                per = String.valueOf(e.p.id);
            }
            if (e.e != null) {
                elevator = String.valueOf(e.e.id);
            }

            if (e.e != null)
                System.out.println(e.e.id + " " + eType + " time: " + e.time);// + " per: " + per + " elevator: " +
                                                                              // elevator);
        }
    }

    private static void removeFromWaitingList(LinkedList<Person> waitingList, Person[] elevatorList) {
        for (Iterator<Person> iter = waitingList.iterator(); iter.hasNext();) {
            Person waitingListPerson = iter.next();
            for (Person elevatorListPerson : elevatorList) {
                if (elevatorListPerson.id == waitingListPerson.id) {
                    iter.remove();
                    break;
                }
            }
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

    private static void sortedInsertEvent(ArrayList<Event> list, Event e) {

        if (list.size() == 0) {
            list.add(e);
        } else if (list.get(0).time > e.time) {
            list.add(0, e);
        } else if (list.get(list.size() - 1).time <= e.time) {
            list.add(list.size(), e);
        } else {
            int i = 0;
            while (list.get(i).time <= e.time) {
                i++;

            }
            list.add(i, e);
        }

    }
}