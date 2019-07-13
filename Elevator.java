import java.util.Arrays;
// import java.io.*; 
import java.util.*;

public class Elevator {
    int id;
    int currentFloor;
    int personCapacity;
    int minimumCapacity;
    Person[] persons;
    boolean isTripCompleted;
    double interFloorTravelTime;
    int[] floorTravelOrder;

    double remainTime;
    double elapsedTime;

    // Specifing allowed floors
    int[] allowedFloors;

    // Accelerate, Deaccelerate, const speed variables
    double accelerate = 1;
    double deaccelerate = 1;
    double constSpeed = 2;

    // floor details
    double[] interFloorDistance;

    Elevator(int cap, int NumberOfFloors, double[] interFloorDistance, int[] allowedFloors, int id) {
        this.currentFloor = 0;
        this.personCapacity = cap;
        this.isTripCompleted = true;
        this.elapsedTime = 0.0;
        this.interFloorTravelTime = 10.0;
        this.interFloorDistance = interFloorDistance;
        this.minimumCapacity = 8;
        this.allowedFloors = allowedFloors;
        this.id = id;
    }

    public void setID(int id){
        this.id = id;
    }

    public void addPersons(Person[] persons) {
        this.persons = persons;
        // System.out.println("Person[0] " + persons[0].floor);
    }

    public void showPersons() {
        System.out.print(persons[0]);
    }

    public double run() {
        elapsedTime = 0.0;
        this.floorTravelOrder = new int[this.personCapacity];
        for(int i=0; i < floorTravelOrder.length ; i++){
            floorTravelOrder[i]= 0;
        }

        // Get floors need to travel
        int i = 0;

        // System.out.println("floorTravelOrder :" + floorTravelOrder.length);
        System.out.println("In elevator :" + this.id);

        for (Person p : persons) {
            // System.out.println("In Elevator "+ this.id + " - Person: " + p.id + " floor: " + p.floor + " arriveTime: " + p.arriveTime);
            floorTravelOrder[i++] = p.floor;
        }

        // Sort array to bottom to upper floor
        Arrays.sort(floorTravelOrder);
        int l = removeDuplicateElements(floorTravelOrder, floorTravelOrder.length);
        // removeDuplicates(floorTravelOrder);

        int[] newArray = new int[l];
        for (int j = 0; j < l; j++) {
            newArray[j] = floorTravelOrder[j];
        }

        floorTravelOrder = newArray;

        // System.out.println("Sorted floor list");
        // for (int f : floorTravelOrder) {
        //     System.out.println("floor " + f);
        // }

        // System.out.println("NO of Floors: " + floorTravelOrder.length);
        double tempTime = 0.0;
        for (int j = 0; j < floorTravelOrder.length - 1; j++) {
            // System.out.println("j: " + j + " floor: " + floorTravelOrder[j]);
            if (j == 0) {
                // First stop floor travel time
                // System.out.println("inside travel floor j==0");
                tempTime = calculateTravelTime(0, floorTravelOrder[j], accelerate, deaccelerate, constSpeed);
                elapsedTime += tempTime;

            }
            // Calculate personal journeyTime
            calculatePersonWaitTime(persons, floorTravelOrder[j], elapsedTime);

            // calculate number of floor to travel without stop
            // int nextFloor = floorTravelOrder[j];
            // System.out.println("current floor: " + j);
            elapsedTime += calculateTravelTime(floorTravelOrder[j], floorTravelOrder[j + 1], accelerate, deaccelerate,
                    constSpeed);

            if (j == floorTravelOrder.length - 2) {
                // Calculate personal journeyTime
                calculatePersonWaitTime(persons, floorTravelOrder[j + 1], elapsedTime);

                // go to base floor
                // System.out.println("inside final floor");
                elapsedTime += calculateTravelTime(0, floorTravelOrder[j + 1], accelerate, deaccelerate, constSpeed);
            }
        }

        // Set Persons Finish the Journey
        for (Person p : persons) {
            p.isFinish = true;
        }

        return elapsedTime;
    }

    double calculateTravelTime(int currentFloor, int nextFloor, double acc, double deacc, double speed) {

        double distance = 0.0;
        double time = 0.0;
        if (currentFloor != nextFloor) {
            // System.out.println("currentFloor: " + currentFloor + ", nextFloor: " + nextFloor);
            for (int k = currentFloor; k < nextFloor; k++) {
                distance += interFloorDistance[k];
            }

            double accTime = speed / acc;
            double deaccTime = speed / deacc;
            double accDistance = 0.5 * acc * accTime * accTime;
            double deaccDistance = 0.5 * deacc * deaccTime * deaccTime;
            double constSpeedTime = (distance - (accDistance + deaccDistance)) / speed;
            time = accTime + constSpeedTime + deaccTime;
        }
        // System.out.println("distance: " + distance + ",  time: " + time);
        // System.out.println();
        return time;
    }

    // Function to add wait time for persons
    void calculatePersonWaitTime(Person[] persons, int currentFloor, double journeyTime) {
        for (Person p : persons) {
            if (p.floor == currentFloor)
                p.journeyTime += journeyTime;
        }
    }

    // To remove duplicate element in an array
    public static int removeDuplicateElements(int arr[], int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int[] temp = new int[n];
        int j = 0;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] != arr[i + 1]) {
                temp[j++] = arr[i];
            }
        }
        temp[j++] = arr[n - 1];
        // Changing original array
        for (int i = 0; i < j; i++) {
            arr[i] = temp[i];
        }
        return j;
    }

    public static int[] removeDuplicates(int[] arr) {
        HashSet<Integer> set = new HashSet<>();
        final int len = arr.length;
        // changed end to len
        for (int i = 0; i < len; i++) {
            set.add(arr[i]);
        }

        int[] whitelist = new int[set.size()];
        int i = 0;
        for (Iterator<Integer> it = set.iterator(); it.hasNext();) {
            whitelist[i++] = it.next();
        }
        return whitelist;
    }
}