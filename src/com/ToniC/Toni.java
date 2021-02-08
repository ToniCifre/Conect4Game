package com.ToniC;

import edu.epsevg.prop.lab.c4.IAuto;
import edu.epsevg.prop.lab.c4.Jugador;
import edu.epsevg.prop.lab.c4.Tauler;

import java.util.ArrayList;
import java.util.List;

public class Toni implements Jugador, IAuto {
    private int depth, color, bestAlpha, prof;

    Toni(int depth) {
        this.depth = depth;
    }

    @Override
    public String nom() {
        return "Toni-D";
    }

    /**
     * Genera un arbre min max amb poda alpha beta de cada moiment possible per decidir, a partir del alpha, quin dels
     * moiments ens porta a un estat del tauler més favorable.
     *
     * @param tauler Classe Tauler
     * @param color  Enter que representa el color de la nostra fixa (Vermell = 1, Blau = -1)
     * @return Enter entre 0 i 7.
     */
    @Override
    public int moviment(Tauler tauler, int color) {
        this.color = color;
        int bestmove = -1, minProf = 0;

        bestAlpha = Integer.MIN_VALUE;

        List<Integer> l = getMoiments(tauler);
        for (int moiment : l) {
            prof = 0;
            int t;if ((t = checkMoves(tauler, l)) != -1) return t;

            Tauler nouTauler = new Tauler(tauler);
            nouTauler.afegeix(moiment, color);

            int aux = min_value(nouTauler, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);

            System.out.println("Move: " + moiment + " alpha: " + aux + " prof:" + prof + " | " + minProf);

            if (aux >= bestAlpha) {
                if (bestAlpha == Integer.MAX_VALUE) {
                    if (prof > minProf) {
                        bestmove = moiment;
                        bestAlpha = aux;
                        minProf = prof;
                    }
                } else {
                    bestmove = moiment;
                    bestAlpha = aux;
                    if (aux == Integer.MAX_VALUE) {
                        minProf = prof;
                    }
                }
            }
        }
        System.out.println(bestmove);
        return bestmove;
    }

    /**
     * Retorna el primer moiment necesari en cas de que ens faltes un sol moiment per guanyar, sino retorna -1.
     *
     * @param tauler Classe Tauler.
     * @param l      Llista amb tots els moiments possibles.
     * @return Un enter entre -1 i 7.
     */
    int checkMoves(Tauler tauler, List<Integer> l) {
        for (int moiment : l) {
            Tauler nouTauler = new Tauler(tauler);
            nouTauler.afegeix(moiment, color);
            if (nouTauler.solucio(moiment, color)) return moiment;
        }
        return -1;
    }

    /**
     * Retorna les posicións de les col·lumnes que ens permeten introduir una fixa.
     *
     * @param t Classe Tauler.
     * @return Llista d'enters
     */
    private List<Integer> getMoiments(Tauler t) {
        List<Integer> moimentPosibles = new ArrayList<>();
        for (int move = 0; move < 8; move++) {
            if (t.movpossible(move)) {
                moimentPosibles.add(move);
            }
        }
        return moimentPosibles;
    }

    /**
     * @param t     Classe Tauler.
     * @param alpha Enter Alpha.
     * @param beta  Enter Beta.
     * @param d     Enter profunditat.
     * @return Enter.
     */
    private int max_value(Tauler t, int alpha, int beta, int d) {
        if (d == 0) { return evalc2(t);
        } else {
            for (int moiment : getMoiments(t)) {
                Tauler nouTauler = new Tauler(t);
                nouTauler.afegeix(moiment, color);

                if (nouTauler.solucio(moiment, color)) {
                    prof = d;
                    return Integer.MAX_VALUE;
                }

                alpha = Math.max(alpha, min_value(nouTauler, alpha, beta, d));
                if (beta <= alpha) return beta;
            }

            return alpha;
        }
    }

    private int min_value(Tauler t, int alpha, int beta, int d) {
        for (int moiment : getMoiments(t)) {
            Tauler nouTauler = new Tauler(t);
            nouTauler.afegeix(moiment, -color);

            if (nouTauler.solucio(moiment, -color)) return Integer.MIN_VALUE;

            beta = Math.min(beta, max_value(nouTauler, alpha, beta, d - 1));
            if (beta < bestAlpha) {
                return Integer.MIN_VALUE;
            }
            if (beta <= alpha) {
                return alpha;
            }
        }

        return beta;
    }


    private int calculatepower(double n) {
        return (int) Math.pow(2, n);
    }

