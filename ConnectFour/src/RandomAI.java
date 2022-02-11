import java.util.ArrayList;

public class RandomAI implements ConnectFourAI {
    private ArrayList<Integer> cols;

    public int findMove(ConnectFour game) {
        cols = new ArrayList<>();
        cols.add(0);
        cols.add(1);
        cols.add(2);
        cols.add(3);
        cols.add(4);
        cols.add(5);
        cols.add(6);
        int rand;
        do {
            rand = (int) (Math.random() * cols.size());
        } while (game.getCell(rand, 5) != 0 && cols.size() > 0);
        return rand;
    }

    public void update() {
    }
}
