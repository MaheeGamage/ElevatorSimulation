//time will be calculated 

public class simulator {

    public static void main(String[] args) {

        System.out.println("Hello World"); // prints Hello World
    }
}

class Building {
    Person[] queue = new Person[10];
    Elevator[] elevators = new Elevator[4];
    final int NumberOfFloors = 10;
    double spendTime;

    Building(){
        spendTime = 0.0;
    }

    public run() {
        //Initializing queue
        for(int i=0; i<10; i++){
            Person p = new Person();
            p.arriveTime = i*10;
            p.Floor = nextInt(10);
            queue[i] = p;
        }

        for (Person p : queue) {
            System.out.println(p.toString());
        }

        for(int i=0; i<4; i++){
            Elevator e = new Elevator(10, NumberOfFloors);

        }

        //Running main simulation
        int isCompelte = false;
        while(true){
            
        }

    }
}