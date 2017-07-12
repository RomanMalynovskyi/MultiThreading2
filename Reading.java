/**
 * Created by Малиновский Роман on 10.07.2017.
 */
public class Reading extends Thread {

    private Action action;

    public Reading(Action action) {
        this.action = action;
    }

    @Override
    public void run() {
        action.readFile();
    }
}
