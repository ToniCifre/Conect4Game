Documentació del codi:
======================

AlumneTJ
--------

AlumneTJ(int depth, boolean heuristic) {\
this.depth = depth;\
this. heuristic= heuristic;\
}

Constructor de la classe la qual definirà la profunditat, en el nostre
cas, la profunditat representa en nombre de moviments nostres, es a dir,
que la profunditat real serà el doble, ja que 4 moviments nostres
representen 8 moviments totals. També definirà quin tipus d'heurística
aplicarem, ja que la nostra classe disposa de dos tipus diferents
d'estratègia.

Nom
---

\@Override\
public String nom() { return \"TJ-D\"+depth+\"-H\"+(heuristic?1:2); }

La Funció nom esta creada en la Classe Jugador, la qual nosaltres
implementem en la nostra classe per fer que formi part de dels jugadors
Aquesta funció retorna el nom per identificar-nos a l'hora de jugar una
partida.

Moviment
--------

*/\*\*\
\* **\@param** tauler Classe Tauler\
\* **\@param** color Enter que reppartialScoreenta el color de la nostra
fixa (Vermell = 1, Blau = -1)\
\* **\@return** Enter entre 0 i 7.\
\*/*

\@Override\
public int moviment(Tauler tauler, int color) {\
this.color = color;\
int bestmove = -1, minProf = 0, maxProf=depth+1;\
\
bestAlpha = Integer.*MIN\_VALUE*;\
\
List\<Integer\> l = getMoviments(tauler);\
int t = checkMoves(tauler, l);\
if (t!= -1) return t;\
for(int moviment : l) {\
prof = 0;prof2 = depth;\
\
Tauler nouTauler = new Tauler(tauler);\
nouTauler.afegeix(moviment, color);\
\
int aux = min\_value(nouTauler, Integer.*MIN\_VALUE*,
Integer.*MAX\_VALUE*, depth);\
\
if (aux \> bestAlpha) {\
bestmove = moviment;\
bestAlpha = aux;\
if (aux == Integer.*MAX\_VALUE*) {\
minProf = prof;\
}\
}else if(aux == bestAlpha) {\
if (bestAlpha == Integer.*MAX\_VALUE* && prof \> minProf) {\
bestmove = moviment;\
minProf = prof;\
}\
if (aux == Integer.*MIN\_VALUE* && prof2 \< maxProf) {\
bestmove = moviment;\
maxProf = prof2;\
bestAlpha = aux;\
}\
}\
}\
return bestmove;\
}

La funció moviments es l'encarregada de retornar la columna on volem que
el nostra jugador introdueixi la fixa. En el nostre cas, per agilitzar
la victòria, abans de tot mirarem si amb un sol moviment podríem
aconseguir la victòria, si no es el cas, es procedeix a mirar quin es el
millor moviment.

Per elegir la millor columna recorrem una llista on es troben tots els
moviments possibles i per cada un d'ells cridarem a la funció
min\_value(), la qual s'encarregarà d'aprofundir en l'arbre nim max i
ens retornarà la millor alpha per aquell moviment. Cada cop que ens
retorni l'alpha, mirarem si la bestAlpha que nosaltres tenim es més
petita, en aquest cas guardarem la nova alpha i el moviment com els
millors i continuarem. En el cas que la bestAlpha sigui infinita, i
l'alpha que ens retorna el min\_value() també ho sigui, triarem el
moviment en funció de la profunditat, es a dir, triarem la que en menys
moviments ens porti a la victòria, d'altra banda, en el cas de que la
funció min\_value() sols ens retorni els pitjor cas, escollirem el
moviment amb el qual triguem més en morir.

checkMoves
----------

*/\*\*\
\* **\@param** tauler Classe Tauler.\
\* **\@param** l Llista amb tots els moiments possibles.\
\* **\@return** Un enter entre -1 i 7.\
\*/\
*int checkMoves(Tauler tauler, List\<Integer\> l){\
for(int moviment : l) {\
Tauler nouTauler = new Tauler(tauler);\
nouTauler.afegeix(moviment, color);\
if (nouTauler.solucio(moviment, color)) return moviment;\
}\
return -1;\
}

Retorna el primer moviment per guanyar en cas de que ens faltes un sol
moviment, sinó retorna -1.

Aquesta classe esta enfocada a reduir el temps per escollir un moviment,
ja que en el cas de que en un sol moviment es pugui guanyar, no ens
interessa recórrer l'arbre min max, sinó retornar directament la comuna
necessària per guanyar.

getMoviments
------------

*/\*\*\
\* **\@param** t Classe Tauler.\
\* **\@return** Llista d\'enters entre 0 i 7.\
\*/\
*private List\<Integer\> getMoviments(Tauler t) {\
List\<Integer\> movimentPosibles = new ArrayList\<\>();\
for (int move = 0; move \< 8; move++) {\
if (t.movpossible(move)) {\
movimentPosibles.add(move);\
}\
}\
return movimentPosibles;\
}

Retornarà una llista amb tots els moviments disponibles dintre del
tauler actual.

max\_value
----------

*/\*\*\
\* **\@param** t Classe Tauler.\
\* **\@param** alpha Enter Alpha.\
\* **\@param** beta Enter Beta.\
\* **\@param** d Enter profunditat.\
\* **\@return** Enter.\
\*/\
*private int max\_value(Tauler t, int alpha, int beta, int d){\
if(d==0) {\
if(heuristic) return heuristic(t);\
else return heuristic2(t);\
}\
else{\
for(int moviment : getMoviments(t)) {\
Tauler nouTauler = new Tauler(t);\
nouTauler.afegeix(moviment,color);\
\
if(nouTauler.solucio(moviment, color)){ prof=d; return
Integer.*MAX\_VALUE*; }\
alpha = Math.*max*(alpha, min\_value(nouTauler, alpha, beta, d));\
if(beta \<= alpha) return beta;\
}\
return alpha;\
}\
}

La funció max\_value representaria l'elecció del moviment més beneficiós
per a nosaltres.

En primes lloc mirem si la profunditat es igual a 0. En aquest cas, en
funció de l'heurística escollida a l'hora de inicialitzar la classe,
s'encarregarà d'avaluar el tauler i retornar el valor calculat. En el
cas en que la profunditat sigui major a 0, recorrerem tots el moviments
possibles, a partir de cada moviment mirarem si es solució o ni, en el
cas de que sigui solució retornarem infinit i guardarem la profunditat
que hem trobar el moviment guanyador. En el cas de que no existeixix cap
moviment guanyador, cridarem a la funció min\_value amb el nou tauler i
esperarem a que ens retorni la nostra alpha.

Un cop tinguem l'alpha, intentarem fer la poda alpha-beta i si no es
possible continuarem totes les accions anteriors amb el següents
moviment disponible.

Min\_value
----------

private int min\_value(Tauler t, int alpha, int beta, int d) {\
for(int moviment : getMoviments(t)){\
Tauler nouTauler = new Tauler(t);\
nouTauler.afegeix(moviment,-color);\
if(nouTauler.solucio(moviment, -color)) return Integer.*MIN\_VALUE*;\
beta = Math.*min*(beta, max\_value(nouTauler, alpha, beta, d-1));\
if(beta \< bestAlpha) return Integer.*MIN\_VALUE*;\
if(beta \<= alpha) return alpha;\
}\
return beta;\
}

La funció min\_value representaria l'elecció del moviment més beneficiós
per el nostre enemic.

S'encarregarà de recórrer tots el moviments possibles i per cada
moviment mirarà si l'enemic ha guanyat, en aquest cas retornarà el valor
mínim, en el cas negatiu cridar a la funció max\_value i espera a
obtenir la nova beta, un cop tinguem la nova beta, aplicarà dos tipus de
poda, una, que la compararà amb la beta general, la quan vindrà definida
per la funció moviment i la segona poda es al clàssica alpha-beta.

Explicació de l'heurística:
===========================

Heuristic2
----------

private int heuristic2(Tauler board) {\
int puntuacio = 0;\
int mida = board.getMida();\
\
for (int i = 0; i \< mida; i++) {\
for (int j = 0; j \< mida; j++) {\
if (board.getColor(i, j) == color) {\
puntuacio += toni2(i, j, color, board);\
} else if (board.getColor(i, j) == -color) {\
puntuacio -= toni2(i, j, -color, board);\
}\
}\
}\
\
return puntuacio;\
}

L'heurístic 2 mirarà totes les posicions del tauler mirant si la fitxa
d'aquella posició es la meva o es la del nostre enemic, i a partir
d'aquí anirà sumant i restant les puntuacions.

Degut a que la funció toni2 es mol llarga nomes analitzarem el cas de
fer una columna.

if (i + 3 \< board.getMida()) {\
if (board.getColor(i + 1, j) != -color &&\
board.getColor(i + 2, j) != -color &&\
board.getColor(i + 3, j) != -color) {\
\
if (board.getColor(i + 1, j) == color) {\
if (board.getColor(i + 2, j) == color) {\
f = calculatepower(3.3);\
} else {\
f = calculatepower(2.3);\
}\
} else {\
f = calculatepower(1.3);\
}\
}\
}

La funció toni mirarà si a partir d'aquella fixa es poden connectar 4,
en al cas negatiu retornarà 0. En cas de que si es pugi connectar 4
fixes mirarà quantes fitxes ja te en línia amb la que esta analitzant
(cada una d'aquesta fixes contarà 1)i en els espais buits, mira si en un
sol moviment pot omplir aquell espai (en aquest cas valdrà 0.3). En el
cas de les columnes com que sempre es pot introduir una fixa a sobre
sempre valdrà 0.3 més

Heuristic
---------

L'heurístic final que hem fet es una versió recursiva de l\'anterior. En
aquest cas nomes comptem el número de fitxes del nostre color o llocs
lliures a partir de la posició inicial. Ho fem en quatre direccions:
horitzontal, vertical ascendent, diagonal dreta ascendent i diagonal
esquerra ascendent. A mes, deixem de banda l\'exponent 4 i fem servir el
mètode ponderat per prioritzar jugades que posin fitxes al centre del
taulell.

La premissa d\'aquesta nova estratègia es, a mes de intentar posar
fitxes en línia, que el nostre jugador controli el centre del taulell.
Si el centre es nostre, es mes fàcil connectar quatre fitxes en totes
direccions.

Aquest nou heurístic ha millorat el temps de cada torn i manté els
resultats assolits fins el moment.

Proba del Jugador
=================

AlumneTJ-D3-H1 VS. ProfeP6H1
----------------------------

![](./myMediaFolder/media/image1.png){width="3.0972222222222223in"
height="2.3881944444444443in"}

AlumneTJ-D6-H1 VS. ProfeP8H1
----------------------------

![](./myMediaFolder/media/image2.png){width="3.21875in"
height="2.078472222222222in"}

ProfeP6H1 VS. AlumneTJ-D3-H2 
-----------------------------

![](./myMediaFolder/media/image3.png){width="3.7006944444444443in"
height="2.33125in"}

ProfeP8H1 VS. AlumneTJ-D4-H2
----------------------------

![](./myMediaFolder/media/image4.png){width="3.2333333333333334in"
height="2.0548611111111112in"}
