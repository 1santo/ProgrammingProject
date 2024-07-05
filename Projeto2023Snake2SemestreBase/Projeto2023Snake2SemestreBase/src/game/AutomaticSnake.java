package game;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}

	@Override
	public void run() {
		
		doInitialPositioning();
		System.out.println(getBoardPosition());
		System.err.println(this.getIdentification()+" initial size:"+snakecells.size());
	
	try {
		Thread.sleep(1000); //10000
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	
	while (!wasKilled()) {
		try {
			System.out.println("board pos: "+this+getBoardPosition());
			
			while(notInMaxGoal) {
				
				try {
				move(nextCell());
	        	getBoard().setChanged();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("out of board");
					return;
				}
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL); //apos mover dorme
			}
				//&&(getBoardPosition().x!=getBoard().WIDTH)&&(getBoardPosition().y!=getBoard().HEIGHT)) {
				//System.out.println("MOVED TO: " + nextCell().getPosition());
				//	move(nextCell); //move to top or bottom etc cell
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	}

	//
	private Cell nextCell() {
		Cell head = getCells().getFirst();
		List<BoardPosition> possiblePositions = getBoard().getNeighboringPositions(head);
		
		//System.out.println(head+" Posicoes possiveis: " + possiblePositions); 
		 
		BoardPosition newPosition = getBoard().selectPositionClosestToGoal(possiblePositions);

		//System.out.println("Posicao escolhida: " + newPosition);
		
		if (newPosition != null) {
			return getBoard().getCell(newPosition);
		} else {
			killSnake();
			return null;
		}
	}
	
	
	@Override
	protected void move(Cell newCell) throws InterruptedException {//
		Cell cellWithHead= getCells().getFirst();
		Cell smallest = cellWithHead.compareTo(newCell)<0 ? cellWithHead : newCell; //condition ? value_if_true : value_if_false;
		Cell biggest = cellWithHead.compareTo(newCell)<0 ? newCell : cellWithHead;
		smallest.getLock().lock();
		try {
			biggest.getLock().lock();
				try {
					newCell.request(this);
				}finally {
					biggest.getLock().unlock();
				}
				
		}finally {
			smallest.getLock().unlock();
		}
		
	}
	
}