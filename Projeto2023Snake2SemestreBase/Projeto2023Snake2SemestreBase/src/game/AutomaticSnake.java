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
		System.out.println(this.getIdentification()+" initial size:"+snakecells.size());
	
	try {
		Thread.sleep(1000); //10000
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	
	while (!wasKilled()) {
		try {
			System.out.println("Autosnake thread board pos: "+this.getIdentification()+getBoardPosition());
			
			while(notInMaxGoal) {
				System.out.println(this.getIdentification()+" mid size:"+snakecells.size());
				try {
					if(!getCells().isEmpty())//
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
		if(wasKilled()) this.interrupt();//
	
	}

	//
	private Cell nextCell() {
		/*
		if (getCells().isEmpty()) {
			System.err.println("Snake has no cells.");
			killSnake();
			return null;
        }*/
		
		Cell head = getCells().getFirst();
		List<BoardPosition> possiblePositions = getBoard().getNeighboringPositions(head);
		
		//System.out.println(head+" Posicoes possiveis: " + possiblePositions); 
		 
		BoardPosition newPosition = getBoard().selectPositionClosestToGoal(possiblePositions);

		//System.out.println("Posicao escolhida: " + newPosition);
		
		if (newPosition != null) {
			return getBoard().getCell(newPosition);
			
			
			/*
			 		if (newPosition != null) {
			Cell newCell = getBoard().getCell(newPosition);
			 if (!newCell.isOcupied()) {
				 return newCell;
			 } else {
				 System.out.println("Cell ocupada.");
				 return null;
	            }
		} else {
			killSnake();
			return null;
		} 
			 
			 */
			
		} else {
			killSnake();
			return null;
		}
	}
	
	
	@Override
	protected void move(Cell newCell) throws InterruptedException {//
		
		try {
			Cell cellWithHead= getCells().getFirst();
			
			Cell smallest = cellWithHead.compareTo(newCell)<0 ? cellWithHead : newCell; //condition ? value_if_true : value_if_false;
			Cell biggest = cellWithHead.compareTo(newCell)<0 ? newCell : cellWithHead;
			
			smallest.getLock().lock();
			try {
				biggest.getLock().lock();
					try {
						
						if(snakecells.size()>=getSize()) { //verifica tam da cobra so tira se for maior
							if(!snakecells.isEmpty()) {
								Cell last = snakecells.getLast();
								last.release();				
									
							}
						}
						
						//so pode mover-se se conseguir adquirir os dois cadeados
						//pq imaginemos q a cobra ainda ficou la presa atras...
						newCell.request(this); //q remove a cauda e interage com elementos
						
						
					}finally {
						biggest.getLock().unlock();
					}
					
			}finally {
				smallest.getLock().unlock();
			}
		
			}catch (Exception e) {
		System.out.println("wait till snake has head?");
		
		}
	
	}
}