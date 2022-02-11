=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D arrays. The board is stored in a 2D array, with each column of the board being a 1D array. This approach makes
  sense because the board is a grid and access at all indeces is important for things like checking diagonal wins, so 
  a linked data structure wouldn't make sense.

  2. Collections. I used a HashMap as a cache for the AIs (excluding the random AI, which has no cache). This is appropriate
  because I needed to store key/value pairs, where the key is the board state and the value is the AI's evaluation of the 
  strength of that board state. This cuts off entire branches of the recursive AI algorithm for many positions in the early 
  game.

  3. Recursion. The AI my game uses is built around a Monte Carlo tree search, where the AI evaluates the strength of one
  position using a minimax algorithm on the evaluations of the positions resulting from all possible moves. This is a good
  method for turn-based games because it allows the AI to evaluate all possible outcomes within a certain depth. Then, even
  a mediocre static evaluation function can be quite effective.
  For example, at 
  the start of the game, the evaluation is the maximum value of the evaluations of the board after each possible move. Then,
  it's red's turn, so the evaluation after the first move is the minimum of all the evaluation of the board after all 
  possible second moves. There is a set depth (7 in my implementation) after which the AI calls a static evaluation function
  as the base case for the recursion. This static evaluation checks if either player has won. If not, it favors the player
  with more pieces towards the center of the board. 

  4. Advanced concept: AI. At the end of each of the branches of the Monte Carlo search described above, there is a static
  evaluation function that assesses the strength of each player's position. I implemented several versions of this function,
  but ultimately settled on one that first checks if either player has won, and if not, assesses the strength of the board
  based on the number of pieces near the middle. This decision was based partly on my prior knowledge of connect four and
  partly on testing I did where I pitted various AIs against each other and human players.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Game.java: this class sets up the MVC framework, instantiating the other classes involved in the game and creating a JFrame.

GameBoard.java: this class controls the view for the game. It processes user input, sending it to the model, requests
moves from the AI at the appropriate points and renders the board as it updates.

ConnectFour.java: this class is the model for the game, storing and updating the game state when appropriate.

ConnectFourAI.java: this interface creates two methods that all the AIs will implement: findMove and update. findMove
takes a ConnectFour object and returns an int representing the column in which to play, and update writes the current cache
HashMap to a file.

RandomAI.java: this class implement ConnectFourAI and returns a random legal move. I used this as a baseline to test other AIs
against.

MonteCarloAI.java: this abstract class is the parent to the family of AIs described above, which calculate several moves ahead
and then use a static function. It implements all the functions except staticEval, which is abstract.

BoardEval.java: this class is used as the values in the AI's cache. It stores the depth and strength of the evaluation.

KeyWrapper: this private class is a helper for MonteCarloAI. I created this so that I could store a 2D array as the
key in a HashMap, overriding hashCode and equals to make this work. I explain this more below in the major stumbling blocks

BasicMonteCarlo.java: this class extends MonteCarloAI. Its staticEval function checks if someone has won.

MiddleMonteCarlo.java: this class extends MonteCarloAI. It is the AI I ultimately used. Its staticEval function checks if
someone has won, and if not, it favors the player with more pieces in/near the center column.

AITournament.java: this class is not part of the final implementation, but I used it to run tournaments where I pitted
the various AIs against each other to see which ones came out on top. This also helped me tune factors like depth and 
smoothing to find an effective AI that also ran in reasonable time.

Entry: this private class was a helper for AITournament. It stored the AI algorithm and name used for displaying results.



- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

The first major issue I had was with my mouseclick listener. I wanted to update the model, repaint, play the AI's move 
and then repaint again. Instead, the board would only repaint after the AI played its move, meaning that it would seem
unresponsive to the player's action. I discovered that this was because of the request queuing that Swing uses, so I ran the
call to the AI using a SwingWorker, which I read about from these sources:

The second issue I had was with storing 2D arrays as a key for HashMaps. Storing them directly didn't work because arrays
use referential equality, so instead, I used Arrays.deepHashCode and stored an integer. instead. This didn't work either.
The AI just seemed to be playing completely nonsensical moves. I discovered that this was the result of hash collisions.
Because there were so many possible board states, many of them were hashing to the same value. I still assumed this was a rare
occurrence, so I decided to just store the board state as part of the value and check that the board state was correct before
using the cached value. This worked, but the AI was rarely actually using the cache. I realized that the hash collisions were
much more frequent than I realized and that storing only one board state per hash was infeasible. This lead me to create the
KeyWrapper class, which stores only an int[][] board state, but overrides the hashCode and equals methods to use structural
equality.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

The functionality is well separated between the MVC framework and the various AIs. Virtually all the fields are private,
and only the necessary information is exposed via getters. The only thing that I would change in this regard is BoardEval.
I initially wrote it as a private class in the MonteCarloAI.java class, but then I wanted to test it in isolation, so I 
made it public. In retrospect, this was probably an error and I should have found a better way to test it.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  Painting/Swing Workers:
  https://www.oracle.com/java/technologies/painting.html
  https://docs.oracle.com/javase/7/docs/api/javax/swing/SwingWorker.html
  
  Serializing: https://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
  
  

  
