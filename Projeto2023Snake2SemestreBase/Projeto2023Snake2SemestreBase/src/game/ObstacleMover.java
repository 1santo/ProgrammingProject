package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class ObstacleMover extends Thread {
	private Obstacle obstacle ;
	private Board board ;
	
	//
	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}
	
	@Override
	public void run() { //
		
		try {
			Thread.sleep(obstacle.OBSTACLE_MOVE_INTERVAL);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0; i< obstacle.getRemainingMoves();i++) {
			try {
				BoardPosition pos=board.getRandomPosition();
				move(board.getCell(pos));
				
				//obstacle.decrementRemainingMoves();
				board.setChanged();
				System.out.println(obstacle.getRemainingMoves());
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("out of board");
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//
	protected void move(Cell toCell) throws InterruptedException {
		try {
			Cell lastCell =obstacle.getCells().getLast();
			
			Cell smallest = lastCell.compareTo(toCell)<0 ? lastCell : toCell; //condition ? value_if_true : value_if_false;
			Cell biggest = lastCell.compareTo(toCell)<0 ? toCell : lastCell;
			
			smallest.getLock().lock();
			try {
				biggest.getLock().lock();
					try {
						System.out.println("1. "+obstacle+" na pos: "+obstacle.getCells());
						Cell last =obstacle.getCells().getLast();
						last.removeObstacle();
						System.out.println("1234567890TESTINHO");
						toCell.setGameElement(obstacle);
						//	addObstacles(obs);
							//addGameElement(obs);
						//}
						
						
						// clear obstacle list , necessary when resetting obstacles.
						System.out.println("2.Obstacle na pos: "+obstacle.getCells());
						board.getObstacles().clear();
						
						
						//board.getObstacles().clear();
					//	board.removeGameElement(this.obstacle);
				
						
						board.setChanged();
				
						
						System.out.println("cleared, lista obst: "+board.getObstacles());		
				
						
					    		//	this.obstacle.doInitialPositioning();
					    			System.out.println("obst"+this.obstacle);
					    			board.setChanged();
		
	    			
	    			
					}finally {
						biggest.getLock().unlock();
					}
					
			}finally {
				smallest.getLock().unlock();
			}
		
			}catch (NullPointerException e) {
		System.out.println("wait till snake has head?");
		
		}
		}
}
