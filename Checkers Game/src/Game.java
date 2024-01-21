/* Name: Michelle Xu & Jessica Ying
 * Description: This programs runs a two-player checkers game using the mouse. The outcome can either be that one player wins or there is a tie.
 * Rules:
 * 1. Checkers is played by two opponents on opposite sides of the game board. 
 * 2. One player has the dark pieces; the other has the light pieces. 
 * 3. Players alternate turns. A player can not move the  opponent's pieces.
 * 4. A move consists of moving a piece an unoccupied square. 
 * 5. If the adjacent square contains an opponent's piece, and the square immediately beyond it is vacant, the piece may be captured by jumping over it.
 * 6. If there is another piece that is able to be captured after the first capture, the player can capture multiple pieces by jumping over each of those pieces. 
 * 7. A player cannot jump over his or her own piece.
 * 8. Pieces can only move in one direction. If a player’s piece reaches the other side of the board, it is now a king piece that can move in both directions.
 * 9. If the game time reaches 100 minutes, the game is over.
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.*;

public class Game extends JPanel implements Runnable, MouseListener {
	
 
	// --------------- CLASS VARIABLES --------------- // 
	
	// GENERAL VARIABLES
	public static Move logic = new Move (8, 8);
	public static int gameState = 0;
	public static int mouseX;
	public static int mouseY;

	// TIMER VARIABLES
	public static int numSecOnes = 0;
	public static int numSecTens = 0;
	public static int numMinOnes = 0;
	public static int numMinTens = 0;
	public static String timer = numMinTens + "" + numMinOnes + ":" + numSecTens + "" + numSecOnes;
	public static boolean timerRun = true;
	public static boolean timerOut = false;
	
	// SETTINGS VARIABLES
	public static int colourChosen = 0;
	public static int chosenPosX = 115;
	public static int chosenPosY = 438;
	
	// GAME SCREEN VARIABLES
	public static int colourNum = 1;
	public static String darkColour = "black";
	public static String lightColour = "white";
	public static boolean savedGameBefore = false;
	
	// END GAME VARIABLES
	public static String winner = "player1";
	
	// PICTURES
			
	// Main Screen
	public static BufferedImage titleScreen;
	public static BufferedImage gameScreen;
	public static BufferedImage rulesScreen;
	
	// Game Screen
	public static BufferedImage lightScreen;
	public static BufferedImage darkScreen;
 
	public static BufferedImage lightPiece;
	public static BufferedImage darkPiece;
	public static BufferedImage lightPieceKing;
	public static BufferedImage darkPieceKing;
	public static BufferedImage pieceRing;

	// Settings Screen
	public static BufferedImage settingsScreen;
	public static BufferedImage chosen;
	
	// Pause Screen
	public static BufferedImage greyScreen;
	public static BufferedImage pausePopup;
	
	// Continue Screen
	public static BufferedImage continueScreen;
	public static BufferedImage noGameSaved;
	
	// End Screen
	public static BufferedImage winScreen;
	public static BufferedImage endGamePopup;
 
	
	// Text File Streaming
	public static Scanner s = new Scanner (System.in);
	public static Scanner inputFile;
	public static PrintWriter outputFile;
	

	
	// JPANEL CONSTRUCTOR
	public Game() {
		
		// JPanel Settings
		setPreferredSize(new Dimension (800, 800));
		setBackground(new Color(255, 255, 255));
		
		// Add MouseListener to JPanel
		addMouseListener(this);
		
		try {
			titleScreen = ImageIO.read(new File("titleScreen.png"));
			darkScreen = ImageIO.read(new File ("colour" + colourNum + "_player1.png"));
			lightScreen = ImageIO.read(new File("colour" + colourNum + "_player2.png"));
			
			rulesScreen = ImageIO.read(new File("rulesScreen.png"));

			darkPiece = ImageIO.read(new File (darkColour + "_piece.png"));
			lightPiece = ImageIO.read(new File (lightColour + "_piece.png"));
			darkPieceKing = ImageIO.read(new File (darkColour + "_king_piece.png"));
			lightPieceKing = ImageIO.read(new File (lightColour + "_king_piece.png"));
			
			settingsScreen = ImageIO.read(new File ("settingsScreen.png"));
			chosen = ImageIO.read(new File ("chosen.png"));
			
			greyScreen = ImageIO.read(new File ("pausedScreenGrey.png"));
			pieceRing = ImageIO.read(new File ("pieceRing.png"));
			continueScreen = ImageIO.read(new File ("continueScreen.png"));
			noGameSaved = ImageIO.read(new File("noGameSaved.png"));
			
			pausePopup = ImageIO.read(new File ("pausePopup.png"));
			endGamePopup = ImageIO.read(new File("endGamePopup.png"));										 
		}
		
		catch (Exception e){
		}
		
		// Create a Timer (Thread) for the program
		Thread thread = new Thread(this);
		thread.start();	
  
	}
	

	public static void main (String[] args) throws IOException {
													   
		inputFile = new Scanner (new File ("savedGame.txt"));
		
		// Create a frame and the name of the program.
		// Create a panel to put inside the frame.
		JFrame frame = new JFrame("Checkers");
		Game panel = new Game();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
  
	}
		
	public void paintComponent (Graphics g) {
		
		// GAME STATE 0: MAIN SCREEN
		if (gameState == 0) {
			super.paintComponent(g);
			g.drawImage(titleScreen, 0, 0, null);
		}
		
		// GAME STATE 1: SETTINGS SCREEN
		else if (gameState == 1) {
			super.paintComponent(g);
			g.drawImage(settingsScreen, 0, 0, null);
			
						   
			if (colourChosen != 0) {
				g.drawImage(chosen, chosenPosX, chosenPosY, null);
				
				// Save colour choice
				if (chosenPosX == 114) {
					colourNum = 1;
					darkColour = "black";
					lightColour = "white";
	 
				}
				
				else if (chosenPosX == 330) {
	 
				   
					colourNum = 2;
					darkColour = "brown";
					lightColour = "lightbrown";
	 
				}
				
				else if (chosenPosX == 545) {
				   
					colourNum = 3;
					darkColour = "black";
					lightColour = "red";
	 
				}
				
				try {
					darkScreen = ImageIO.read(new File ("colour" + colourNum + "_player1.png"));
					lightScreen = ImageIO.read(new File("colour" + colourNum + "_player2.png"));
					
					darkPiece = ImageIO.read(new File (darkColour + "_piece.png"));
					lightPiece = ImageIO.read(new File (lightColour + "_piece.png"));
					
					darkPieceKing = ImageIO.read(new File (darkColour + "_king_piece.png"));
					lightPieceKing = ImageIO.read(new File (lightColour + "_king_piece.png"));
				}
				
				catch (Exception e){	
				}
			}
		}

		// GAME STATE 2: RULES SCREEN 			
		else if (gameState == 2) {
			super.paintComponent(g);
			g.drawImage(rulesScreen, 0, 0, null);
   
		}
		
		// GAME STATE 3: GAME SCREEN				
		else if (gameState == 3) {
			super.paintComponent(g);
			
			// Draw Timer 
			g.setColor(new Color(87, 48, 15));
			g.setFont(new Font("Arial", Font.PLAIN, 50));
			
							  
			if (logic.curPlayer == 0) {
				g.drawImage(darkScreen, 0, 0, null);
			}
			
			else {
				g.drawImage(lightScreen, 0, 0, null);
			}
			
			g.drawString(timer, 139, 755);
			
			
													   
			for (int row = 0; row < logic.board.length; row++) {
				for (int col = 0 ; col < logic.board[0].length; col++) {
					
					if (logic.board[row][col] == 1) {
						g.drawImage(darkPiece, 70 * col + 85, 70 * row + 85, null);	
					}
					
					else if (logic.board[row][col] == 2) {
						g.drawImage(lightPiece, 70 * col + 85, 70 * row + 85, null);
					}
					
					else if (logic.board[row][col] == 4) {
						g.drawImage(darkPieceKing, 70 * col + 85, 70 * row + 85, null);
					}
					
					else if (logic.board[row][col] == 5) {
						g.drawImage(lightPieceKing, 70 * col + 85, 70 * row + 85, null);
					}
				}
			}

			g.setColor(new Color(255, 255, 255));
	 
										   
			if (logic.scores[0] < 10) {
				g.setFont(new Font("Arial", Font.PLAIN, 30));
				g.drawString("" + logic.scores[0], 714, 401);
			}
			
			else {
				g.setFont(new Font("Arial", Font.PLAIN, 25));
				g.drawString("" + logic.scores[0], 708, 400);
			}								 									 
			
			g.setColor(new Color(0, 0, 0));
			
			if (logic.scores[1] < 10) {
				g.setFont(new Font("Arial", Font.PLAIN, 30));
				g.drawString("" + logic.scores[1], 714, 348);
												 
			}
			
			else {
				g.setFont(new Font("Arial", Font.PLAIN, 25));
				g.drawString("" + logic.scores[1], 708, 347);
			}
												 
   
							 
			if (logic.pieceSelected) {
				g.drawImage(pieceRing, 70 * logic.selectedCol[logic.curPlayer] + 83, 70 * logic.selectedRow[logic.curPlayer] + 83, null);
			}					 
   
		}
		
		// GAME STATE 4: PAUSE SCREEN
		else if (gameState == 4) {
			g.drawImage(greyScreen, 0, 0, null);
			g.drawImage(pausePopup, 0, 0, null);
   
		}
											  
		// GAME STATE 5: GAME END SCREEN												
		else if (gameState == 5) {
			g.drawImage(endGamePopup, 0, 0, null);
		}
		
		// GAME STATE 6: CONTINUE SCREEN
		else if (gameState == 6) {
			super.paintComponent(g);
			g.drawImage(continueScreen, 0, 0, null);
		}
		
		// GAME STATE 7: CLICK TO CONTINUE END SCREEN
		else if(gameState == 7) {
			super.paintComponent(g);
			
			try {
				winScreen = ImageIO.read(new File(winner + "Wins.png"));
			} 
			
			catch (IOException e) {
				e.printStackTrace();
			}
			
			g.drawImage(winScreen, 0, 0, null);
		}
		
		// GAME STATE 8: NO GAME SAVED SCREEN
		else if(gameState == 8) {
			super.paintComponent(g);
			g.drawImage(noGameSaved, 0, 0, null);	
										
   
		}
	}
	
	public void run() {
		while(true) {
			
			if (gameState != 4 && gameState != 5) {
				repaint();
				
				// Change the timer.
				if (gameState == 3) {
					if (timerRun) {
						try {
							Thread.sleep(1000);
						  
															  
						}
						
						catch(Exception e) {
							e.printStackTrace();
						}
						
						// Update the timer every 1000 milliseconds (1 second).
						Timer.timerUpdate();	
						
						if(numMinTens == 9 && numMinOnes == 9 && numSecTens == 5 && numSecOnes == 9) {
							timerRun = false;
							timerOut = true;
							paintComponent(this.getGraphics());
							gameState = 5;
							paintComponent(this.getGraphics());
						}				  
						
					}
				}
				
				// Pause the "No Game Saved" pop-up for 2 seconds.
				else if(gameState == 8) {
					try {
						Thread.sleep(1500);
					} 
					
					catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					gameState = 6;
					paintComponent(this.getGraphics());
	 
   
				}
			}	
		}
	}
		
	// This method updates the colour of the pieces chosen by the player in the 'Settings' screen.
	public static void colourChosenUpdate () {
		if (mouseX > 114 && mouseX < 253 && mouseY > 438 && mouseY < 585) {
			chosenPosX = 114;
		}
		
		else if (mouseX > 330 && mouseX < 478 && mouseY > 438 && mouseY < 585) {
			chosenPosX = 330;
		}
		
		else if (mouseX > 545 && mouseX < 692 && mouseY > 438 && mouseY < 585) {
			chosenPosX = 545;
		}
	}
	

	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

		// GAME STATE 0: MAIN SCREEN
		if (gameState == 0) {
			
			// If the user clicks the 'Start' button, change game state to 6 (new game or saved game)
			if (mouseX > 220 && mouseX < 580 && mouseY > 335 && mouseY < 445) {
				gameState = 6;
				paintComponent(this.getGraphics());
			}
			
			// If the user clicks the 'Rules' button, change game state to 2
			else if (mouseX > 220 && mouseX < 580 && mouseY > 570 && mouseY < 680) {
				gameState = 2;
				paintComponent(this.getGraphics());
			}
			
			// If the user clicks the 'Settings' button, change game state to 1
			else if (mouseX > 220 && mouseX < 580 && mouseY > 450 && mouseY < 560) {
				gameState = 1;
				paintComponent(this.getGraphics());
			}
   
		}
		
		
		// GAME STATE 1: SETTINGS SCREEN
		else if (gameState == 1) {
			
			if (((mouseX > 114 && mouseX < 253) || (mouseX > 330 && mouseX < 478) || (mouseX > 545 && mouseX < 692)) && (mouseY > 438 && mouseY < 585)) {
				colourChosen = 1;
				colourChosenUpdate();
				paintComponent(this.getGraphics());
			}
			
			else if (mouseX > 50 && mouseX < 116 && mouseY > 703 && mouseY < 770) {
				gameState = 0;
			}	
		}
		
		// GAME STATE 2: RULES SCREEN 
		else if (gameState == 2) {
			if (mouseX > 62 && mouseX < 118 && mouseY > 708 && mouseY < 774) {
				gameState = 0;
			}
		}
		
		// GAME STATE 3: GAME SCREEN
		else if (gameState == 3) {
			
			int rowIndex = (mouseY - 80)/70;
			int colIndex = (mouseX - 80)/70;
			
			if (rowIndex < 8 && colIndex < 8 && rowIndex >= 0 && colIndex >= 0) {
				logic.select(logic.curPlayer, rowIndex, colIndex);
				paintComponent(this.getGraphics());
				
				if(logic.scores[0] == 12 || !logic.anyMoves(1)) {
	 
					timerRun = false;
					gameState = 5;
					paintComponent(this.getGraphics());
				}
				
				else if(logic.scores[1] == 12 || !logic.anyMoves(0)) {
	 
					timerRun = false;
					winner = "player2";
					gameState = 5;
					paintComponent(this.getGraphics());
				}

			}
			
			else if (mouseX > 42 && mouseX < 111 && mouseY > 702 && mouseY < 771) {
				timerRun = false;
				gameState = 4;
				paintComponent(this.getGraphics());
			}
		}
		
		// GAME STATE 4: PAUSE SCREEN
		else if (gameState == 4) {
   
			if (mouseX > 252 && mouseX < 387 && mouseY > 475 && mouseY < 563) {
				inputFile.close();
				
				try {
					outputFile = new PrintWriter (new FileWriter ("savedGame.txt"));	

	 
				} 
				
				catch (IOException e1) {
					e1.printStackTrace();
				}
				
	
				for (int row = 0; row < logic.board.length; row++) {
					for (int col = 0 ; col < logic.board[0].length; col++) {
						outputFile.println(logic.board[row][col]);
	
	 
					}
				}
				
	
				outputFile.println(numSecOnes);
				outputFile.println(numSecTens);
				outputFile.println(numMinOnes);
				outputFile.println(numMinTens);
				outputFile.println(colourChosen);
				outputFile.println(chosenPosX);
				outputFile.println(colourNum);
				outputFile.println(darkColour);
				outputFile.println(lightColour);
				outputFile.println(logic.scores[0]);
				outputFile.println(logic.scores[1]);
				outputFile.println(logic.curPlayer);
				savedGameBefore = true;
				outputFile.close();
				gameState = 0;

			}
			
			else if (mouseX > 414 && mouseX < 543 && mouseY > 475 && mouseY < 563) {
				gameState = 0;
			}
					   
			else if (mouseX > 252 && mouseX < 544 && mouseY > 363 && mouseY < 451) {
				timerRun = true;
				gameState = 3;
			}
   
											   
		}
		
		// GAME STATE 5: GAME END SCREEN
		else if(gameState == 5) {
			if(timerOut) {
				winner = "no";
				gameState = 7;
			}

		}
		
		// GAME STATE 6: CONTINUE SCREEN
		else if (gameState == 6) {
			
			if (mouseX > 52 && mouseX < 119 && mouseY > 678 && mouseY < 754) {
				gameState = 0;
			}
			
			// NEW GAME
			else if (mouseX > 248 && mouseX < 540 && mouseY > 350 && mouseY < 438) {
			  
				for (int i = 0; i < 8; i++) {
	
					for (int j = 0; j < 8; j++){
						logic.board[i][j] = 3;
					}
				}
				
				for (int i = 0; i < 3; i++) {
					int startCol = 0;
					
					if (i % 2 == 0) {
						startCol = 1;
					}
					
					for (int j = startCol; j < 8; j+=2) {
						logic.board[i][j] = 2;
					}
				}
				
				for (int i = 3; i <= 4; i++) {
					int startCol = 0;
					
					if( i % 2 == 0) {
						startCol = 1;
					}
					
					for(int j = startCol; j < 8; j+=2) {
						logic.board[i][j] = 0;
					}
				}
				
				for(int i = 7; i > 4; i--) {
	
					int startCol = 0;
					
					if (i % 2 == 0) {
						startCol = 1;
					}
					
					for (int j = startCol; j < 8; j+=2) {
						logic.board[i][j] = 1;
					}
				}

				// Re-assigning variables 
				numSecOnes = 0;
				numSecTens = 0;
				numMinOnes = 0;
				numMinTens = 0;
				timer = numMinTens + "" + numMinOnes + ":" + numSecTens + "" + numSecOnes;
				colourChosen = 0;
				chosenPosX = 115;
				colourNum = 1;
				darkColour = "black";
				lightColour = "white";
				logic.scores[0] = 0;
				logic.scores[1] = 0;
				logic.curPlayer = 0;
				logic.pieceSelected = false;
				
				timerRun = true;
				gameState = 3;
			}
			
			else if (mouseX > 248 && mouseX < 540 && mouseY > 481 && mouseY < 569) {
		        try {
		            BufferedReader test = new BufferedReader(new FileReader("savedGame.txt"));
		            
		            // Check if text file is empty.
		            if (test.readLine() == null) {
		                gameState = 8;
		                paintComponent(this.getGraphics());
								
		            }
		            
		            else {
			  
						try {
							inputFile = new Scanner (new File ("savedGame.txt"));
						} 
						
						catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					
						
						for (int row = 0; row < logic.board.length; row++) {
							for (int col = 0 ; col < logic.board[0].length; col++) {
								logic.board[row][col] = Integer.parseInt(inputFile.nextLine());
															   
																	   
							}
						}
						
						// Save variables in the text file.
						numSecOnes = Integer.parseInt(inputFile.nextLine());
						numSecTens = Integer.parseInt(inputFile.nextLine());
						numMinOnes = Integer.parseInt(inputFile.nextLine());
						numMinTens = Integer.parseInt(inputFile.nextLine());
						timer = numMinTens + "" + numMinOnes + ":" + numSecTens + "" + numSecOnes;
						colourChosen = Integer.parseInt(inputFile.nextLine());
						chosenPosX = Integer.parseInt(inputFile.nextLine());
						colourNum = Integer.parseInt(inputFile.nextLine());
						darkColour = inputFile.nextLine();
						lightColour = inputFile.nextLine();
						logic.scores[0] = Integer.parseInt(inputFile.nextLine());
						logic.scores[1] = Integer.parseInt(inputFile.nextLine());
						logic.curPlayer = Integer.parseInt(inputFile.nextLine());
						
						timerRun = true;
						gameState = 3;
					  
		            }
		        } 
		        
		        catch (IOException e1) {
		            e1.printStackTrace();
		        }
			}	
		   
	
																	   
	
				  
	
   
		}
		
		// GAME STATE 7: CLICK TO CONTINUE END SCREEN
		else if(gameState == 7) {
			if(mouseX > 50 && mouseX < 123 && mouseY > 693 && mouseY < 766) {
						   
				gameState = 0;
				paintComponent(this.getGraphics());
			}
		}

	}

	// USELESS METHODS

	public void mouseExited(MouseEvent e) {	
	}
	public void mouseClicked(MouseEvent e) {	
	}
	public void mouseEntered(MouseEvent e) {	
	}
	public void mouseReleased(MouseEvent e) {	
	}

}
