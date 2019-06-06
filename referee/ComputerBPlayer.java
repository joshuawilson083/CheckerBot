package referee;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.rmi.*;
import java.net.*;


/**
*/

public class ComputerBPlayer extends java.rmi.server.UnicastRemoteObject implements referee.Player {
	private String name;
	public Boolean isWhite;
	public Board best;
	public int[] inputBoard;

	public static final int NO_PIECE = 0;
	public static final int WHITE_PAWN = 1;
	public static final int WHITE_KING = 2;
	public static final int BLACK_PAWN = -1;
	public static final int BLACK_KING = -2;
	public static final int MAX_PLY = 6;
	//public static final Player PLAYER_MAX = this;


	public ComputerBPlayer(String Name) throws java.rmi.RemoteException {
		this.name = name;
	}

	public static int theLeft(int start){
		if(start < 6 || start == 13 || start == 21 || start == 29){
			return start;
		} else if((start-1)/4 == 1 || (start-1)/4 == 3 || (start-1)/4 == 5 || (start-1)/4 == 7){
			return start-5;
		} else {
			return start-4;
		}

	}

	public static int theRight(int start){
		if(start > 27 || start == 20 || start == 12 || start == 4){
			return start;
		} else if((start-1)/4 == 0 || (start-1)/4 == 2 || (start-1)/4 == 4 || (start-1)/4 == 6){
			return start+5;
		} else {
			return start+4;
		}

	}

	public static int theTop(int start){
		if(start < 5 || start == 12 || start == 20 || start == 28){
			return start;
		} else if((start-1)/4 == 1 || (start-1)/4 == 3 || (start-1)/4 == 5 || (start-1)/4 == 7){
			return start-4;
		} else {
			return start-3;
		}

	}

	public static int theBottom(int start){
		if(start > 28 || start == 5 || start == 13 || start == 21){
			return start;
		} else if((start-1)/4 == 0 || (start-1)/4 == 2 || (start-1)/4 == 4 || (start-1)/4 == 6){
			return start+4;
		} else {
			return start+3;
		}

	}

	public int[] getMove(int[] board, boolean amIWhite, int movesRemaining){
		inputBoard = board;
		isWhite = amIWhite;
		name = "Dummy";
		Board theInitialBoard = new Board(board, isWhite);
		System.out.println("Input Board:");
		System.out.println(boardToString(inputBoard));
		try{
			BoardValue(theInitialBoard, this, 0, -1000, 1000, best);
		}
		catch(RemoteException err){
			System.err.println("RemoteException");
		}
		int[] theMoves;
		int startPos = 0;
		int midPos = 0;
		int finalPos = 0;
		int[] bestMove = best.toArray();
		
		System.out.println("Best Board:");
		System.out.println(boardToString(bestMove));
		for(int i = 1; i<33; i++){
			if(isWhite && inputBoard[i] == WHITE_PAWN && bestMove[i] == NO_PIECE){
				startPos = i;
			} else if(isWhite && inputBoard[i] == WHITE_KING && bestMove[i] == NO_PIECE){
				startPos = i;
			} else if (!isWhite && inputBoard[i] == BLACK_PAWN && bestMove[i] == NO_PIECE){
				startPos = i;
			} else if (!isWhite && inputBoard[i] == BLACK_KING && bestMove[i] == NO_PIECE){
				startPos = i;
			}
			if(isWhite && inputBoard[i] == NO_PIECE && bestMove[i] == WHITE_PAWN){
				finalPos = i;
			} else if(isWhite && inputBoard[i] == NO_PIECE && bestMove[i] == WHITE_KING){
				finalPos = i;
			} else if (!isWhite && inputBoard[i] == NO_PIECE && bestMove[i] == BLACK_PAWN){
				finalPos = i;
			} else if(!isWhite && inputBoard[i] == NO_PIECE && bestMove[i] == BLACK_KING){
				finalPos = i;
			}
		}
		for(int i = 1; i<33; i++){
			if(isWhite && inputBoard[i] == BLACK_PAWN && bestMove[i] == NO_PIECE){
				if(theLeft(i) == finalPos || theLeft(i) == startPos){
					midPos = theRight(i);
				}  else if(theRight(i) == finalPos || theRight(i) == startPos){
					midPos = theLeft(i);
				} else if(theTop(i) == finalPos || theTop(i) == startPos){
					midPos = theBottom(i);
				} else if(theBottom(i) == finalPos || theBottom(i) == startPos){
					midPos = theTop(i);
				}
			} else if(isWhite && inputBoard[i] == BLACK_KING && bestMove[i] == NO_PIECE){
				if(theLeft(i) == finalPos || theLeft(i) == startPos){
					midPos = theRight(i);
				}  else if(theRight(i) == finalPos || theRight(i) == startPos){
					midPos = theLeft(i);
				} else if(theTop(i) == finalPos || theTop(i) == startPos){
					midPos = theBottom(i);
				} else if(theBottom(i) == finalPos || theBottom(i) == startPos){
					midPos = theTop(i);
				}
			} else if(!isWhite && inputBoard[i] == WHITE_PAWN && bestMove[i] == NO_PIECE){
				if(theLeft(i) == finalPos || theLeft(i) == startPos){
					midPos = theRight(i);
				}  else if(theRight(i) == finalPos || theRight(i) == startPos){
					midPos = theLeft(i);
				} else if(theTop(i) == finalPos || theTop(i) == startPos){
					midPos = theBottom(i);
				} else if(theBottom(i) == finalPos || theBottom(i) == startPos){
					midPos = theTop(i);
				}
			} else if(!isWhite && inputBoard[i] == WHITE_KING && bestMove[i] == NO_PIECE){
				if(theLeft(i) == finalPos || theLeft(i) == startPos){
					midPos = theRight(i);
				}  else if(theRight(i) == finalPos || theRight(i) == startPos){
					midPos = theLeft(i);
				} else if(theTop(i) == finalPos || theTop(i) == startPos){
					midPos = theBottom(i);
				} else if(theBottom(i) == finalPos || theBottom(i) == startPos){
					midPos = theTop(i);
				}
			}
		}
		if(midPos == 0 || (midPos == startPos || midPos == finalPos)){
			theMoves = new int[2];
			theMoves[0] = startPos;
			theMoves[1] = finalPos;
		} else {
			theMoves = new int[3];
			theMoves[0] = startPos;
			theMoves[1] = midPos;
			theMoves[2] = finalPos;
		}
		System.out.println("midPos: " + midPos);
		return theMoves;
	}

