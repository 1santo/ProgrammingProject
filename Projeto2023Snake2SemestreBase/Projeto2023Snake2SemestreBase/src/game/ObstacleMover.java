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
				move(pos);
				
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
	protected void move(BoardPosition newPos) throws InterruptedException {
		
		
		obstacle.getCells();
		// clear obstacle list , necessary when resetting obstacles.
		System.out.println("lista obst: "+board.getObstacles());		
		board.getObstacles().removeFirst();
		
		
		//board.getObstacles().clear();
	//	board.removeGameElement(this.obstacle);

		
		board.setChanged();

		
		System.out.println("cleared, lista obst: "+board.getObstacles());		

		
	    		//	this.obstacle.doInitialPositioning();
	    			System.out.println("obst"+this.obstacle);
	    			board.setChanged();
		
		}
}
