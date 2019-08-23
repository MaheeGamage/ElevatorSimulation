
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
    final int NumberOfPersons = 300;
    final int NumberOfFloors = 10;
    final int MAX_ARRIVE_TIME = 900; // in seconds

    // Number of Persons
    Person[] queue = new Person[NumberOfPersons];

    // Defining the distance between 2 floors
    final double[] interFloorDistance = new double[NumberOfFloors]; // { 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0,
                                                                    // 18.0 };
    // List begin in distance to first floor(1) to base Floor(0)
    // eg - interFloorDistance[i] = Distance from i-1 floor to i floor

    // Defining percentage of people go to each floor
    final int[] FLOOR_WEIGHTS = { 55, 5, 5, 5, 5, 5, 5, 5, 5, 5 }; // Sum of all element should be 100

    // Create elevator set
    Elevator[] elevators;

    // Event type specification
    static final int person_arrived = 1;
    static final int elevator_arrived = 2;
    static final int elevator_leave = 3;

    //Elevator Waiting time for next person
    static final double WAIT_FOR_NEXT_PERSON = 5.0;

    double spendTime; // Total Simulation time

    Building() {
        spendTime = 0.0;

        int NumberOfElevators = 2;
        elevators = new Elevator[NumberOfElevators];
        // Elevator(Maximum_capacity, Number_of_Floors_in_the_building,
        // Inter_Floor_Distance, Aloowed_floors, ID)
        elevators[0] = new Elevator(26, NumberOfFloors, interFloorDistance, generateAllowedFloor(1, 10), 0);
        // elevators[1] = new Elevator(26, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1, 10), 1);
        // elevators[2] = new Elevator(26, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1, 10), 2);
        elevators[1] = new Elevator(26, NumberOfFloors, interFloorDistance, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
                1);
        // elevators[4] = new Elevator(26, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1, 10), 4);
        // elevators[5] = new Elevator(26, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1, 10), 5);
        // elevators[6] = new Elevator(26, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1, 10), 6);
        // elevators[7] = new Elevator(26, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1, 10), 7);

    }

    public void run1() {

        // Create Inter floor distance
        for (int f = 0; f < NumberOfFloors; f++) {
            interFloorDistance[f] = 4; // Distance between 2 floors
        }

        // Initializing queue with random floors
        // Random rand = new Random();
        // for (int i = 0; i < NumberOfPersons; i++) {
        //     double arriveTime = MAX_ARRIVE_TIME * rand.nextDouble();
        //     int floor = rand.nextInt(NumberOfFloors) + 1;
        //     Person p = new Person(i, arriveTime, floor); // Person(id, arrive_Time, Floor)
        //     queue[i] = p;
        // }

        // Initializing queue with weighted floors
        int[] peoplePerFloor = new int[FLOOR_WEIGHTS.length];
        int tempSum = 0;
        for (int i=0; i < FLOOR_WEIGHTS.length; i++) {
            if(i==FLOOR_WEIGHTS.length - 1){
                peoplePerFloor[i] = NumberOfPersons;
            }
            else{
                tempSum += (NumberOfPersons * FLOOR_WEIGHTS[i])/100;
                peoplePerFloor[i] = tempSum;
            }
        }

        Random rand = new Random();
        for (int i = 0; i < NumberOfPersons; i++) {

            //seting up Floor
            int floor = 0;
            for (int j=0; j < peoplePerFloor.length; j++) {
                if((i+1)<=peoplePerFloor[j]){
                    floor = j+1;
                    break;
                }
            }

            double arriveTime = MAX_ARRIVE_TIME * rand.nextDouble();
            Person p = new Person(i, arriveTime, floor); // Person(id, arrive_Time, Floor)
            queue[i] = p;
        }

        System.out.println("People list");
        for (Person person : queue) {
            System.out.println(person.id + " floor: " + person.floor + " arriveTime: " + person.arriveTime);
        }

        SuffleArray(queue);

        System.out.println("People list shuffled");
        for (Person person : queue) {
            System.out.println(person.id + " floor: " + person.floor + " arriveTime: " + person.arriveTime);
        }

        // // Create elevator set
        // Elevator[] elevators = new Elevator[2];
        // // int[] allowedFloors = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // Elevator[0]
        // - allowed floors

        // Elevator(Maximum_capacity, Number_of_Floors_in_the_building,
        // Inter_Floor_Distance, Aloowed_floors, ID)
        // elevators[0] = new Elevator(10, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1,10), 0);
        // elevators[1] = new Elevator(10, NumberOfFloors, interFloorDistance,
        // generateAllowedFloor(1,10), 1);

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
            // events.add(e);
            sortedInsertEvent(events, e);
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

            //Check next person arriveTime
            boolean isNextPerson = false;
            for(int j=i+1; j<events.size(); j++){
                Event nextEvent = events.get(j);
                if(nextEvent.p!=null){
                    double timeToNext = nextEvent.time - e.time;
                    if(timeToNext <= WAIT_FOR_NEXT_PERSON){
                        isNextPerson =true;
                        break;
                    }
                    else{
                        isNextPerson =false;
                        break;
                    }
                }
            }


            for (Elevator elevator : elevators) {
                if (elevator.isTripCompleted) { // Check is Elevator available4

                    // if (waitingList.size() >= elevator.minimumCapacity) {
                        boolean elevatorFull = false;
                        // List to add persons to assign to the elevator
                        ArrayList<Person> personElevatorSet = new ArrayList<Person>(elevator.personCapacity);
                        for (Person p : waitingList) { // Iterating through person waiting list
                            for (int floor : elevator.allowedFloors) {
                                if (p.floor == floor) { // Check the elevator allow to go to person's destination
                                                        // floor
                                    personElevatorSet.add(p);
                                }
                                if (personElevatorSet.size() >= elevator.personCapacity) {
                                    elevatorFull = true;
                                    break;
                                }
                            }
                            if (elevatorFull)
                                break;
                        }

                        if ((elevatorFull || !isNextPerson) && (waitingList.size()>0)) {
                            // System.out.println("waiting list size: " + waitingList.size()+ " elevatorFull: " + elevatorFull + " isNextPerson: " + isNextPerson);
                            Person[] list = new Person[personElevatorSet.size()];
                            personElevatorSet.toArray(list);
                            elevator.addPersons(list); // Add selected Persons to elevator

                            for (Person person : list) {
                                person.waitTime = this.spendTime - person.arriveTime;
                            }

                            removeFromWaitingList(waitingList, list); // remove persons_elevator_set from
                                                                      // waitingList

                            Event elevatorStart = new Event(this.spendTime, elevator_leave, elevator, list.length);
                            elevator.isTripCompleted = false;
                            double elevatorTravelTime = elevator.run(); // Get Elevator Total journey Time
                            double elevatorTravelEndTime = this.spendTime + elevatorTravelTime; // Calculate when
                                                                                                // elevator
                                                                                                // comming down
                            Event elevatorEnd = new Event(elevatorTravelEndTime, elevator_arrived, elevator);

                            // Adding elevatorStart and elevatorEnd Events to Main Event List
                            sortedInsertEvent(events, elevatorStart);
                            sortedInsertEvent(events, elevatorEnd);

                            System.out.println("runTime " + elevatorTravelTime + "\n");
                        }
                    // }
                }
            }

            // sort after adding
            // System.out.println("simulationTime: " + simulationTime);
        }

        // Remaining waiting list
        // System.out.println("\nRemaining waitingList");
        // Iterator<Person> itr = waitingList.iterator();
        // while (itr.hasNext()) {
        //     Person e = itr.next();
        //     System.out.println("Arrive Time: " + e.arriveTime);
        // }

        System.out.println("\nPerson Queue");
        LinkedList<Person> sortedPersonList = new LinkedList<Person>();
        for (Person person : queue) {
            sortedInsertPerson(sortedPersonList, person);
        }
        
        double waitTimeSum = 0.0;
        double journeyTimeSum = 0.0;
        int count = 0;
        for (Person person : sortedPersonList) {
            if (person.isFinish) {
                waitTimeSum += person.waitTime;
                journeyTimeSum += person.journeyTime;
                count++;
            }
            System.out.println("Person id: " + person.id + " Floor: " + person.floor + " waitTime: " 
                    + person.isFinish + " arriveTime: " + person.arriveTime + " journeyTime: " + person.journeyTime);
                //+ person.waitTime + " isFinish: "
        }
        double averageWaitTime = waitTimeSum / count;
        double averageJourneyTime = journeyTimeSum / count;
        double averageTotalTime = (waitTimeSum + journeyTimeSum) / count;

        // System.out.println("\nEvent List");
        // Iterator<Event> eitr1 = events.iterator();
        // while (eitr1.hasNext()) {
        //     Event e1 = eitr1.next();
        //     System.out.println(e1.type + " time: " + e1.time + " per: " + e1.p + " elevator:" + e1.e);
        // }

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

            // System.out.println(eType + " time: " + e.time + " per: " + per + " elevator:
            // " + elevator);

            //Show people events in event list
            // if (e.p != null)
            //     System.out.println("P " + e.p.id + " " + "floor: " + e.p.floor + " ArriveTime: " + e.p.arriveTime);

            //Show elevator events in event list
            if (e.e != null){
                String elevatorCarrySize = " ";
                if(e.numberOfPeople != 0)
                    elevatorCarrySize =  " #ofPersons: " + e.numberOfPeople;
                System.out.println("E " + e.e.id + " " + eType + " time: " + e.time + elevatorCarrySize );// + " per: " + per + " elevator: " +
                                                                              // elevator);
            }
        }

        System.out.println("\naverage waitTime: " + averageWaitTime);
        System.out.println("average journeyTime: " + averageJourneyTime);
        System.out.println("average totalTime: " + averageTotalTime);
    }

    private static int[] generateAllowedFloor(int minFloor, int maxFloor) {
        int[] arr = new int[maxFloor - minFloor + 1];
        for (int i = 0; i <= maxFloor - minFloor; i++)
            arr[i] = i + minFloor;

        return arr;
    }

    // Generate inter floor distance in same distance pattern
    private static double[] generateInterFloorDistance(int floors, int distance) {
        double[] list = new double[floors - 1];
        for (int i = 0; i < list.length; i++)
            list[i] = i * 10;

        return list;
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

    public static Person[] SuffleArray(Person[] array) {
        Random rgen = new Random(); // Random number generator

        for (int i = 0; i < array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            Person temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }

        return array;
    }
}