	public int BoardEval(Board b){
		int[] theBoard = b.toArray();
		int evaluation = 0;
		int numberOfBlack = 0;
		int numberOfWhite = 0;
		for (int i = 1; i < 33; i++){
			/*
			if(theBoard[i] == WHITE_PAWN){
				if(i>4 && i<9){
					evaluation += (theBoard[i] + 18);
				} else if(i>8 && i<13){
					evaluation += (theBoard[i] + 16);
				} else if(i>12 && i<17){
					evaluation += (theBoard[i] + 14);
				} else if(i>16 && i<21){
					evaluation += (theBoard[i] + 12);
				} else {
					evaluation += (theBoard[i] +10);
				}
				numberOfWhite += 1;
			} else if(theBoard[i] == BLACK_PAWN){
				if(i>24 && i<29){
					evaluation += (theBoard[i] - 18);
				} else if(i>20 && i<25){
					evaluation += (theBoard[i] - 16);
				} else if(i>16 && i<21){
					evaluation += (theBoard[i] - 14);
				} else if(i>12 && i<17){
					evaluation += (theBoard[i] - 12);
				} else {
					evaluation += (theBoard[i] - 10);
				}
				numberOfBlack += 1;
			} else if(theBoard[i] == WHITE_KING){
				evaluation += 35;
				numberOfWhite += 1;
			} else if(theBoard[i] == BLACK_KING){
				evaluation -= 35;
				numberOfBlack += 1;
			}
			*/
		evaluation += theBoard[i];
		}
		if(numberOfBlack == 0){
			return 999;
		}
		if(numberOfWhite == 0){
			return -999;
		}
		return evaluation;
	}

