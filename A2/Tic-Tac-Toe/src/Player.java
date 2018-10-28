import java.util.*;


public class Player {

    private int[][] winningCases = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15},
            {0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, {0, 5, 10, 15}, {3, 6, 9, 12}};


    /**
     * Performs a move
     *
     * @param gameState the current state of the board
     * @param deadline  time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */

        int v = Integer.MIN_VALUE;
        int max;
        GameState move = new GameState();
        for (int i = 0; i < nextStates.size(); i++) {
            int level = 3;
            max = minimax(nextStates.elementAt(i), level, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getNextPlayer());
            if (max > v) {
                move = nextStates.elementAt(i);
                v = max;
            }

        }
        //Random random = new Random();
        //return nextStates.elementAt(random.nextInt(nextStates.size()));
        return move;
    }

    public int minimax(GameState state, int level, double alpha, double beta, int nextPlayer) {
        if (nextPlayer == Constants.CELL_X) {
            return minValue(state, level, alpha, beta);
        } else {
            return maxValue(state, level, alpha, beta);
        }
    }

    public int maxValue(GameState state, int level, double alpha, double beta) {
        Vector<GameState> states = new Vector<GameState>();
        state.findPossibleMoves(states);

        if (level == 0 || states.size() == 0) {
            return eval(state);
        }
        int v = Integer.MIN_VALUE;
        for (int i = 0; i < states.size(); i++) {
            v = Math.max(v, minimax(states.elementAt(i), level - 1, alpha, beta, state.getNextPlayer()));
            alpha = Math.max(v, alpha);
            if (beta <= alpha) {
                break;
            }
        }
        return v;
    }

    public int minValue(GameState state, int level, double alpha, double beta) {
        Vector<GameState> states = new Vector<GameState>();
        state.findPossibleMoves(states);

        if (level == 0 || states.size() == 0) {
            return eval(state);
        }

        int v = Integer.MAX_VALUE;
        for (int i = 0; i < states.size(); i++) {
            v = Math.min(v, minimax(states.elementAt(i), level - 1, alpha, beta, state.getNextPlayer()));
            beta = Math.min(v, beta);
            if (beta <= alpha) {
                break;
            }
        }
        return v;
    }


    public int eval(GameState state) {
        int x, o;
        int sum = 0;

            if (state.isEOG() && state.isXWin()) {
                return 1000;
            } else if (state.isEOG() && state.isOWin()) {
                return -1000;
            }
                for (int i = 0; i < winningCases.length; i++) {
                    x = 0;
                    o = 0;
                    for (int j = 0; j < winningCases[0].length; j++) {
                        if (state.at(winningCases[i][j]) == Constants.CELL_X) {
                            x++;
                        } else if (state.at(winningCases[i][j]) == Constants.CELL_O) {
                            o++;
                        }
                    }
                        if (x == 1 && o == 0) {
                            sum += 5;
                        } else if (x == 2 && o == 0) {
                            sum += 10;
                        } else if (x == 3 && o == 0) {
                            sum += 30;
                        } else if (x == 0 && o == 1) {
                            sum += 0;
                        } else if (x == 0 && o == 2) {
                            sum += 0;
                        } else if (x == 0 && o == 3) {
                            sum += -1;
                        } else if (x == 0 && o == 0) {
                            sum += 3;
                        } else {
                            sum += 2;
                        }

                    }
                return sum;
            }

        }


