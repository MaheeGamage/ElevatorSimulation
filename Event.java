public class Event{
    double time;
    int type;
    Person p;
    Elevator e;
    int numberOfPeople;

    Event(double time, int type, Person p){
        this.time = time;
        this.type = type;
        this.p = p;
    }

    Event(double time, int type, Elevator e, int numberOfPeople){
        this.time = time;
        this.type = type;
        this.e = e;
        this.numberOfPeople = numberOfPeople;
    }

    Event(double time, int type, Elevator e){
        this.time = time;
        this.type = type;
        this.e = e;
    }

    public Person getPerson(){
        return p;
    }

    public Elevator getElevator(){
        return e;
    }
}