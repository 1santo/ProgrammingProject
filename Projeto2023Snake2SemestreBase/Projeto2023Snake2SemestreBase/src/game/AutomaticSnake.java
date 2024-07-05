package game;

import java.util.LinkedList;
import java.util.List;

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
			moveTowardsGoal();
            getBoard().setChanged();
			
			while(notInMaxGoal) {
					//&&(getBoardPosition().x!=getBoard().WIDTH)&&(getBoardPosition().y!=getBoard().HEIGHT)) {
				//System.out.println("MOVED TO: " + nextCell().getPosition());
				
				try {
				//	move(nextCell); //move to top or bottom etc cell

					getBoard().setChanged();
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("out of board");
					return;
				}
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL); //apos mover dorme
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	}
	
	//
	private void moveTowardsGoal() { 
		Cell head = getCells().getFirst();
        List<BoardPosition> possiblePositions = getBoard().getNeighboringPositions(head);
        BoardPosition newPosition = getBoard().selectPositionClosestToGoal(possiblePositions);

        if (newPosition != null) {
            Cell newCell = getBoard().getCell(newPosition);
            try {
                move(newCell);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            killSnake();
        }
		
	}

	@Override
	protected void move(Cell newCell) throws InterruptedException {//
		newCell.request(this);
	}
	
}