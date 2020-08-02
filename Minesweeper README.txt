	Displaying the data:

the minefield on the GUI is built with a grid of JLabels with the number of mined neighbours on each label (this data is received from the getMinedNeighbours method from the Minefield class). Each JLabel has a JButton on it which is simply removed (visibility set to false) if the tile is not mined revealing the number of mined neighbours on the JLabel. If the tile stepped is mined, all the mined tiles are set to red colour and show a bomb image on them, and the smiley face in the section above the minefield is changed to a dead smiley face. Tiles can also be marked with a right click, in which case a flag is shown on top of the JButton. Clicking the reset button (smiley face) will create a new minefield of the same size and all the JLabels are hidden behind JButtons again.


	Editing the data:

(playing process described in the previous section) 
the game provides an option of changing the size of the minefield (changing difficulty or creating a custom sized minefield). If the difficulty is changed, everything is removed from the minefield panel, a minefield of the new size is created (Minefield class) and the minefiedl JPanel is populated with JLabels and JButtons on top according to the new Minefield object. The sizes of minefield of different difficulties is determined in the top menu's action listener.

	Optional extras:

1) Custom minefield:

The menu Difficulty on the menu bar provieds an option of creating a custom sized minefield. When it's called, a window pops up with textfields to be filled with the desired number of rows, columns and mines. The following processes are the same as for standard changing of difficulty described in the previous section.

2) Loosing a game:

When the player looses 3 times in a row, a message pops up, describing playing skills of the user. Loosing 5 times in a row will call another window with an image on it and an, clicking 'OK' button will close the game.

3) Winning a game:

When player wins a game, the smiley face on the button above the minefield will change to a 'cool' smiley face, and also music will start playing.

4) Extras menu:

This menu simply contains a few items that will call windows with game's description, the description of game's 'Easter Eggs'.

5) Timer:

Next to the button with smiley face above the minefield there's a timer that starts counting when a tile is clicked and stops when player looses or wins a game. When restarting the game, timer is set back to 0. Timer can count up to 999 seconds like in the original Minesweeper.

6) Win counter:

Located to the left of the smiley face and below the Flags countdown, this bit counts how many games were won in a row. Goes back to 0 if a game is lost.