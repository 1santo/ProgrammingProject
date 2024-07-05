package game;

import java.io.Serializable;
import java.util.LinkedList;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;

public abstract class Snake extends Thread {// em vez de extend meti implement

	private boolean killed = false ;
	protected LinkedList<Cell> snakecells = new LinkedList<Cell>();
	protected int size = 5;
	private int id;
	private Board board;
	private BoardPosition init; //
	protected boolean notInMaxGoal = true; //
	
	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
	}

	
	public void killSnake () { killed = true ; }
	public boolean wasKilled () { return killed == true ;}
	
	public int getSize() {
		return size;
	}

	public int getIdentification() {
		return id;
	}

	public int getCurrentLength() {
		return snakecells.size();
	}
	
	public LinkedList<Cell> getCells() {
		return snakecells;
	}
	
	protected void move(Cell newCell) throws InterruptedException {//
		newCell.request(this);
	}
	
	
	protected void doInitialPositioning() { //
		// Random position on the first column. 
				// At startup, snake occupies a single cell
				int posX = 0;
				int posY = (int) (Math.random() * Board.HEIGHT);
				init = new BoardPosition(posX, posY);
				
				try {
					Cell initialCell = board.getCell(init);
					initialCell.request(this);
					//snakecells.add(initialCell);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//snakecells.add(board.getCell(init));
				System.err.println("Snake "+getIdentification()+" starting at:"+getCells().getLast().getPosition());	
				//a ultima pos da lista vai ter a cauda^
				
	}
	
	public Board getBoard() {
		return board;
	}
	
	public BoardPosition getBoardPosition() {
		return init;
	}
	
	// Utility method to return cells occupied by snake as a list of BoardPosition
	// Used in GUI. Do not alter
	public synchronized LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<BoardPosition>();
		for (Cell cell : snakecells) {
			coordinates.add(cell.getPosition());
		}

		return coordinates;
	}	
}
