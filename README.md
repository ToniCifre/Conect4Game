
# Connect 4 Game

## Choose Players

To select the players you must modify the main of the project which is in Juga2.java

```java
// Define 2 players
AlumneTJ al = new AlumneTJ(4, false);
Jugador p2 = new Manual();

// Good Player -> Jugador p2 = new Profe(8,false);
// Random player -> Jugador p2 = new Aleatori();

boolean autoMode = true;
final Play2 j = new Play2 (al, p2, autoMode);
```


Manual mode is for playing against the machine.

The other players need two parameters. the first is the depth of the search tree and the second is to select the heuristic


If the automode is false it will wait for each movement




You can create your own player by implementing the player and IAuto class.

## Game Test

AlumneTJ-D3-H1 VS. ProfeP6H1
----------------------------

![](./myMediaFolder/media/image1.png)

AlumneTJ-D6-H1 VS. ProfeP8H1
----------------------------

![](./myMediaFolder/media/image2.png)

ProfeP6H1 VS. AlumneTJ-D3-H2 
-----------------------------

![](./myMediaFolder/media/image3.png)

ProfeP8H1 VS. AlumneTJ-D4-H2
----------------------------

![](./myMediaFolder/media/image4.png)
