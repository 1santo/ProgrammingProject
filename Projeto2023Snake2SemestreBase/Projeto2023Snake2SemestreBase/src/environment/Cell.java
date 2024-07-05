package environment;


import game.GameElement;
import game.Goal;
import game.Killer;
import game.Obstacle;
import game.Snake;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.AutomaticSnake;

public class Cell{
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	
	private Lock lockC = new ReentrantLock();
	private Condition cellOccupied = lockC.newCondition();
	
		public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public GameElement getGameElement() {
		return gameElement;
	}

	public BoardPosition getPosition() {
		return position;
	}

	// request a cell to be occupied by Snake, If it is occupied by another Snake or Obstacle, wait.
	public  void request(Snake snake) throws InterruptedException { //
		lockC.lock();
		try {
			if(!isOcupied()) {
				ocuppyingSnake=snake;
				//inicialmente adiciona a primeira cell
				snake.getCells().addFirst(this);
				//mas se ja for cobra com mais celulas>
				if(snake.getCells().size()>1) {
				Cell last = snake.getCells().getLast();
					snake.getCells().removeLast();
				}
				if(isOcupiedByGoal()) {
					getGoal().captureGoal(); //se for goal incremnta premio
					removeGoal(); //remove goal da celula
					//falta meter objeto goal noutro sitio
						
				}
			}
			else if(isOccupiedByKiller()) {
				snake.killSnake(); //mata cobra
				cellOccupied.notifyAll();  //avisa quem estava a espera q n ta ocupada
			}
			
			else {
				cellOccupied.await(); //espera q a cel fique desocupada
			}
			
		}finally {
			lockC.unlock();
		}
	}

	public void release() {//
		lockC.lock();
		try {
			if(isOcupiedBySnake()) {
				Snake snakeTemp = ocuppyingSnake;
				ocuppyingSnake=null;
				cellOccupied.notifyAll();
			}
			else if (isOcupiedByGoal()){
				
			}
			else if (isOccupiedByKiller()){
				
			}
			else if (isOcupiedByObstacle()){
				
			}
			
		}finally {
			lockC.unlock();
		}
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}

	@Override
	public String toString() {
		return "" + position;
	}

	public void setGameElement(GameElement obstacle) {
		// TODO

	}

	public boolean isOcupied() { //
		return isOcupiedBySnake() || isOccupiedByKiller() || isOcupiedByObstacle();
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public Goal removeGoal() { //
		// TODO
		return null;
	}
	public void removeObstacle() {
		// TODO
	}


	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}


	public boolean isOccupiedByKiller() {
		return (gameElement!=null && gameElement instanceof Killer);
	}


	public boolean isOcupiedByObstacle() {
		return (gameElement!=null && gameElement instanceof Obstacle);
	}


	public void removeSnake(Snake snake) { //
		// TODO
	}

	public void requestInitialPositioning() {
		
	}

}
