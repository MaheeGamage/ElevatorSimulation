//package simulator;

public class Person {
    int floor;
    double waitTime;
    double totTime;
    double arriveTime;
    boolean isFinish;

    public Person(){
        waitTime = 0.0;
        totTime = 0.0;
        isFinish = false;
    }
}