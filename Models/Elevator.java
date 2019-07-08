import java.util.Arrays;

public class Elevator {
    int currentFloor;
    int personCapacity;
    int minimumCapacity;
    Person[] persons;
    boolean isTripCompleted;
    double interFloorTravelTime;
    int[] floorTravelOrder;

    double remainTime;
    double elapsedTime;

    Elevator(int cap, int NumberOfFloors){
        currentFloor = 0;
        personCapacity = cap;
        persons = new Person[cap];
        isTripCompleted = true;
        elapsedTime = 0.0;
        interFloorTravelTime = 10.0;
        floorTravelOrder = new int[NumberOfFloors];
    }

    double run(){
        isTripCompleted = false;
            
        for (Person p : persons) {
            floorTravelOrder.add(element);
        }
        
        Arrays.sort(floorTravelOrder);

        
        return 0.0;
    }
}