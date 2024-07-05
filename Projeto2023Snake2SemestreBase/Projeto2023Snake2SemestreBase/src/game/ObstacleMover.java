package game;

import environment.Board;
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

		for(int i=0; i< obstacle.getRemainingMoves();i++) {
			try {
				move();
				obstacle.decrementRemainingMoves();
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
	protected void move() throws InterruptedException {
		
		// clear obstacle list , necessary when resetting obstacles.
		System.out.println("lista obst: "+board.getObstacles());		

		board.getObstacles().clear();
		//board.removeGameElement(this.obstacle);

		
		board.setChanged();

		
		System.out.println("cleared, lista obst: "+board.getObstacles());		

		Thread.sleep(obstacle.OBSTACLE_MOVE_INTERVAL);
	    			this.obstacle.doInitialPositioning();
	    			System.out.println("obst"+this.obstacle);
	    			board.setChanged();
		
		}
}
