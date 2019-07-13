//package simulator;

public class Person {
    int id;
    int floor;
    double waitTime;
    double journeyTime;
    double totTime;
    double arriveTime;
    boolean isFinish;

    public Person(int id, double arriveTime, int floor){
        this.waitTime = 0.0;
        this.totTime = 0.0;
        this.journeyTime = 0.0;
        this.isFinish = false;
        this.id = id;
        this. arriveTime = arriveTime;
        this.floor = floor;
    }
}