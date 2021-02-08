package com.ToniC;

import edu.epsevg.prop.lab.c4.IAuto;
import edu.epsevg.prop.lab.c4.Jugador;
import edu.epsevg.prop.lab.c4.Tauler;

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author Profe
 */
public class Aleatori
  implements Jugador, IAuto
{
  private String nom;
  
  public Aleatori()
  {
    nom = "RandomBanzai";
  }
  
  public int moviment(Tauler t, int color)
  {
    int col = (int)(8.0D * Math.random());
    while (!t.movpossible(col)) {
      col = (int)(8.0D * Math.random());
    }
    return col;
  }
  
  public String nom()
  {
    return nom;
  }
}
