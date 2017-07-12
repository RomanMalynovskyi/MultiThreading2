/**
 * Created by Малиновский Роман on 10.07.2017.
 */
public class Main {
    public static void main(String[] args) {
        Action action = new Action("c://Табель//1.txt", "c://Табель//2.txt", 3);
        Reading reading = new Reading(action);
        Writing writing = new Writing(action);
        
        reading.start();
        writing.start();

    }
}
