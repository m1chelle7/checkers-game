public class Move {
	
	// Class Variables
	public static int emptyCell = 0;
	public static int p1Piece = 1;
	public static int p2Piece = 2;
	public static int illegalCell = 3;
	public static int p1King = 4;
	public static int p2King = 5;
	public int curPlayer = 0;
	public int[] selectedRow = new int[2];
	public int[] selectedCol = new int[2];
	public static int[] pieces = {p1Piece, p2Piece};
	public static int[] kings = {p1King, p2King};
	public int[][] board = {{3, 2, 3, 2, 3, 2, 3, 2},
            				{2, 3, 2, 3, 2, 3, 2, 3},
            				{3, 2, 3, 2, 3, 2, 3, 2},
            				{0, 3, 0, 3, 0, 3, 0, 3},
            				{3, 0, 3, 0, 3, 0, 3, 0},
            				{1, 3, 1, 3, 1, 3, 1, 3},
            				{3, 1, 3, 1, 3, 1, 3, 1},
            				{1, 3, 1, 3, 1, 3, 1, 3}}; 
	
	public int[] scores = new int[2];
	public boolean pieceSelected = false;
	
	// Constructor
	public Move(int row, int col) {
	
		for(int i = 0; i < 2; i++) {
			selectedRow[i] = -1;
			selectedCol[i] = -1;
			scores[i] = 0;
		}
	}
	
	// This method controls the selection and subsequent movement of pieces on the board
	// It has parameters of: which player's turn it is, and the row and column that was clicked
	// If no piece is selected (selectedRow[player] and selectedCol[player] have values of -1), 
	// their next click on one of their pieces will select that piece.
	// If the player has already selected one of their pieces, their next click on a valid square will:
	// move their piece, capture an opponent's piece, or do nothing if neither are applicable
		// After each movement/capture, the board will be updated
	public void select(int player, int row, int col) {
		// Local Variables
		int piece = player+1;
		int king = player+4;

		
		// Method Body
		// If there is no piece selected, it will select a piece (as long as the user clicks on one of their pieces)
		if(selectedRow[player] == -1) {
			if(board[row][col] == piece || board[row][col] == king) {
				selectedRow[player] = row;
				selectedCol[player] = col;
				pieceSelected = true;
			}
		}
		else {
			// Overrides selection and chooses a different piece if you click on that one
			if(board[row][col] == piece || board[row][col] == king) {
				selectedRow[player] = row;
				selectedCol[player] = col;	
				pieceSelected = true;
			}
			else {
				// If you can move to that square, then: the board will be updated, the current player will 
				// switch, and the selection is cleared
				if((player == 0 && canP1Move(row, col)) || (player == 1 && canP2Move(row, col))) {
					board[row][col] = board[selectedRow[player]][selectedCol[player]];
					changeToKing(player, row, col);
					board[selectedRow[player]][selectedCol[player]] = 0;
					selectedRow[player] = -1;
					selectedCol[player] = -1;
					curPlayer = 1-curPlayer;
					pieceSelected = false;
				}
				// If you can capture an opponent's piece, the board will be updated to reflect that
				else if(player == 0) {
					updateP1Capture(row, col);
				}
				else if(player == 1) {
					updateP2Capture(row, col);
				}
				
			}
		}
	}
	
	
	
	// This method determines if player 1 is able to move 
	// The parameters row and col are for the square on the board that the player will move to
	public boolean canP1Move(int row, int col) {
		// Local Variables
		int p = 0;
		
		// Method Body
		// Checks if the row and column selected are valid to move to, and if there is an empty space there to move to
		if(row+1 == selectedRow[p]) {
			if(col-1 == selectedCol[p] || col+1 == selectedCol[p]) {
				if(board[row][col] == emptyCell) {
					return true;
				}
			}
		}
		// Checks the same thing, except including moving backward if the selected piece is a king piece
		if(row-1 == selectedRow[p] && board[selectedRow[p]][selectedCol[p]] == p1King) {
			if(col-1 == selectedCol[p] || col+1 == selectedCol[p]) {
				if(board[row][col] == emptyCell) {
					return true;
				}
			}
		}
		return false;
	}
	// This method determines if player 2 is able to move 
	// THe parameters row and col are for the square on the board that the player will move to
	public boolean canP2Move(int row, int col) {
		// Local Variables
		int p = 1;
		
		// Method Body
		// Checks if the row and column selected are valid to move to, and if there is an empty space there to move to
		if(row-1 == selectedRow[p]) {
			if(col-1 == selectedCol[p] || col+1 == selectedCol[p]) {
				if(board[row][col] == emptyCell) {
					return true;
				}
			}
		}
		// Checks the same thing, except including moving backward if the selected piece is a king piece
		if(row+1 == selectedRow[p] && board[selectedRow[p]][selectedCol[p]] == p2King) {
			if(col-1 == selectedCol[p] || col+1 == selectedCol[p]) {
				if(board[row][col] == emptyCell) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	// This method determines if player 1 can capture one of player 2's pieces
	// curRow and curCol are the indices on the inputBoard array of player 1's current piece
	// finRow and finCol are the indices on the inputBoard array that their piece will move to after capturing their opponent's piece,
	// where the user will be clicking to capture a piece
	public static boolean canP1Capture(int curRow, int curCol, int finRow, int finCol, int[][] inputBoard) {
		// Local Variables
		int p = 0;
		int opponent = 1 - p;
		
		// Method Body
		// Checks if the indices are actually on the board
		if(!validCell(curRow, curCol, inputBoard) || !validCell(finRow, finCol, inputBoard)) {
			return false;
		}
		
		// If the [finRow][finCol] cell is unoccupied by any pieces, finRow and finCol are the right distance away from curRow and curCol,
		// and the piece between finRow and curRow, and finCol and curCol is an opponent's piece, then they will be able to capture that piece
		if(inputBoard[finRow][finCol] == emptyCell) {
			if(finRow + 2 == curRow) {
				if(finCol + 2 == curCol) {
					if(isPlayerPiece(opponent, finRow+1, finCol+1, inputBoard)) {
						return true;
					}
				}
				if(finCol - 2 == curCol) {
					if(isPlayerPiece(opponent, finRow+1, finCol-1, inputBoard)) {
						return true;
					}
				}
				
			}
			// Allows player 1 to capture pieces backwards if their piece is a king piece
			if(inputBoard[curRow][curCol] == kings[p]) {
				if(finRow - 2 == curRow) {
					if(finCol + 2 == curCol) {
						if(isPlayerPiece(opponent, finRow-1, finCol+1, inputBoard)) {
							return true;
						}
					}
					if(finCol - 2 == curCol) {
						if(isPlayerPiece(opponent, finRow-1, finCol-1, inputBoard)){
							return true;
						}
					}
					
				}
			}
			
		}
		
		return false;
	}
	
	// This method determines if player 2 can capture one of player 1's pieces
	// curRow and curCol are the indices on the inputBoard array of player 2's current piece
	// finRow and finCol are the indices on the inputBoard array that their piece will move to after capturing their opponent's piece
	// finRow and finCol are also where the user will be clicking in order to capture a piece
	public static boolean canP2Capture(int curRow, int curCol, int finRow, int finCol, int[][] inputBoard) {
		// Local Variables
		int p = 1;
		int opponent = 1 - p;
		
		// Method Body
		// Checks if the indices are actually on the board
		if(!validCell(curRow, curCol, inputBoard) || !validCell(finRow, finCol, inputBoard)) {
			return false;
		}
		
		// If the [finRow][finCol] cell is unoccupied by any pieces, finRow and finCol are the right distance away from curRow and curCol,
		// and the piece between finRow and curRow and finCol and curCol is an opponent's piece, then they will be able to capture that piece
		if(inputBoard[finRow][finCol] == emptyCell) {
			if(finRow - 2 == curRow) {
				if(finCol + 2 == curCol) {
					if(isPlayerPiece(opponent, finRow-1, finCol+1, inputBoard)) {
						return true;
					}
				}
				if(finCol - 2 == curCol) {
					if(isPlayerPiece(opponent, finRow-1, finCol-1, inputBoard)) {
						return true;
					}
				}
				
			}
			// Allows player 2 to capture pieces backwards if their piece is a king piece
			if(inputBoard[curRow][curCol] == kings[p]) {
				if(finRow + 2 == curRow) {
					if(finCol + 2 == curCol) {
						if(isPlayerPiece(opponent, finRow+1, finCol+1, inputBoard)) {
							return true;
						}
					}
					if(finCol - 2 == curCol) {
						if(isPlayerPiece(opponent, finRow+1, finCol-1, inputBoard)) {
							return true;
						}
					}
					
				}
			}
			
		}
		
		return false;
	}
	
	// This method handles the captures and multicaptures made by player 1
	// It will return the final array that is updated after the capture/multicapture has been made
	// It will return null if a multicapture if there are no captures that can be made
	public static int[][] p1Multicapture(int curRow, int curCol, int finRow,int finCol, int[][] inputBoard)
	{
		int player = 0;
		int rowDiff = Math.abs(finRow-curRow);
		int colDiff = Math.abs(finCol-curCol);
		
		if(rowDiff %2 == 1 || colDiff %2 == 1)
		{
			return null;
		}
		
		if(rowDiff == 2 && colDiff == 2)
		{
			if(canP1Capture(curRow, curCol, finRow, finCol, inputBoard))
			{
				updateMove(player, curRow, curCol, finRow, finCol, inputBoard);
				return inputBoard;
			}
			return null;
		}
		if(canP1Capture(curRow, curCol, curRow-2, curCol-2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow-2, curCol-2, inputBoard);
			return p1Multicapture(curRow-2, curCol-2, finRow, finCol, createFakeBoard(inputBoard));
		}
		if(canP1Capture(curRow, curCol, curRow-2, curCol+2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow-2, curCol+2, inputBoard);
			return p1Multicapture(curRow-2, curCol+2, finRow, finCol, createFakeBoard(inputBoard));
		}
		if(canP1Capture(curRow, curCol, curRow+2, curCol-2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow+2, curCol-2, inputBoard);
			return p1Multicapture(curRow+2, curCol-2, finRow, finCol, createFakeBoard(inputBoard));
		}
		if(canP1Capture(curRow, curCol, curRow+2, curCol+2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow+2, curCol+2, inputBoard);
			return p1Multicapture(curRow+2, curCol+2, finRow, finCol, createFakeBoard(inputBoard));
		}
		
		return null;
	}
	
	// This method handles the captures and multicaptures made by player 2
	// It will return the final array that is updated after the capture/multicapture has been made
	// It will return null if a multicapture if there are no captures that can be made
	public static int[][] p2Multicapture(int curRow, int curCol, int finRow,int finCol, int[][] inputBoard)
	{
		int player = 1;
		int rowDiff = Math.abs(finRow-curRow);
		int colDiff = Math.abs(finCol-curCol);
		
		if(rowDiff %2 == 1 || colDiff %2 == 1)
		{
			return null;
		}
		
		if(rowDiff == 2 && colDiff == 2)
		{
			if(canP2Capture(curRow, curCol, finRow, finCol, inputBoard))
			{
				updateMove(player, curRow, curCol, finRow, finCol, inputBoard);
				return inputBoard;
			}
			return null;
		}
		if(canP2Capture(curRow, curCol, curRow-2, curCol-2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow-2, curCol-2, inputBoard);
			return p2Multicapture(curRow-2, curCol-2, finRow, finCol, createFakeBoard(inputBoard));
		}
		if(canP2Capture(curRow, curCol, curRow-2, curCol+2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow-2, curCol+2, inputBoard);
			return p2Multicapture(curRow-2, curCol+2, finRow, finCol, createFakeBoard(inputBoard));
		}
		if(canP2Capture(curRow, curCol, curRow+2, curCol-2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow+2, curCol-2, inputBoard);
			return p2Multicapture(curRow+2, curCol-2, finRow, finCol, createFakeBoard(inputBoard));
		}
		if(canP2Capture(curRow, curCol, curRow+2, curCol+2, inputBoard))
		{
			updateMove(player, curRow, curCol, curRow+2, curCol+2, inputBoard);
			return p2Multicapture(curRow+2, curCol+2, finRow, finCol, createFakeBoard(inputBoard));
		}
		
		return null;
	}
	
	// This method checks if a certain square on the board is int player's piece
	public static boolean isPlayerPiece(int player, int row, int col, int[][] inputBoard) {
		// Method Body
		// Checks to see if the row and col are outside the board
		if(!validCell(row, col, inputBoard)) {
			return false;
		}
		return inputBoard[row][col] == pieces[player] || inputBoard[row][col] == kings[player];
	}
	
	// This method checks if a square on the board is actually on the board
	public static boolean validCell(int row, int col, int[][] inputBoard) {
		return !(row < 0 || col < 0 || row >= inputBoard.length || col >= inputBoard[0].length);
	}
	
	
	// This method updates the inputBoard after a piece capture has been made
	// curRow and curCol are the indices of the piece
	// finRow and fnCol are the indices of where the piece will end up
	public static void updateMove(int player, int curRow, int curCol, int finRow, int finCol, int[][] inputBoard) {
		// Local Variables
		boolean changeToKing = isKingRow(player, finRow, inputBoard);
		
		// Method Body
		inputBoard[finRow][finCol] = changeToKing ? kings[player] : inputBoard[curRow][curCol];
		inputBoard[(finRow+curRow)/2][(finCol+curCol)/2] = emptyCell;
		inputBoard[curRow][curCol] = 0;
	}
	
	// This method copies index values from fakeBoard into board if the values are different
	// Board end up the same as fakeBoard
	public void updateBoard(int[][] fakeBoard) {
		// Local Variables
		int opponent = 1 - curPlayer;
		
		// Method Body
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				// Will only copy a value over to board if the values are different
				if(fakeBoard[i][j] != board[i][j]) {
					// If an index is empty but used to be occupied by an opponent's piece, the player's score will be
					// increased, as that means must have captured one of their opoponent's pieces
					if(fakeBoard[i][j] == emptyCell && (board[i][j] == pieces[opponent] || board[i][j] == kings[opponent])) {
						scores[curPlayer]++;
					}
					board[i][j] = fakeBoard[i][j];
				}
			}
		}
	}
	
	// This method will update the board after a successful piece capture by player 1
	public void updateP1Capture(int row, int col) {
		// Local Variables
		int p = 0;
		int[][] fakeBoard = p1Multicapture(selectedRow[p], selectedCol[p], row, col, createFakeBoard(board));
		
		// Method Body
		// If the fakeboard has values in it, then update the board, deselect the piece and switch the player's turn
		if(fakeBoard != null) {
			updateBoard(fakeBoard);
			selectedRow[p] = -1;
			selectedCol[p] = -1;
			curPlayer = 1-curPlayer;
			pieceSelected = false;
		}
	}
	
	// This method will update the board after a successful piece capture by player 2
	public void updateP2Capture(int row, int col) {
		// Local Variables
		int p = 1;
		int[][] fakeBoard = p2Multicapture(selectedRow[p], selectedCol[p], row, col, createFakeBoard(board));
		
		// Method Body
		// If the fakeboard has values in it, then update the board, deselect the piece and switch the player's turn
		if(fakeBoard != null) {
			updateBoard(fakeBoard);
			selectedRow[p] = -1;
			selectedCol[p] = -1;
			curPlayer = 1-curPlayer;
			pieceSelected = false;
		}
	}
	
	// This methods returns a copy of the inputBoard
	public static int[][] createFakeBoard(int[][] inputBoard) {
		// Local Variables
		int[][] fakeBoard = new int[inputBoard.length][inputBoard[0].length];
		
		// Method Body
		for(int i = 0; i < inputBoard.length; i++) {
			for(int j = 0; j < inputBoard[0].length; j++) {
				fakeBoard[i][j] = inputBoard[i][j];
			}
		}
		return fakeBoard;
	}
	
	// This method checks if a player's piece is a king piece
	public boolean isKing(int player) {
		// Method Body
		return board[selectedRow[player]][selectedCol[player]] == kings[player];
	}
	
	// This method determines if a certain row for a certain player will turn their piece into a king piece
	// Row 0 will turn player 1's piece into a king piece
	// Row 7 will turn player 2' piece into a king piece
	public static boolean isKingRow(int player, int row, int[][] inputBoard) {
		// Method Body
		if(player == 0 && row == 0) {
			return true;
		}
		else if(player == 1 && row == inputBoard[row].length-1) {
			return true;
		}
		return false;
	}
	
	// This method changes a player's piece from a regular piece to a king piece if it is supposed to change
	// Once either player 1 or player 2's piece hits the other side of the board, the piece will switch to a king piece
	public void changeToKing(int player, int row, int col) {
		// Method Body
		if(player == 0 && row == 0) {
			board[row][col] = p1King;
		}
		else if(player == 1 && row == board[row].length-1) {
			board[row][col] = p2King;
		}
	}
	
	// This method determines if a certain player is able to move any of their pieces or not
	public boolean anyMoves(int player)
	{
		// Method Body
		if(player == 0) {
			
			for(int r = 0; r < board.length; r++) {
				
				for(int c = 0; c < board[0].length; c++) {
					
					if(board[r][c] == p1Piece || board[r][c] == p1King) {
						
						if(r > 0) {
							
							if((c > 0 && board[r-1][c-1] == emptyCell) || (c < 7 && board[r-1][c+1] == emptyCell)) {
								return true;
							}
						}
						if(r > 1) {
							
							if(canP1Capture(r, c, r-2, c-2, board) || canP1Capture(r, c, r-2, c+2, board)) {
								return true;
							}
						}
					}
					if(board[r][c] == p1King) {
						
						if(r < 7) {
							
							if(c > 0 && board[r+1][c-1] == emptyCell || c < 7 && board[r+1][c+1] == emptyCell) {
								return true;
							}
						}
						if(r < 6) {
							
							if(canP1Capture(r, c, r+2, c-2, board) || canP1Capture(r, c, r+2, c+2, board)) {
								return true;
							}
						}
					}
				}
			}
		}
		else if(player == 1) {
			for(int r = 0; r < board.length; r++) {
				
				for(int c = 0; c < board[0].length; c++) {
					
					if(board[r][c] == p2Piece || board[r][c] == p2King) {
						
						if(r < 7) {
							
							if(c > 0 && board[r+1][c-1] == emptyCell || (c < 7 && board[r+1][c+1] == emptyCell)) {
								return true;
							}
						}
						if(r < 6) {
							
							if(canP2Capture(r, c, r+2, c-2, board) || canP2Capture(r, c, r+2, c+2, board)) {
								return true;
							}
						}
					}
					if(board[r][c] == p2King) {
						
						if(r > 0) {
							if((c > 0 && board[r-1][c-1] == emptyCell) || c < 7 && board[r-1][c+1] == emptyCell) {
								return true;
							}
						}
						if(r > 1) {
							
							if(canP1Capture(r, c, r-2, c-2, board) || canP1Capture(r, c, r-2, c+2, board)) {
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}