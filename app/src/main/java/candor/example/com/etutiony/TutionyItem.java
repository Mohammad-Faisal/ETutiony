package candor.example.com.etutiony;

public class TutionyItem {

    String description;
    int cycle;
    int current_days;
    long time_stamp;

    TutionyItem (){}

    public int getCurrent_days() {
        return current_days;
    }

    public int getCycle() {
        return cycle;
    }

    public String getDescription() {
        return description;
    }

    public long getTime_stamp() {
        return time_stamp;
    }
}