    int evalc2(Tauler board) {
        int puntuacio = 0;
        int mida = board.getMida();

        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if (board.getColor(i, j) == color) {
                    puntuacio += toni2(i, j, color, board);
                } else if (board.getColor(i, j) == -color) {
                    puntuacio -= toni2(i, j, -color, board);
                }
            }
        }

        return puntuacio;
    }

    private int toni2(int i, int j, int color, Tauler board) {
        double c1 = 0;
        int f = 0;
        int dd = 0;
        int de = 0;
        if (i + 3 < board.getMida()) {
            if (board.getColor(i + 1, j) != -color &&
                    board.getColor(i + 2, j) != -color &&
                    board.getColor(i + 3, j) != -color) {

                if (board.getColor(i + 1, j) == color) {
                    if (board.getColor(i + 2, j) == color) {
                        f = calculatepower(3.3);
                    } else {
                        f = calculatepower(2.3);
                    }
                } else {
                    f = calculatepower(1.3);
                }
            }
        }

        if (j == 0) {
            if (board.getColor(i, j + 1) != -color &&
                    board.getColor(i, j + 2) != -color &&
                    board.getColor(i, j + 3) != -color) {
                c1++;
                if (board.getColor(i, j + 1) == color) {
                    c1++;
                } else if (i - 1 < 0 || board.getColor(i - 1, j + 1) != 0) {
                    c1 += 0.3;
                }
                if (board.getColor(i, j + 2) == color) {
                    c1++;
                } else if (i - 1 < 0 || board.getColor(i - 1, j + 2) != 0) {
                    c1 += 0.3;
                }
                if (board.getColor(i, j + 3) == color) {
                    c1++;
                } else if (i - 1 < 0 || board.getColor(i - 1, j + 3) != 0) {
                    c1 += 0.3;
                }
                c1 = calculatepower(c1);
            }
        } else if (j + 3 < board.getMida()) {
            if (board.getColor(i, j + 1) != -color &&
                    board.getColor(i, j + 2) != -color &&
                    (board.getColor(i, j + 3) != -color || board.getColor(i, j - 1) != -color)) {
                c1++;
                if (board.getColor(i, j + 1) == color) {
                    c1++;
                } else if (i - 1 < 0 || board.getColor(i - 1, j + 1) != 0) {
                    c1 += 0.3;
                }
                if (board.getColor(i, j + 2) == color) {
                    c1++;
                } else if (i - 1 < 0 || board.getColor(i - 1, j + 2) != 0) {
                    c1 += 0.3;
                }
                if (board.getColor(i, j + 3) == color || board.getColor(i, j - 1) == color) {
                    c1++;
                } else if (i - 1 < 0 || board.getColor(i - 1, j + 3) != 0 || board.getColor(i - 1, j - 1) != 0) {
                    c1 += 0.3;
                }
                c1 = calculatepower(c1);
            }
        }


        if (i + 3 < board.getMida() && j + 3 < board.getMida()) {
            if (board.getColor(i + 1, j + 1) != -color &&
                    board.getColor(i + 2, j + 2) != -color &&
                    board.getColor(i + 3, j + 3) != -color) {
                dd++;
                if (board.getColor(i + 1, j + 1) == color) {
                    dd++;
                } else if (board.getColor(i, j + 1) != 0) {
                    dd += 0.3;
                }
                if (board.getColor(i + 2, j + 2) == color) {
                    dd++;
                } else if (board.getColor(i + 1, j + 2) != 0) {
                    dd += 0.3;
                }
                if (board.getColor(i + 3, j + 3) == color) {
                    dd++;
                } else if (board.getColor(i + 2, j + 3) != 0) {
                    dd += 0.3;
                }
            }
            dd = calculatepower(dd);
        }
        if (i + 3 < board.getMida() && j - 3 >= 0) {
            if (board.getColor(i + 1, j - 1) != -color &&
                    board.getColor(i + 2, j - 2) != -color &&
                    board.getColor(i + 3, j - 3) != -color) {
                de++;
                if (board.getColor(i + 1, j - 1) == color) {
                    de++;
                } else if (board.getColor(i, j - 1) != 0) {
                    de += 0.3;
                }
                if (board.getColor(i + 2, j - 2) == color) {
                    de++;
                } else if (board.getColor(i + 1, j - 2) != 0) {
                    de += 0.3;
                }
                if (board.getColor(i + 3, j - 3) == color) {
                    de++;
                } else if (board.getColor(i + 2, j - 3) != 0) {
                    de += 0.3;
                }
            }
            de = calculatepower(de);
        }


        return (int) (c1 + f + dd + de);
    }

}