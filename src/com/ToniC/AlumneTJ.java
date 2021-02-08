package com.ToniC;

import edu.epsevg.prop.lab.c4.IAuto;
import edu.epsevg.prop.lab.c4.Jugador;
import edu.epsevg.prop.lab.c4.Tauler;

import java.util.ArrayList;
import java.util.List;

public class AlumneTJ implements Jugador, IAuto {
    private int depth, color, prof, prof2, bestAlpha;
    private boolean heuristic;

    AlumneTJ(int depth, boolean heuristic) {
        this.depth = depth;
        this.heuristic = heuristic;
    }

    @Override
    public String nom() { return "TJ-D"+depth+"-H"+(heuristic?1:2); }

    /**
     * Genera un arbre min max amb poda alpha beta de cada moiment possible per decidir, a partir del alpha, quin dels
     * moiments ens porta a un estat del tauler més favorable.
     *
     * @param tauler Classe Tauler
     * @param color Enter que reppartialScoreenta el color de la nostra fixa (Vermell = 1, Blau = -1)
     * @return Enter entre 0 i 7.
     */
    @Override
    public int moviment(Tauler tauler, int color) {
        this.color = color;
        int bestmove = -1, minProf = 0, maxProf=depth+1;

        bestAlpha = Integer.MIN_VALUE;

        List<Integer> l = getMoviments(tauler);
        int t = checkMoves(tauler, l);
        if (t!= -1) return t;
        for(int moviment : l) {
            prof = 0;prof2 = depth;

            Tauler nouTauler = new Tauler(tauler);
            nouTauler.afegeix(moviment, color);

            int aux = min_value(nouTauler, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);

            if (aux > bestAlpha) {
                bestmove = moviment;
                bestAlpha = aux;
                if (aux == Integer.MAX_VALUE) {
                    minProf = prof;
                }
            }else if(aux == bestAlpha) {
                if (bestAlpha == Integer.MAX_VALUE && prof > minProf) {
                    bestmove = moviment;
                    minProf = prof;
                }
                if (aux == Integer.MIN_VALUE && prof2 < maxProf) {
                        bestmove = moviment;
                        maxProf = prof2;
                        bestAlpha = aux;
                }
            }
        }
        return bestmove;
    }

    /**
     * Retorna el primer moiment necesari en cas de que ens faltes un sol moiment per guanyar, sino retorna -1.
     * @param tauler Classe Tauler.
     * @param l Llista amb tots els moiments possibles.
     * @return Un enter entre -1 i 7.
     */
    int checkMoves(Tauler tauler, List<Integer> l){
        for(int moviment : l) {
            Tauler nouTauler = new Tauler(tauler);
            nouTauler.afegeix(moviment, color);
            if (nouTauler.solucio(moviment, color)) return moviment;
        }
        return -1;
    }

    /**
     * Retorna les posicións de les col·lumnes que ens permeten introduir una fixa.
     *
     * @param t Classe Tauler.
     * @return Llista d'enters
     */
    private List<Integer> getMoviments(Tauler t) {
        List<Integer> movimentPosibles = new ArrayList<>();
        for (int move = 0; move < 8; move++) {
            if (t.movpossible(move)) {
                movimentPosibles.add(move);
            }
        }
        return movimentPosibles;
    }

    /**
     *
     * @param t Classe Tauler.
     * @param alpha Enter Alpha.
     * @param beta Enter Beta.
     * @param d Enter profunditat.
     * @return Enter.
     */
    private int max_value(Tauler t, int alpha, int beta, int d){
        if(d==0) {
            if(heuristic) return heuristic(t);
            else return heuristic2(t);
        }
        else{
            for(int moviment : getMoviments(t)) {
                Tauler nouTauler = new Tauler(t);
                nouTauler.afegeix(moviment,color);

                if(nouTauler.solucio(moviment, color)){ prof=d; return Integer.MAX_VALUE; }

                alpha = Math.max(alpha, min_value(nouTauler, alpha, beta, d));
                if(beta <= alpha) return beta;
            }
            return alpha;
        }
    }

    private int min_value(Tauler t, int alpha, int beta, int d) {
        for(int moviment : getMoviments(t)){
            Tauler nouTauler = new Tauler(t);
            nouTauler.afegeix(moviment,-color);
            if(nouTauler.solucio(moviment, -color)){ prof2 = d; return Integer.MIN_VALUE;}
            beta = Math.min(beta, max_value(nouTauler, alpha, beta, d-1));
            if(beta < bestAlpha) return Integer.MIN_VALUE;
            if(beta <= alpha) return alpha; 
        }
        return beta;
    }

// --------------------------- Heuristic 1

