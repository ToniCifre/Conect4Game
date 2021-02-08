package com.ToniC;

import edu.epsevg.prop.lab.c4.IAuto;
import edu.epsevg.prop.lab.c4.Jugador;
import edu.epsevg.prop.lab.c4.Tauler;

import java.util.ArrayList;
import java.util.List;

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author Profe
 */
public class MinMax_1
  implements Jugador, IAuto
{
    private int depth, color;
    private float bestAlpha;
    private String nom;

    public MinMax_1(int depth) {
        this.depth = depth;
        nom = "TuPutaMadre";
  }

    @Override
    public int moviment(Tauler t, int color)
  {
      this.color = color;
      bestAlpha = Float.NEGATIVE_INFINITY;
      int bestmove = -1;

      for(int moiment : getMoiments(t)) {
          Tauler nouTauler = new Tauler(t);
          nouTauler.afegeix(moiment, color);
          if (nouTauler.solucio(moiment, color)) return moiment;

          float aux = min_value(nouTauler, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, depth);

          System.out.println("Moiment: "+moiment+"   /Valor: "+aux);

          if (aux > bestAlpha) {
              bestmove = moiment;
              bestAlpha = aux;
          }
      }
      System.out.println("Move: "+bestmove);

      return bestmove;
  }

    private List<Integer> getMoiments(Tauler t) {
        List<Integer> moimentPosibles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (t.movpossible(i)) {
                moimentPosibles.add(i);
            }
        }
        return moimentPosibles;
    }

    private float max_value(Tauler t, float alpha, float beta, int d){
        if(d ==0) {float f = heuristica(t, color);
//            System.out.println(f);
            if (Float.isNaN(f)){f = 0;}
            return  f;}
        else{

            for(int moiment : getMoiments(t)) {
                Tauler nouTauler = new Tauler(t);
                nouTauler.afegeix(moiment,color);

                if(nouTauler.solucio(moiment, color)){ return Float.POSITIVE_INFINITY; }

                alpha = Math.max(alpha, min_value(nouTauler, alpha, beta, d));
                if(beta <= alpha) { return beta; }
            }

            return alpha;
        }
    }

    private float min_value(Tauler t, float alpha, float beta, int d) {

        for(int moiment : getMoiments(t)){
            Tauler nouTauler = new Tauler(t);
            nouTauler.afegeix(moiment,-color);

            if(nouTauler.solucio(moiment, -color)){ return Float.NEGATIVE_INFINITY; }

            beta = Math.min(beta, max_value(nouTauler, alpha, beta, d-1));
            if(beta <= alpha) { return alpha; }
        }

        return beta;
    }


    public float heuristica(Tauler t, int color){
      float max = 0;
      for(int j = 0; j < t.getMida(); j++){
          for(int i = t.getMida()-1; i > -1 && t.getColor(t.getMida()-1,j) == 0; i--){
                  if(t.getColor(i,j) == 0){
                      if(i>0)if(t.getColor(i-1,j) != 0){
                          max+=vertical(t, j, i-1, color);
                      }
                      if(j>0)if(t.getColor(i, j-1) != 0){
                          max+=horizontalI(t,j-1,i,color);
                      }
                      if(j<t.getMida()-1)if(t.getColor(i, j+1) != 0){
                          max+=horizontalD(t,j+1,i,color);
                      }
                      if(j<t.getMida()-1 && i>0)if(t.getColor(i-1,j+1) != 0){
                          max+=diagonalDchInf(t, j+1, i-1, color);
                      }
                      if(j<t.getMida()-1 && i<t.getMida()-1)if(t.getColor(i+1,j+1) != 0){
                          max+=diagonalDchSup(t, j+1, i+1, color);
                      }
                      if(j>0 && i>0)if(t.getColor(i-1,j-1) != 0){
                          max+=diagonalEsqInf(t, j-1, i-1, color);
                      }
                      if(j>0 && i>t.getMida()-1)if(t.getColor(i+1,j-1) != 0){
                          max+=diagonalEsqSup(t, j-1, i+1, color);
                      }
                  }
          }
      }
      return max;
  }
  
  public float vertical(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = alt; i >= 0 && !trobat; i--){
          if(color != t.getColor(i,col))trobat = true;
          else ++valor;
      }
      
      valor = valor * espacioVacio(t, col, alt+1);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  public float horizontalI(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i >= 0 && !trobat; i--){
          if(color != t.getColor(alt,i))trobat = true;
          else ++valor;
      }
  
      valor = valor * espacioVacio(t, col+1, alt);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  public float horizontalD(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i < t.getMida() && !trobat; i++){
          if(color != t.getColor(alt,i))trobat = true;
          else ++valor;
      }
      
      valor = valor * espacioVacio(t, col-1, alt);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalDchInf(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i < t.getMida() && !trobat; i++){
          for(int j = alt; j >= 0 && !trobat; j--){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col-1, alt+1);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalDchSup(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i < t.getMida() && !trobat; i++){
          for(int j = alt; j < t.getMida() && !trobat; j++){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col-1, alt-1);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalEsqInf(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i >= 0 && !trobat; i--){
          for(int j = alt; j >= 0 && !trobat; j--){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col+1, alt+1);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalEsqSup(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i >= 0 && !trobat; i--){
          for(int j = alt; j < t.getMida() && !trobat; j++){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col+1, alt-1);
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
      else return valor;
  }
  
  //Si la ficha inferior es suelo (fuera de las medidas del tablero) o
  //su color != 0, return 1, en otro caso, la distancia hasta una de estos dos
  //casos
  public float espacioVacio(Tauler t, int col, int alt){
      float vuit = 0;
      boolean trobat = false;
      for(int i = alt; i >= 0 && !trobat; i--){
          if(t.getColor(col,i) == 0)++vuit;
          else trobat = true;
      }
      if(vuit == 0)return 0;
      return 1/vuit;
  }
    @Override
  public String nom()
  {
    return nom;
  }
  
}
