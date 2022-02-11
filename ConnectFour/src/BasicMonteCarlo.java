
public class BasicMonteCarlo extends MonteCarloAI {
    public BasicMonteCarlo(int d, double s) {
        super(d, s);
    }

    public double staticEval(ConnectFour game) {
        if (game.getWinner() == 2) {
            return 0;
        }
        return (double) game.getWinner();
    }

    @Override
    public int hashCode() {
        int prime = 7;
        int result = 1;
        result += prime * getDepth();
        result += prime * Double.hashCode(getSmoothing());
        return result;
    }
}