	public List<Board> ReachableBoards(Board b, Player p){
		List<Board> possibleBoards = new ArrayList<Board>();
		List<Board> doubleCapBoards = new ArrayList<Board>();
		int[] bCheck = b.toArray();
		Boolean pIsWhite;
		Boolean hasCaptureMove = false;
		Boolean hasDoubleCap = false;
		if (p == this){
			pIsWhite = isWhite;
		} else {
			pIsWhite = !isWhite;
		}
		for (int i = 1; i<33; i++){
			if (pIsWhite && bCheck[i] == WHITE_PAWN){
				//left white pawn moves
				if((bCheck[theLeft(i)] == BLACK_PAWN || bCheck[theLeft(i)] == BLACK_KING) && bCheck[theLeft(theLeft(i))] == NO_PIECE){
					int newLoc = theLeft(theLeft(i));
					//left/left double cap
					if((bCheck[theLeft(newLoc)] == BLACK_PAWN || bCheck[theLeft(newLoc)] == BLACK_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] leftDblCapBoard = b.toArray();
						leftDblCapBoard[i] = NO_PIECE;
						leftDblCapBoard[theLeft(i)] = NO_PIECE;
						leftDblCapBoard[newLoc] = NO_PIECE;
						leftDblCapBoard[theLeft(newLoc)] = NO_PIECE;
						leftDblCapBoard[theLeft(theLeft(newLoc))] = WHITE_PAWN;
						Board leftDblCapMade = new Board(leftDblCapBoard, pIsWhite);
						doubleCapBoards.add(leftDblCapMade);
						hasDoubleCap = true;
					} 
					//left/top double cap
					if((bCheck[theTop(newLoc)] == BLACK_PAWN || bCheck[theTop(newLoc)] == BLACK_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] leftTopCapBoard = b.toArray();
						leftTopCapBoard[i] = NO_PIECE;
						leftTopCapBoard[theLeft(i)] = NO_PIECE;
						leftTopCapBoard[newLoc] = NO_PIECE;
						leftTopCapBoard[theTop(newLoc)] = NO_PIECE;
						leftTopCapBoard[theTop(theTop(newLoc))] = WHITE_PAWN;
						Board leftTopCapMade = new Board(leftTopCapBoard, pIsWhite);
						doubleCapBoards.add(leftTopCapMade);
						hasDoubleCap = true;
					} 
					//left single cap
					if(!hasDoubleCap){
						int[] leftCaptureBoard = b.toArray();
						leftCaptureBoard[theLeft(theLeft(i))] = WHITE_PAWN;
						leftCaptureBoard[i] = NO_PIECE;
						leftCaptureBoard[theLeft(i)] = NO_PIECE;
						Board leftCaptureMade = new Board(leftCaptureBoard, pIsWhite);
						possibleBoards.add(leftCaptureMade);
						hasCaptureMove = true;
					}
					
				} 
				//top white pawn moves
				if ((bCheck[theTop(i)] == BLACK_PAWN || bCheck[theTop(i)] == BLACK_KING) && bCheck[theTop(theTop(i))] == NO_PIECE){
					int newLoc = theTop(theTop(i));

					//top/left double cap
					if((bCheck[theLeft(newLoc)] == BLACK_PAWN || bCheck[theLeft(newLoc)] == BLACK_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] topLeftCapBoard = b.toArray();
						topLeftCapBoard[i] = NO_PIECE;
						topLeftCapBoard[theTop(i)] = NO_PIECE;
						topLeftCapBoard[newLoc] = NO_PIECE;
						topLeftCapBoard[theLeft(newLoc)] = NO_PIECE;
						topLeftCapBoard[theLeft(theLeft(newLoc))] = WHITE_PAWN;
						Board topLeftCapMade = new Board(topLeftCapBoard, pIsWhite);
						doubleCapBoards.add(topLeftCapMade);
						hasDoubleCap = true;
					} 
					//top/top double cap
					if((bCheck[theTop(newLoc)] == BLACK_PAWN || bCheck[theTop(newLoc)] == BLACK_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] topDblCapBoard = b.toArray();
						topDblCapBoard[i] = NO_PIECE;
						topDblCapBoard[theTop(i)] = NO_PIECE;
						topDblCapBoard[newLoc] = NO_PIECE;
						topDblCapBoard[theTop(newLoc)] = NO_PIECE;
						topDblCapBoard[theTop(theTop(newLoc))] = WHITE_PAWN;
						Board topDblCapMade = new Board(topDblCapBoard, pIsWhite);
						doubleCapBoards.add(topDblCapMade);
						hasDoubleCap = true;
					} 
					//top single cap
					if(!hasDoubleCap){
						int[] topCaptureBoard = b.toArray();
						topCaptureBoard[theTop(theTop(i))] = WHITE_PAWN;
						topCaptureBoard[i] = NO_PIECE;
						topCaptureBoard[theTop(i)] = NO_PIECE;
						Board topCaptureMade = new Board(topCaptureBoard, pIsWhite);
						possibleBoards.add(topCaptureMade);
						hasCaptureMove = true;
					}
				}
			} else if (pIsWhite && bCheck[i] == WHITE_KING){
				//left White King Moves
				if((bCheck[theLeft(i)] == BLACK_PAWN || bCheck[theLeft(i)] == BLACK_KING) && bCheck[theLeft(theLeft(i))] == NO_PIECE){
					int newLoc = theLeft(theLeft(i));
					//left/left double capture
					if((bCheck[theLeft(newLoc)] == BLACK_PAWN || bCheck[theLeft(newLoc)] == BLACK_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] leftDblCapBoard = b.toArray();
						leftDblCapBoard[i] = NO_PIECE;
						leftDblCapBoard[theLeft(i)] = NO_PIECE;
						leftDblCapBoard[newLoc] = NO_PIECE;
						leftDblCapBoard[theLeft(newLoc)] = NO_PIECE;
						leftDblCapBoard[theLeft(theLeft(newLoc))] = WHITE_KING;
						Board leftDblCapMade = new Board(leftDblCapBoard, pIsWhite);
						doubleCapBoards.add(leftDblCapMade);
						hasDoubleCap = true;
					} 
					//left/top double capture
					if((bCheck[theTop(newLoc)] == BLACK_PAWN || bCheck[theTop(newLoc)] == BLACK_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] leftTopCapBoard = b.toArray();
						leftTopCapBoard[i] = NO_PIECE;
						leftTopCapBoard[theLeft(i)] = NO_PIECE;
						leftTopCapBoard[newLoc] = NO_PIECE;
						leftTopCapBoard[theTop(newLoc)] = NO_PIECE;
						leftTopCapBoard[theTop(theTop(newLoc))] = WHITE_KING;
						Board leftTopCapMade = new Board(leftTopCapBoard, pIsWhite);
						doubleCapBoards.add(leftTopCapMade);
						hasDoubleCap = true;
					}
					//left/bottom double capture
					if((bCheck[theBottom(newLoc)] == BLACK_PAWN || bCheck[theBottom(newLoc)] == BLACK_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE){
						int[] leftBottomCapBoard = b.toArray();
						leftBottomCapBoard[i] = NO_PIECE;
						leftBottomCapBoard[theLeft(i)] = NO_PIECE;
						leftBottomCapBoard[newLoc] = NO_PIECE;
						leftBottomCapBoard[theBottom(newLoc)] = NO_PIECE;
						leftBottomCapBoard[theBottom(theBottom(newLoc))] = WHITE_KING;
						Board leftBottomCapMade = new Board(leftBottomCapBoard, pIsWhite);
						doubleCapBoards.add(leftBottomCapMade);
						hasDoubleCap = true;
					}
					//left single cap
					if(!hasDoubleCap){
						int[] leftCaptureBoard = b.toArray();
						leftCaptureBoard[theLeft(theLeft(i))] = WHITE_KING;
						leftCaptureBoard[i] = NO_PIECE;
						leftCaptureBoard[theLeft(i)] = NO_PIECE;
						Board leftCaptureMade = new Board(leftCaptureBoard, pIsWhite);
						possibleBoards.add(leftCaptureMade);
						hasCaptureMove = true;
					}
				}
				//king top moves
				if ((bCheck[theTop(i)] == BLACK_PAWN || bCheck[theTop(i)] == BLACK_KING) && bCheck[theTop(theTop(i))] == NO_PIECE){
					int newLoc = theTop(theTop(i));
					//top/left double cap
					if((bCheck[theLeft(newLoc)] == BLACK_PAWN || bCheck[theLeft(newLoc)] == BLACK_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] topLeftCapBoard = b.toArray();
						topLeftCapBoard[i] = NO_PIECE;
						topLeftCapBoard[theTop(i)] = NO_PIECE;
						topLeftCapBoard[newLoc] = NO_PIECE;
						topLeftCapBoard[theLeft(newLoc)] = NO_PIECE;
						topLeftCapBoard[theLeft(theLeft(newLoc))] = WHITE_KING;
						Board topLeftCapMade = new Board(topLeftCapBoard, pIsWhite);
						doubleCapBoards.add(topLeftCapMade);
						hasDoubleCap = true;
					} 
					//top/top double cap
					if((bCheck[theTop(newLoc)] == BLACK_PAWN || bCheck[theTop(newLoc)] == BLACK_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] topDblCapBoard = b.toArray();
						topDblCapBoard[i] = NO_PIECE;
						topDblCapBoard[theTop(i)] = NO_PIECE;
						topDblCapBoard[newLoc] = NO_PIECE;
						topDblCapBoard[theTop(newLoc)] = NO_PIECE;
						topDblCapBoard[theTop(theTop(newLoc))] = WHITE_KING;
						Board topDblCapMade = new Board(topDblCapBoard, pIsWhite);
						doubleCapBoards.add(topDblCapMade);
						hasDoubleCap = true;
					} 
					//top/right double cap
					if((bCheck[theRight(newLoc)] == BLACK_PAWN || bCheck[theRight(newLoc)] == BLACK_KING) && bCheck[theRight(theRight(newLoc))] == NO_PIECE){
						int[] topRightCapBoard = b.toArray();
						topRightCapBoard[i] = NO_PIECE;
						topRightCapBoard[theTop(i)] = NO_PIECE;
						topRightCapBoard[newLoc] = NO_PIECE;
						topRightCapBoard[theRight(newLoc)] = NO_PIECE;
						topRightCapBoard[theRight(theRight(newLoc))] = WHITE_KING;
						Board topRightCapMade = new Board(topRightCapBoard, pIsWhite);
						doubleCapBoards.add(topRightCapMade);
						hasDoubleCap = true;
					}
					//top single cap
					if(!hasDoubleCap){
						int[] topCaptureBoard = b.toArray();
						topCaptureBoard[theTop(theTop(i))] = WHITE_KING;
						topCaptureBoard[i] = NO_PIECE;
						topCaptureBoard[theTop(i)] = NO_PIECE;
						Board topCaptureMade = new Board(topCaptureBoard, pIsWhite);
						possibleBoards.add(topCaptureMade);
						hasCaptureMove = true;
					}
				}
				//right White King Moves
				if((bCheck[theRight(i)] == BLACK_PAWN || bCheck[theRight(i)] == BLACK_KING) && bCheck[theRight(theRight(i))] == NO_PIECE){
					int newLoc = theRight(theRight(i));
					//right/right double capture
					if((bCheck[theRight(newLoc)] == BLACK_PAWN || bCheck[theRight(newLoc)] == BLACK_KING) 
						&& bCheck[theRight(theRight(newLoc))] == NO_PIECE){
						int[] rightDblCapBoard = b.toArray();
						rightDblCapBoard[i] = NO_PIECE;
						rightDblCapBoard[theRight(i)] = NO_PIECE;
						rightDblCapBoard[newLoc] = NO_PIECE;
						rightDblCapBoard[theRight(newLoc)] = NO_PIECE;
						rightDblCapBoard[theRight(theRight(newLoc))] = WHITE_KING;
						Board rightDblCapMade = new Board(rightDblCapBoard, pIsWhite);
						doubleCapBoards.add(rightDblCapMade);
						hasDoubleCap = true;
					} 
					//right/top double capture
					if((bCheck[theTop(newLoc)] == BLACK_PAWN || bCheck[theTop(newLoc)] == BLACK_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] rightTopCapBoard = b.toArray();
						rightTopCapBoard[i] = NO_PIECE;
						rightTopCapBoard[theRight(i)] = NO_PIECE;
						rightTopCapBoard[newLoc] = NO_PIECE;
						rightTopCapBoard[theTop(newLoc)] = NO_PIECE;
						rightTopCapBoard[theTop(theTop(newLoc))] = WHITE_KING;
						Board rightTopCapMade = new Board(rightTopCapBoard, pIsWhite);
						doubleCapBoards.add(rightTopCapMade);
						hasDoubleCap = true;
					}
					//right/bot double capture
					if((bCheck[theBottom(newLoc)] == BLACK_PAWN || bCheck[theBottom(newLoc)] == BLACK_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE){
						int[] rightBottomCapBoard = b.toArray();
						rightBottomCapBoard[i] = NO_PIECE;
						rightBottomCapBoard[theRight(i)] = NO_PIECE;
						rightBottomCapBoard[newLoc] = NO_PIECE;
						rightBottomCapBoard[theBottom(newLoc)] = NO_PIECE;
						rightBottomCapBoard[theBottom(theBottom(newLoc))] = WHITE_KING;
						Board rightBottomCapMade = new Board(rightBottomCapBoard, pIsWhite);
						doubleCapBoards.add(rightBottomCapMade);
						hasDoubleCap = true;
					}
					//right single cap
					if(!hasDoubleCap){
						int[] rightCaptureBoard = b.toArray();
						rightCaptureBoard[theRight(theRight(i))] = WHITE_KING;
						rightCaptureBoard[i] = NO_PIECE;
						rightCaptureBoard[theRight(i)] = NO_PIECE;
						Board rightCaptureMade = new Board(rightCaptureBoard, pIsWhite);
						possibleBoards.add(rightCaptureMade);
						hasCaptureMove = true;
					}
				}
				//bottom White King Moves
				if((bCheck[theBottom(i)] == BLACK_PAWN || bCheck[theBottom(i)] == BLACK_KING) && bCheck[theBottom(theBottom(i))] == NO_PIECE){
					int newLoc = theBottom(theBottom(i));
					//bottom/left double capture
					if((bCheck[theLeft(newLoc)] == BLACK_PAWN || bCheck[theLeft(newLoc)] == BLACK_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] bottomLeftCapBoard = b.toArray();
						bottomLeftCapBoard[i] = NO_PIECE;
						bottomLeftCapBoard[theBottom(i)] = NO_PIECE;
						bottomLeftCapBoard[newLoc] = NO_PIECE;
						bottomLeftCapBoard[theLeft(newLoc)] = NO_PIECE;
						bottomLeftCapBoard[theLeft(theLeft(newLoc))] = WHITE_KING;
						Board bottomLeftCapMade = new Board(bottomLeftCapBoard, pIsWhite);
						doubleCapBoards.add(bottomLeftCapMade);
						hasDoubleCap = true;
					} 
					//bottom/bottom double capture
					if((bCheck[theBottom(newLoc)] == BLACK_PAWN || bCheck[theBottom(newLoc)] == BLACK_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE){
						int[] bottomDblCapBoard = b.toArray();
						bottomDblCapBoard[i] = NO_PIECE;
						bottomDblCapBoard[theBottom(i)] = NO_PIECE;
						bottomDblCapBoard[newLoc] = NO_PIECE;
						bottomDblCapBoard[theBottom(newLoc)] = NO_PIECE;
						bottomDblCapBoard[theBottom(theBottom(newLoc))] = WHITE_KING;
						Board bottomDblCapMade = new Board(bottomDblCapBoard, pIsWhite);
						doubleCapBoards.add(bottomDblCapMade);
						hasDoubleCap = true;
					}
					//bottom/right double cap
					if((bCheck[theRight(newLoc)] == BLACK_PAWN || bCheck[theRight(newLoc)] == BLACK_KING) && bCheck[theRight(theRight(newLoc))] == NO_PIECE){
						int[] bottomRightCapBoard = b.toArray();
						bottomRightCapBoard[i] = NO_PIECE;
						bottomRightCapBoard[theBottom(i)] = NO_PIECE;
						bottomRightCapBoard[newLoc] = NO_PIECE;
						bottomRightCapBoard[theRight(newLoc)] = NO_PIECE;
						bottomRightCapBoard[theRight(theRight(newLoc))] = WHITE_KING;
						Board bottomRightCapMade = new Board(bottomRightCapBoard, pIsWhite);
						doubleCapBoards.add(bottomRightCapMade);
						hasDoubleCap = true;
					}
					//bottom single cap
					if(!hasDoubleCap){
						int[] bottomCaptureBoard = b.toArray();
						bottomCaptureBoard[theBottom(theBottom(i))] = WHITE_KING;
						bottomCaptureBoard[i] = NO_PIECE;
						bottomCaptureBoard[theBottom(i)] = NO_PIECE;
						Board bottomCaptureMade = new Board(bottomCaptureBoard, pIsWhite);
						possibleBoards.add(bottomCaptureMade);
						hasCaptureMove = true;
					}
				}

			} else if (!pIsWhite && bCheck[i] == BLACK_PAWN){
					//right black pawn moves
					if(((bCheck[theRight(i)] == WHITE_PAWN || bCheck[theRight(i)] == WHITE_KING) && bCheck[theRight(theRight(i))] == NO_PIECE)){
						int newLoc = theRight(theRight(i));
						//right/right double capture
						if(((bCheck[theRight(newLoc)] == WHITE_PAWN || bCheck[theRight(newLoc)] == WHITE_KING) && bCheck[theRight(theRight(newLoc))] == NO_PIECE)){
							int[] rightDblCapBoard = b.toArray();
							rightDblCapBoard[i] = NO_PIECE;
							rightDblCapBoard[theRight(i)] = NO_PIECE;
							rightDblCapBoard[newLoc] = NO_PIECE;
							rightDblCapBoard[theRight(newLoc)] = NO_PIECE;
							rightDblCapBoard[theRight(theRight(newLoc))] = BLACK_PAWN;
							Board rightDblCapMade = new Board(rightDblCapBoard, pIsWhite);
							doubleCapBoards.add(rightDblCapMade);
							hasDoubleCap = true;
						}
						//right/bottom double capture
						if(((bCheck[theBottom(newLoc)] == WHITE_PAWN || bCheck[theBottom(newLoc)] == WHITE_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE)){
							int[] rightBottomCapBoard = b.toArray();
							rightBottomCapBoard[i] = NO_PIECE;
							rightBottomCapBoard[theRight(i)] = NO_PIECE;
							rightBottomCapBoard[newLoc] = NO_PIECE;
							rightBottomCapBoard[theBottom(newLoc)] = NO_PIECE;
							rightBottomCapBoard[theBottom(theBottom(newLoc))] = BLACK_PAWN;
							Board rightBottomCapMade = new Board(rightBottomCapBoard, pIsWhite);
							doubleCapBoards.add(rightBottomCapMade);
							hasDoubleCap = true;
						}
						//right single capture
						if(!hasDoubleCap){
							int[] rightCaptureBoard = b.toArray();
							rightCaptureBoard[theRight(theRight(i))] = BLACK_PAWN;
							rightCaptureBoard[i] = NO_PIECE;
							rightCaptureBoard[theRight(i)] = NO_PIECE;
							Board rightCaptureMade = new Board(rightCaptureBoard, pIsWhite);
							possibleBoards.add(rightCaptureMade);
							hasCaptureMove = true;
						}
					}
					//bottom black pawn moves
					if(((bCheck[theBottom(i)] == WHITE_PAWN || bCheck[theBottom(i)] == WHITE_KING) && bCheck[theBottom(theBottom(i))] == NO_PIECE)){
						int newLoc = theBottom(theBottom(i));
						//bottom/bottom double capture
						if(((bCheck[theBottom(newLoc)] == WHITE_PAWN || bCheck[theBottom(newLoc)] == WHITE_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE)){
							int[] bottomDblCapBoard = b.toArray();
							bottomDblCapBoard[i] = NO_PIECE;
							bottomDblCapBoard[theBottom(i)] = NO_PIECE;
							bottomDblCapBoard[newLoc] = NO_PIECE;
							bottomDblCapBoard[theBottom(newLoc)] = NO_PIECE;
							bottomDblCapBoard[theBottom(theBottom(newLoc))] = BLACK_PAWN;
							Board bottomDblCapMade = new Board(bottomDblCapBoard, pIsWhite);
							doubleCapBoards.add(bottomDblCapMade);
							hasDoubleCap = true;
						}
						//bottom/right double capture
						if(((bCheck[theRight(newLoc)] == WHITE_PAWN || bCheck[theRight(newLoc)] == WHITE_KING) && bCheck[theRight(theRight(newLoc))] == NO_PIECE)){
							int[] bottomRightCapBoard = b.toArray();
							bottomRightCapBoard[i] = NO_PIECE;
							bottomRightCapBoard[theBottom(i)] = NO_PIECE;
							bottomRightCapBoard[newLoc] = NO_PIECE;
							bottomRightCapBoard[theRight(newLoc)] = NO_PIECE;
							bottomRightCapBoard[theRight(theRight(newLoc))] = BLACK_PAWN;
							Board bottomRightCapMade = new Board(bottomRightCapBoard, pIsWhite);
							doubleCapBoards.add(bottomRightCapMade);
							hasDoubleCap = true;
						}
						//bottom single capture
						if(!hasDoubleCap){
							int[] downCaptureBoard = b.toArray();
							downCaptureBoard[theBottom(theBottom(i))] = BLACK_PAWN;
							downCaptureBoard[i] = NO_PIECE;
							downCaptureBoard[theBottom(i)] = NO_PIECE;
							Board downCaptureMade = new Board(downCaptureBoard,pIsWhite);
							possibleBoards.add(downCaptureMade);
							hasCaptureMove = true;
						}						
					}
			} else if (!pIsWhite && bCheck[i] == BLACK_KING){
				//left Black King Moves
				if((bCheck[theLeft(i)] == WHITE_PAWN || bCheck[theLeft(i)] == WHITE_KING) && bCheck[theLeft(theLeft(i))] == NO_PIECE){
					int newLoc = theLeft(theLeft(i));
					//left/left double capture
					if((bCheck[theLeft(newLoc)] == WHITE_PAWN || bCheck[theLeft(newLoc)] == WHITE_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] leftDblCapBoard = b.toArray();
						leftDblCapBoard[i] = NO_PIECE;
						leftDblCapBoard[theLeft(i)] = NO_PIECE;
						leftDblCapBoard[newLoc] = NO_PIECE;
						leftDblCapBoard[theLeft(newLoc)] = NO_PIECE;
						leftDblCapBoard[theLeft(theLeft(newLoc))] = BLACK_KING;
						Board leftDblCapMade = new Board(leftDblCapBoard, pIsWhite);
						doubleCapBoards.add(leftDblCapMade);
						hasDoubleCap = true;
					} 
					//left/top double capture
					if((bCheck[theTop(newLoc)] == WHITE_PAWN || bCheck[theTop(newLoc)] == WHITE_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] leftTopCapBoard = b.toArray();
						leftTopCapBoard[i] = NO_PIECE;
						leftTopCapBoard[theLeft(i)] = NO_PIECE;
						leftTopCapBoard[newLoc] = NO_PIECE;
						leftTopCapBoard[theTop(newLoc)] = NO_PIECE;
						leftTopCapBoard[theTop(theTop(newLoc))] = BLACK_KING;
						Board leftTopCapMade = new Board(leftTopCapBoard, pIsWhite);
						doubleCapBoards.add(leftTopCapMade);
						hasDoubleCap = true;
					}
					//left/bottom double capture
					if((bCheck[theBottom(newLoc)] == WHITE_PAWN || bCheck[theBottom(newLoc)] == WHITE_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE){
						int[] leftBottomCapBoard = b.toArray();
						leftBottomCapBoard[i] = NO_PIECE;
						leftBottomCapBoard[theLeft(i)] = NO_PIECE;
						leftBottomCapBoard[newLoc] = NO_PIECE;
						leftBottomCapBoard[theBottom(newLoc)] = NO_PIECE;
						leftBottomCapBoard[theBottom(theBottom(newLoc))] = BLACK_KING;
						Board leftBottomCapMade = new Board(leftBottomCapBoard, pIsWhite);
						doubleCapBoards.add(leftBottomCapMade);
						hasDoubleCap = true;
					}
					//left single cap
					if(!hasDoubleCap){
						int[] leftCaptureBoard = b.toArray();
						leftCaptureBoard[theLeft(theLeft(i))] = BLACK_KING;
						leftCaptureBoard[i] = NO_PIECE;
						leftCaptureBoard[theLeft(i)] = NO_PIECE;
						Board leftCaptureMade = new Board(leftCaptureBoard, pIsWhite);
						possibleBoards.add(leftCaptureMade);
						hasCaptureMove = true;
					}
				}
				//king top moves
				if ((bCheck[theTop(i)] == WHITE_PAWN || bCheck[theTop(i)] == WHITE_KING) && bCheck[theTop(theTop(i))] == NO_PIECE){
					int newLoc = theTop(theTop(i));
					//top/left double cap
					if((bCheck[theLeft(newLoc)] == WHITE_PAWN || bCheck[theLeft(newLoc)] == WHITE_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] topLeftCapBoard = b.toArray();
						topLeftCapBoard[i] = NO_PIECE;
						topLeftCapBoard[theTop(i)] = NO_PIECE;
						topLeftCapBoard[newLoc] = NO_PIECE;
						topLeftCapBoard[theLeft(newLoc)] = NO_PIECE;
						topLeftCapBoard[theLeft(theLeft(newLoc))] = BLACK_KING;
						Board topLeftCapMade = new Board(topLeftCapBoard, pIsWhite);
						doubleCapBoards.add(topLeftCapMade);
						hasDoubleCap = true;
					} 
					//top/top double cap
					if((bCheck[theTop(newLoc)] == WHITE_PAWN || bCheck[theTop(newLoc)] == WHITE_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] topDblCapBoard = b.toArray();
						topDblCapBoard[i] = NO_PIECE;
						topDblCapBoard[theTop(i)] = NO_PIECE;
						topDblCapBoard[newLoc] = NO_PIECE;
						topDblCapBoard[theTop(newLoc)] = NO_PIECE;
						topDblCapBoard[theTop(theTop(newLoc))] = BLACK_KING;
						Board topDblCapMade = new Board(topDblCapBoard, pIsWhite);
						doubleCapBoards.add(topDblCapMade);
						hasDoubleCap = true;
					} 
					//top/right double cap
					if((bCheck[theRight(newLoc)] == WHITE_PAWN || bCheck[theRight(newLoc)] == WHITE_KING) && bCheck[theRight(theRight(newLoc))] == NO_PIECE){
						int[] topRightCapBoard = b.toArray();
						topRightCapBoard[i] = NO_PIECE;
						topRightCapBoard[theTop(i)] = NO_PIECE;
						topRightCapBoard[newLoc] = NO_PIECE;
						topRightCapBoard[theRight(newLoc)] = NO_PIECE;
						topRightCapBoard[theRight(theRight(newLoc))] = BLACK_KING;
						Board topRightCapMade = new Board(topRightCapBoard, pIsWhite);
						doubleCapBoards.add(topRightCapMade);
						hasDoubleCap = true;
					}
					//top single cap
					if(!hasDoubleCap){
						int[] topCaptureBoard = b.toArray();
						topCaptureBoard[theTop(theTop(i))] = BLACK_KING;
						topCaptureBoard[i] = NO_PIECE;
						topCaptureBoard[theTop(i)] = NO_PIECE;
						Board topCaptureMade = new Board(topCaptureBoard, pIsWhite);
						possibleBoards.add(topCaptureMade);
						hasCaptureMove = true;
					}
				}
				//right Black King Moves
				if((bCheck[theRight(i)] == WHITE_PAWN || bCheck[theRight(i)] == WHITE_KING) && bCheck[theRight(theRight(i))] == NO_PIECE){
					int newLoc = theRight(theRight(i));
					//right/right double capture
					if((bCheck[theRight(newLoc)] == WHITE_PAWN || bCheck[theRight(newLoc)] == WHITE_KING) 
						&& bCheck[theRight(theRight(newLoc))] == NO_PIECE){
						int[] rightDblCapBoard = b.toArray();
						rightDblCapBoard[i] = NO_PIECE;
						rightDblCapBoard[theRight(i)] = NO_PIECE;
						rightDblCapBoard[newLoc] = NO_PIECE;
						rightDblCapBoard[theRight(newLoc)] = NO_PIECE;
						rightDblCapBoard[theRight(theRight(newLoc))] = BLACK_KING;
						Board rightDblCapMade = new Board(rightDblCapBoard, pIsWhite);
						doubleCapBoards.add(rightDblCapMade);
						hasDoubleCap = true;
					} 
					//right/top double capture
					if((bCheck[theTop(newLoc)] == WHITE_PAWN || bCheck[theTop(newLoc)] == WHITE_KING) && bCheck[theTop(theTop(newLoc))] == NO_PIECE){
						int[] rightTopCapBoard = b.toArray();
						rightTopCapBoard[i] = NO_PIECE;
						rightTopCapBoard[theRight(i)] = NO_PIECE;
						rightTopCapBoard[newLoc] = NO_PIECE;
						rightTopCapBoard[theTop(newLoc)] = NO_PIECE;
						rightTopCapBoard[theTop(theTop(newLoc))] = BLACK_KING;
						Board rightTopCapMade = new Board(rightTopCapBoard, pIsWhite);
						doubleCapBoards.add(rightTopCapMade);
						hasDoubleCap = true;
					}
					//right/bot double capture
					if((bCheck[theBottom(newLoc)] == WHITE_PAWN || bCheck[theBottom(newLoc)] == WHITE_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE){
						int[] rightBottomCapBoard = b.toArray();
						rightBottomCapBoard[i] = NO_PIECE;
						rightBottomCapBoard[theRight(i)] = NO_PIECE;
						rightBottomCapBoard[newLoc] = NO_PIECE;
						rightBottomCapBoard[theBottom(newLoc)] = NO_PIECE;
						rightBottomCapBoard[theBottom(theBottom(newLoc))] = BLACK_KING;
						Board rightBottomCapMade = new Board(rightBottomCapBoard, pIsWhite);
						doubleCapBoards.add(rightBottomCapMade);
						hasDoubleCap = true;
					}
					//right single cap
					if(!hasDoubleCap){
						int[] rightCaptureBoard = b.toArray();
						rightCaptureBoard[theRight(theRight(i))] = BLACK_KING;
						rightCaptureBoard[i] = NO_PIECE;
						rightCaptureBoard[theRight(i)] = NO_PIECE;
						Board rightCaptureMade = new Board(rightCaptureBoard, pIsWhite);
						possibleBoards.add(rightCaptureMade);
						hasCaptureMove = true;
					}
				}
				//bottom Black King Moves
				if((bCheck[theBottom(i)] == WHITE_PAWN || bCheck[theBottom(i)] == WHITE_KING) && bCheck[theBottom(theBottom(i))] == NO_PIECE){
					int newLoc = theBottom(theBottom(i));
					//bottom/left double capture
					if((bCheck[theLeft(newLoc)] == WHITE_PAWN || bCheck[theLeft(newLoc)] == WHITE_KING) && bCheck[theLeft(theLeft(newLoc))] == NO_PIECE){
						int[] bottomLeftCapBoard = b.toArray();
						bottomLeftCapBoard[i] = NO_PIECE;
						bottomLeftCapBoard[theBottom(i)] = NO_PIECE;
						bottomLeftCapBoard[newLoc] = NO_PIECE;
						bottomLeftCapBoard[theLeft(newLoc)] = NO_PIECE;
						bottomLeftCapBoard[theLeft(theLeft(newLoc))] = BLACK_KING;
						Board bottomLeftCapMade = new Board(bottomLeftCapBoard, pIsWhite);
						doubleCapBoards.add(bottomLeftCapMade);
						hasDoubleCap = true;
					} 
					//bottom/bottom double capture
					if((bCheck[theBottom(newLoc)] == WHITE_PAWN || bCheck[theBottom(newLoc)] == WHITE_KING) && bCheck[theBottom(theBottom(newLoc))] == NO_PIECE){
						int[] bottomDblCapBoard = b.toArray();
						bottomDblCapBoard[i] = NO_PIECE;
						bottomDblCapBoard[theBottom(i)] = NO_PIECE;
						bottomDblCapBoard[newLoc] = NO_PIECE;
						bottomDblCapBoard[theBottom(newLoc)] = NO_PIECE;
						bottomDblCapBoard[theBottom(theBottom(newLoc))] = BLACK_KING;
						Board bottomDblCapMade = new Board(bottomDblCapBoard, pIsWhite);
						doubleCapBoards.add(bottomDblCapMade);
						hasDoubleCap = true;
					}
					//bottom/right double cap
					if((bCheck[theRight(newLoc)] == WHITE_PAWN || bCheck[theRight(newLoc)] == WHITE_KING) && bCheck[theRight(theRight(newLoc))] == NO_PIECE){
						int[] bottomRightCapBoard = b.toArray();
						bottomRightCapBoard[i] = NO_PIECE;
						bottomRightCapBoard[theBottom(i)] = NO_PIECE;
						bottomRightCapBoard[newLoc] = NO_PIECE;
						bottomRightCapBoard[theRight(newLoc)] = NO_PIECE;
						bottomRightCapBoard[theRight(theRight(newLoc))] = BLACK_KING;
						Board bottomRightCapMade = new Board(bottomRightCapBoard, pIsWhite);
						doubleCapBoards.add(bottomRightCapMade);
						hasDoubleCap = true;
					}
					//bottom single cap
					if(!hasDoubleCap){
						int[] bottomCaptureBoard = b.toArray();
						bottomCaptureBoard[theBottom(theBottom(i))] = BLACK_KING;
						bottomCaptureBoard[i] = NO_PIECE;
						bottomCaptureBoard[theBottom(i)] = NO_PIECE;
						Board bottomCaptureMade = new Board(bottomCaptureBoard, pIsWhite);
						possibleBoards.add(bottomCaptureMade);
						hasCaptureMove = true;
					}
				}
			}
		}
		//if no captures exist, check for non capture moves
		if(!hasCaptureMove && !hasDoubleCap){
			for(int i=1; i<33; i++){
				//white pawn non capture moves
				if (pIsWhite && bCheck[i] == WHITE_PAWN){
					if(bCheck[theLeft(i)] == NO_PIECE){
						int[] leftMoveBoard = b.toArray();
						leftMoveBoard[theLeft(i)] = WHITE_PAWN;
						leftMoveBoard[i] = NO_PIECE;
						Board leftMoveMade = new Board(leftMoveBoard, pIsWhite);
						possibleBoards.add(leftMoveMade);
					}
					if(bCheck[theTop(i)] == NO_PIECE){
						int[] upMoveBoard = b.toArray();
						upMoveBoard[theTop(i)] = WHITE_PAWN;
						upMoveBoard[i] = NO_PIECE;
						Board upMoveMade = new Board(upMoveBoard, pIsWhite);
						possibleBoards.add(upMoveMade);
					}
					//white king
				} else if (pIsWhite && bCheck[i] == WHITE_KING){
					if(bCheck[theLeft(i)] == NO_PIECE){
						int[] leftMoveBoard = b.toArray();
						leftMoveBoard[theLeft(i)] = WHITE_KING;
						leftMoveBoard[i] = NO_PIECE;
						Board leftMoveMade = new Board(leftMoveBoard, pIsWhite);
						possibleBoards.add(leftMoveMade);
					}
					if(bCheck[theTop(i)] == NO_PIECE){
						int[] upMoveBoard = b.toArray();
						upMoveBoard[theTop(i)] = WHITE_KING;
						upMoveBoard[i] = NO_PIECE;
						Board upMoveMade = new Board(upMoveBoard, pIsWhite);
						possibleBoards.add(upMoveMade);
					}
					if(bCheck[theRight(i)] == NO_PIECE){
						int[] rightMoveBoard = b.toArray();
						rightMoveBoard[theRight(i)] = WHITE_KING;
						rightMoveBoard[i] = NO_PIECE;
						Board rightMoveMade = new Board(rightMoveBoard, pIsWhite);
						possibleBoards.add(rightMoveMade);
					}
					if(bCheck[theBottom(i)] == NO_PIECE){
						int[] downMoveBoard = b.toArray();
						downMoveBoard[theBottom(i)] = WHITE_KING;
						downMoveBoard[i] = NO_PIECE;
						Board downMoveMade = new Board(downMoveBoard, pIsWhite);
						possibleBoards.add(downMoveMade);
					}
					//black pawn
				} else if (!pIsWhite && bCheck[i] == BLACK_PAWN){
					if(bCheck[theRight(i)] == NO_PIECE){
						int[] rightMoveBoard = b.toArray();
						rightMoveBoard[theRight(i)] = BLACK_PAWN;
						rightMoveBoard[i] = NO_PIECE;
						Board rightMoveMade = new Board(rightMoveBoard, pIsWhite);
						possibleBoards.add(rightMoveMade);
					}
					if(bCheck[theBottom(i)] == NO_PIECE){
						int[] downMoveBoard = b.toArray();
						downMoveBoard[theBottom(i)] = BLACK_PAWN;
						downMoveBoard[i] = NO_PIECE;
						Board downMoveMade = new Board(downMoveBoard, pIsWhite);
						possibleBoards.add(downMoveMade);
					}
					//black king
				} else if (!pIsWhite && bCheck[i] == BLACK_KING){
					if(bCheck[theLeft(i)] == NO_PIECE){
						int[] leftMoveBoard = b.toArray();
						leftMoveBoard[theLeft(i)] = BLACK_KING;
						leftMoveBoard[i] = NO_PIECE;
						Board leftMoveMade = new Board(leftMoveBoard, pIsWhite);
						possibleBoards.add(leftMoveMade);
					}
					if(bCheck[theTop(i)] == NO_PIECE){
						int[] upMoveBoard = b.toArray();
						upMoveBoard[theTop(i)] = BLACK_KING;
						upMoveBoard[i] = NO_PIECE;
						Board upMoveMade = new Board(upMoveBoard, pIsWhite);
						possibleBoards.add(upMoveMade);
					}
					if(bCheck[theRight(i)] == NO_PIECE){
						int[] rightMoveBoard = b.toArray();
						rightMoveBoard[theRight(i)] = BLACK_KING;
						rightMoveBoard[i] = NO_PIECE;
						Board rightMoveMade = new Board(rightMoveBoard, pIsWhite);
						possibleBoards.add(rightMoveMade);
					}
					if(bCheck[theBottom(i)] == NO_PIECE){
						int[] downMoveBoard = b.toArray();
						downMoveBoard[theBottom(i)] = BLACK_KING;
						downMoveBoard[i] = NO_PIECE;
						Board downMoveMade = new Board(downMoveBoard, pIsWhite);
						possibleBoards.add(downMoveMade);
					}
				}

			}
		}
		System.out.println("Possible Moves: " + possibleBoards.size());
		if(hasDoubleCap){
			//check every board for new kings
			for(Board kingMe : doubleCapBoards){
				int[] kingBoard = kingMe.toArray();
				//check for new white kings
				for(int i = 1; i<5; i++){
					if(kingBoard[i] == WHITE_PAWN){
						kingBoard[i] = WHITE_KING;
					}
				}
				//check for new black kings
				for(int i = 29; i<33; i++){
					if(kingBoard[i] == BLACK_PAWN){
						kingBoard[i] = BLACK_KING;
					}
				}
				kingMe = new Board(kingBoard, isWhite);
			}
			return doubleCapBoards;
		}
		for(Board kingMeB : possibleBoards){
			int[] kingBoard = kingMeB.toArray();
			for(int i = 1; i<5; i++){
				if(kingBoard[i] == WHITE_PAWN){
					kingBoard[i] = WHITE_KING;
				}
			}
			for(int i = 29; i<33; i++){
				if(kingBoard[i] == BLACK_PAWN){
					kingBoard[i] = BLACK_KING;
				}
			}
			kingMeB = new Board(kingBoard, isWhite);
		}
		return possibleBoards;

	}

	public int BoardValue(Board initBrd, Player p, int ply, int alpha, int beta, Board theBest) throws java.rmi.RemoteException {
		Boolean pIsWhite;
		Player notThisPlayer;
		if(p == this){
			pIsWhite = isWhite;
			notThisPlayer = new ComputerBPlayer("temp");
		} else {
			pIsWhite = !isWhite;
			notThisPlayer = this;
		}
		int[] initialBoard = initBrd.toArray();
		if (ply >= MAX_PLY){
			return BoardEval(initBrd);
		}
		List<Board> boards = ReachableBoards(initBrd, p);
		if (boards.isEmpty()){
			return BoardEval(initBrd);
		}
		if(ply == 0)
			best = boards.get(0);			//??What to do if boards is empty???
		for (Board b : boards){
			try{
				int val = BoardValue(b, notThisPlayer, ply+1, alpha, beta, best);
				if (pIsWhite){
				//if (p == PLAYER_MAX){
					if (val > alpha){
						alpha = val;
						if(ply == 0){
							best = b;
						}
					}
					if (alpha >= beta){
						return alpha;	//Prune! Alpha-cutoff
					}
				}
				else{					//Min's turn
					if (val < beta){
						beta = val;
						if(ply == 0){
							best = b;
						}
					}
					if (alpha >= beta){
						return beta;	//Prune! Beta-cutoff
					}
				}
			}
			catch (RemoteException ex){
				System.err.println("RemoteException");
			}
			
		}
		return (pIsWhite ? alpha : beta);
	}

	public static void main (String[] args) {
		if (args.length != 2 || (!args[0].equals("1") && !args[0].equals("2"))) {
      		System.err.println("Usage: java ComputerBPlayer X FOO, where X is 1 for registering the agent as 'first',\n"+
                         "  2 for registering it as 'second'.  The second argument (FOO)is the name of the agent.\n");
      		System.exit(-1);
   		}

    	String playerName = args[1];
    	String playerRegistration = (args[0].equals("1") ? "first" : "second");

    	System.setSecurityManager(new SecurityManager());

    	try {
     		ComputerBPlayer p = new ComputerBPlayer(playerName);
     		if(args[0].equals("1")){
    			p.isWhite = false;
    		} else {
    			p.isWhite = true;
    		}
      		Naming.rebind(playerRegistration, p);
      		System.out.println("Player "+playerRegistration+"(named "+playerName+") is waiting for the referee");
    	}
    	catch (MalformedURLException ex) {
      		System.err.println("Bad URL for RMI server");
      		System.err.println(ex);
    	}
    	catch (RemoteException ex) {
      		System.err.println(ex);
    	}
	}

	public String getName() throws java.rmi.RemoteException {
		return name;
	}

	private String boardToString (int[] pieces) {
    	String result = "  ";
    	for (int pos = 1; pos <= 32; pos++) {
      		if (pos % 4 == 1 && pos != 1) {
        		result += "\n";
        	if (!isLeftRow(pos)) {
          		result += "  ";
       		}
      	}
      	String piece;
      	switch (pieces[pos]) {
        	case BLACK_KING:
          		piece = "B ";
          		break;
        	case BLACK_PAWN:
          		piece = "b ";
          		break;
        	case NO_PIECE:
          		piece = Integer.toString(pos);
          		if (pos < 10) piece +=" ";  // All spaces should be two characters
          		break;
        	case WHITE_PAWN:
          		piece = "w ";
          		break;
        	case WHITE_KING:
          		piece = "W ";
          		break;
        	default:
          		System.err.println("Illegal piece " + pieces[pos] + " at position " +
                             pos + "!!");
          	return "ERROR--BAD BOARD, Illegal piece " + pieces[pos] +
              " at position " + pos + "!!";
      	}
      	result += piece+"  ";
    	}
    	return result;
  	}

  	private boolean isLeftRow(int pos) {
    	return (pos - 1) % 8 > 3;
  	}


}