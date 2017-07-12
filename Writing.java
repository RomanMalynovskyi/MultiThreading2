/**
 * Created by Малиновский Роман on 10.07.2017.
 */
public class Writing extends Thread {

    private Action action;

    public Writing(Action action) {
        this.action = action;
    }

    @Override
    public void run() {
        action.writeFile();
    }
}
