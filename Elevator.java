import java.util.Arrays;
import java.io.*; 
import java.util.*;

public class Elevator {
    int currentFloor;
    int personCapacity;
    int minimumCapacity;
    Person[] persons;
    Person[] persons2;
    boolean isTripCompleted;
    double interFloorTravelTime;
    int[] floorTravelOrder;

    double remainTime;
    double elapsedTime;

    //Accelerate, Deaccelerate, const speed variables
    double accelerate = 1;
    double deaccelerate = 1;
    double constSpeed = 2;

    //floor details
    double[] interFloorDistance;
    double[] interFloorDistance2;

    Elevator(int cap, int NumberOfFloors, double[] interFloorDistance){
        currentFloor = 0;
        personCapacity = cap;
        isTripCompleted = true;
        elapsedTime = 0.0;
        interFloorTravelTime = 10.0;
        floorTravelOrder = new int[NumberOfFloors];
        interFloorDistance = interFloorDistance;
        interFloorDistance2 = interFloorDistance;
    }

    public void addPersons(Person[] persons){
        persons = persons;
        persons2 = persons;
        System.out.println("Person[0] " + persons[0].floor);
    }

    public void showPersons(){
        persons = persons2;
        System.out.print(persons[0]);
    }

    public double run(){
        persons = persons2;
        isTripCompleted = false;
        elapsedTime = 0.0;

        //Get floors need to travel
        int i=0;
        System.out.println("Person[0] " + persons[0].floor);
        for (Person p : persons) {
            floorTravelOrder[i++] = p.floor;
        }
        
        //Sort array to bottom to upper floor
        Arrays.sort(floorTravelOrder);
        int l = removeDuplicateElements(floorTravelOrder, floorTravelOrder.length);
        //removeDuplicates(floorTravelOrder);  

        int[] newArray = new int[l]; 
        for(int j=0; j < l; j++){
            newArray[j] = floorTravelOrder[j];
        }

        floorTravelOrder = newArray;

        System.out.println("Shoe florr");
        for (int f : floorTravelOrder) {
            System.out.println("floor " + f);
        }

        System.out.println("NO of Floors: " + floorTravelOrder.length);
        for(int j=0; j < floorTravelOrder.length - 1; j++){
            System.out.println("j: " + j + " floor: " + floorTravelOrder[j]);
            if(j == 0){
                //First stop floor travel time
                System.out.println("inside travel floor j==0");
                elapsedTime += calculateTravelTime( 0, floorTravelOrder[j], accelerate, deaccelerate, constSpeed);
            }

            //calculate number of floor to travel without stop
            int nextFloor = floorTravelOrder[j];
            System.out.println("current floor: " + j);
            elapsedTime += calculateTravelTime( floorTravelOrder[j], floorTravelOrder[j+1], accelerate, deaccelerate, constSpeed);
            
            if(j == floorTravelOrder.length - 2){
                //go to base floor
                System.out.println("inside final floor");
                elapsedTime += calculateTravelTime( 0, floorTravelOrder[j], accelerate, deaccelerate, constSpeed);
            }
        }

        return elapsedTime;
    }

    double calculateTravelTime(int currentFloor, int nextFloor, double acc, double deacc, double speed){
        
        double distance = 0.0;
        System.out.println("currentFloor: "+ currentFloor + ", nextFloor: " + nextFloor);
        for(int k=currentFloor; k<nextFloor; k++){
            distance += interFloorDistance2[k];
        }

        double time = 0.0;
        double accTime = speed/acc;
        double deaccTime = speed/deacc;
        double accDistance = 0.5*acc*accTime*accTime;
        double deaccDistance = 0.5*deacc*deaccTime*deaccTime;
        double constSpeedTime = (distance - (accDistance + deaccDistance))/speed;
        time = accTime + constSpeedTime + deaccTime;
        System.out.println("time: " + time);
        System.out.println();
        return time;
    }

    public static int removeDuplicateElements(int arr[], int n){  
        if (n==0 || n==1){  
            return n;  
        }  
        int[] temp = new int[n];  
        int j = 0;  
        for (int i=0; i<n-1; i++){  
            if (arr[i] != arr[i+1]){  
                temp[j++] = arr[i];  
            }  
         }  
        temp[j++] = arr[n-1];     
        // Changing original array  
        for (int i=0; i<j; i++){  
            arr[i] = temp[i];  
        }  
        return j;  
    }  

    public static int[] removeDuplicates(int[] arr){
        HashSet<Integer> set = new HashSet<>();
        final int len = arr.length;
        //changed end to len
        for(int i = 0; i < len; i++){
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