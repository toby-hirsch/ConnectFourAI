import java.util.ArrayList;
import java.util.Arrays;

public class AITournament {
    private ArrayList<Entry> bots;
    private int[][] scores;
    private int drawCount;
    private int blackCount;
    private int redCount;

    public AITournament() {
        drawCount = 0;
        blackCount = 0;
        redCount = 0;
        bots = new ArrayList<>();
        bots.add(new Entry(new RandomAI(), "random"));
        bots.add(new Entry(new BasicMonteCarlo(3, 0.1), "basic Monte Carlo, d=3, s=0.1"));
        bots.add(new Entry(new BasicMonteCarlo(5, 0.1), "basic Monte Carlo, d=5, s=0.1"));
        bots.add(new Entry(new MiddleMonteCarlo(3, 0.1), "middle Monte Carlo, d=3, s=0.1"));
        bots.add(new Entry(new MiddleMonteCarlo(5, 0.1), "middle Monte Carlo, d=5, s=0.1"));
        bots.add(new Entry(new MiddleMonteCarlo(3, 0.3), "middle Monte Carlo, d=3, s=0.3"));
        bots.add(new Entry(new MiddleMonteCarlo(5, 0.3), "middle Monte Carlo, d=5, s=0.3"));
        bots.add(new Entry(new MiddleMonteCarlo(3, 0.5), "middle Monte Carlo, d=3, s=0.5"));
        bots.add(new Entry(new MiddleMonteCarlo(5, 0.5), "middle Monte Carlo, d=5, s=0.5"));
        bots.add(new Entry(new BasicMonteCarlo(7, 0.1), "basic Monte Carlo, d=7, s=0.1"));
        bots.add(new Entry(new MiddleMonteCarlo(7, 0.1), "middle Monte Carlo, d=7, s=0.1"));
        bots.add(new Entry(new MiddleMonteCarlo(7, 0.3), "middle Monte Carlo, d=7, s=0.3"));
        bots.add(new Entry(new MiddleMonteCarlo(7, 0.5), "middle Monte Carlo, d=7, s=0.5"));
        int numBots = bots.size();
        scores = new int[numBots][numBots];
    }

    public static void main(String[] args) {
        AITournament trn = new AITournament();
        trn.runTournament();
    }

    public void runTournament() {
        int s = bots.size();
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s; j++) {
                int[] ws = playGames(bots.get(i), bots.get(j));
                scores[i][j] = ws[0];
                scores[j][i] = ws[1];
            }
        }
        for (int i = 0; i < s; i++) {
            System.out.println(bots.get(i) + ": " + Arrays.stream(scores[i]).sum());
        }
        System.out.println("draws: " + drawCount);
        System.out.println("black wins: " + blackCount);
        System.out.println("red wins: " + redCount);
    }

    public int[] playGames(Entry a, Entry b) {
        int[] pts = playGame(a, b);
        int[] temp = playGame(b, a);
        pts[0] += temp[1];
        pts[1] += temp[0];
        System.out.println(a + " won " + pts[0] + " games against " + b);
        return pts;
    }

    public int[] playGame(Entry a, Entry b) {
        ConnectFour game = new ConnectFour();
        while (game.getWinner() == 2) {
            a.playMove(game);
            b.playMove(game);
        }
        switch (game.getWinner()) {
            case -1:
                redCount++;
                return new int[] { 0, 100 };
            case 0:
                drawCount++;
                return new int[] { 49, 49 };
            default:
                blackCount++;
                return new int[] { 100, 0 };
        }
    }
}

class Entry {
    private ConnectFourAI ai;
    private String name;

    public Entry(ConnectFourAI a, String s) {
        ai = a;
        name = s;
    }

    public boolean playMove(ConnectFour game) {
        return game.playMove(ai.findMove(game));
    }

    @Override
    public String toString() {
        return name;
    }
}