    private int ponderate(int j, int partialScore){
        switch (j) {
            case 0:
            case 7:
                return partialScore;
            case 1:
            case 6:
                return 2*partialScore;
            case 2:
            case 5:
                return 3*partialScore;
            case 3:
            case 4:
                return 4*partialScore;
            default:
                return -1;
        }
    }
    private int heuristic(Tauler board) {
        int score = 0;
        int mida =  board.getMida();

        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if(board.getColor(i,j)!=0){
                    if(board.getColor(i,j) == color){ 
                        score += ponderate(j, partialScore(i, j, color, board));
                    }
                    else 
                        score -= ponderate(j, partialScore(i, j, -color, board));
                }
            }
        }
        return score;
    }
    private int verticalScore(int i, int j, int color, Tauler t, int c){
        int partialScore=1;
        if (i>t.getMida()-1 || c>2) return 0;
        else{
            if (t.getColor(i,j) !=-color)return partialScore + verticalScore(i+1, j, color, t, c+1);
            else return 0;
        }
    }
    private int horizontalScore(int i, int j, int color, Tauler t, int c){
        int partialScore=1;
        if (j>t.getMida()-1 || c>2) return 0;
        else{
            if (t.getColor(i,j) !=-color) return partialScore + horizontalScore(i, j+1, color, t, c+1);
            else return 0;
        }    
    }
    private int rdiagonalScore(int i, int j, int color, Tauler t, int c){
        int partialScore=1;
        if (i>t.getMida()-1 || j>t.getMida()-1 || c>2) return 0;
        else{
            if (t.getColor(i,j) !=-color) return partialScore + rdiagonalScore(i+1, j+1, color, t, c+1);
            else return 0;
        }
    }
    private int ldiagonalScore(int i, int j, int color, Tauler t, int c){
        int partialScore=1;
        if (i>t.getMida()-1 || j<0 || c>2) return 0;
        else{
            if(t.getColor(i, j)!=-color) return partialScore + ldiagonalScore(i+1, j-1, color, t, c+1);
            else return 0;
        }
    }
    private int partialScore(int i, int j, int color, Tauler t){
        int vertical=1;
        int horizontal=1;
        int rdiagonal=1;
        int ldiagonal=1;
        vertical+=verticalScore(i+1, j, color, t, 0);
        rdiagonal+=rdiagonalScore(i+1, j+1, color, t, 0);
        ldiagonal+=ldiagonalScore(i+1, j-1, color, t, 0);
        horizontal+=horizontalScore(i, j+1, color, t, 0);
        return vertical + horizontal + rdiagonal + ldiagonal;
    }





//------------------------------------ Heuristic 2

    private int calculatepower(double n){
        return (int) Math.pow(4, n);
    }

    int heuristic2(Tauler board) {
        int puntuacio = 0;
        int mida =  board.getMida();

        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if(board.getColor(i,j) == color){
                    puntuacio += toni2(i, j, color, board);
                }else if (board.getColor(i,j) == -color){
                    puntuacio -= toni2(i, j, -color, board);
                }
            }
        }

        return puntuacio;
    }

    private int toni2(int i, int j, int color, Tauler board){
        double c1 = 0;
        int f = 0;
        int dd = 0;
        int de = 0;
        if(i+3<board.getMida()){
            if(     board.getColor(i+1,j) != -color &&
                    board.getColor(i+2,j) != -color &&
                    board.getColor(i+3,j) != -color){

                if(board.getColor(i+1,j) == color) {
                    if(board.getColor(i+2,j) == color) {
                        f = calculatepower(3.3);
                    }else{f = calculatepower(2.3);}
                }else{f = calculatepower(1.3);}
            }
        }

        if(j == 0){
            if(     board.getColor(i,j+1) != -color &&
                    board.getColor(i,j+2) != -color &&
                    board.getColor(i,j+3) != -color){
                c1++;
                if(board.getColor(i,j+1) == color) {c1++;
                } else if (i-1 < 0 || board.getColor(i-1,j+1) != 0){c1+=0.3;}
                if(board.getColor(i,j+2) == color) {c1++;
                } else if (i-1 < 0 || board.getColor(i-1,j+2) != 0){c1+=0.3;}
                if(board.getColor(i,j+3) == color) {c1++;
                } else if (i-1 < 0 || board.getColor(i-1,j+3) != 0){c1+=0.3;}
                c1 = calculatepower(c1);
            }
        }else if(j+3<board.getMida() ){
            if(     board.getColor(i,j+1) != -color &&
                    board.getColor(i,j+2) != -color &&
                    (board.getColor(i,j+3) != -color || board.getColor(i,j-1) != -color)){
                c1++;
                if(board.getColor(i,j+1) == color) { c1++;
                } else if (i-1 < 0 || board.getColor(i-1,j+1) != 0){c1+=0.3;}
                if(board.getColor(i,j+2) == color) { c1++;
                } else if (i-1 < 0 || board.getColor(i-1,j+2) != 0){c1+=0.3;}
                if(board.getColor(i,j+3) == color || board.getColor(i,j-1) == color) { c1++;
                } else if (i-1 < 0 || board.getColor(i-1,j+3) != 0 || board.getColor(i-1,j-1) != 0){ c1+=0.3; }
                c1 = calculatepower(c1);
            }
        }


        if(i+3<board.getMida() && j+3<board.getMida()){
            if(     board.getColor(i+1,j+1) != -color &&
                    board.getColor(i+2,j+2) != -color &&
                    board.getColor(i+3,j+3) != -color){
                dd++;
                if(board.getColor(i+1,j+1) == color){ dd++;
                }else if (board.getColor(i,j+1) != 0){dd+=0.3;}
                if(board.getColor(i+2,j+2) == color){ dd++;
                }else if (board.getColor(i+1,j+2) != 0){dd+=0.3;}
                if(board.getColor(i+3,j+3) == color){ dd++;
                }else if (board.getColor(i+2,j+3) != 0){dd+=0.3;}
            }
            dd = calculatepower(dd);
        }
        if(i+3<board.getMida() && j-3>=0){
            if(     board.getColor(i+1,j-1) != -color &&
                    board.getColor(i+2,j-2) != -color &&
                    board.getColor(i+3,j-3) != -color){
                de++;
                if(board.getColor(i+1,j-1) == color){ de++;
                }else if (board.getColor(i,j-1) != 0){de+=0.3;}
                if(board.getColor(i+2,j-2) == color){ de++;
                }else if (board.getColor(i+1,j-2) != 0){de+=0.3;}
                if(board.getColor(i+3,j-3) == color){ de++;
                }else if (board.getColor(i+2,j-3) != 0){de+=0.3;}
            }
            de = calculatepower(de);
        }


        return (int) (c1 + f + dd + de);
    }

}