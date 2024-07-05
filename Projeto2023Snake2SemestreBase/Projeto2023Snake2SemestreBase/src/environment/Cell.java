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

public class Cell implements Comparable<Cell>{//meti implements comaprable
	//pra qnd formos mover no jogador, saber qual o lock da cell
//que adquirimos primeiro
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	
	private Lock lockC = new ReentrantLock();//
	private Condition cellOccupiedTemp= lockC.newCondition();//temporaria com cobra ou obs
	private Condition cellDeoccupied = lockC.newCondition();//temporaria
	private Condition cellOccupiedByGoal = lockC.newCondition();//vai notificar que o sitio do goal é outro
	private Condition cellOccupiedByKiller = lockC.newCondition();//killer nao vai sair dali
	
		public Cell(BoardPosition position)  {
		super();
		this.position = position;
	}

	//
	public Lock getLock() {
		return lockC;
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
				cellOccupiedTemp.signalAll();
				//mas se ja for cobra com mais celulas>
				//if(snakeTemp.getCells().size()>1) {
				//Cell last = snakeTemp.getCells().getLast();
			//	ocuppyingSnake.getCells().removeLast();
				//}
				
				if(isOcupiedByGoal()) {
					snake.killSnake();
					//se o premio for o valor maximo (9) termina o jogo
					if(getGoal().getValue()==Goal.MAX_VALUE-1) {
						getGoal().captureGoal();
						//tem q dizer q agr o sitio do goal e outro
						//cellOccupiedByGoal.signalAll();
						snake.getBoard().gameOver(); //termina o jogo
						
					}
					else {
						getGoal().captureGoal(); //se for goal incremnta premio
						removeGoal(); //remove goal da celula
						//falta meter objeto goal noutro sitio
					}
						
				}

			}
			else if(isOccupiedByKiller()) {
				snake.killSnake(); //mata cobra
				cellDeoccupied.notifyAll();  //avisa quem estava a espera q n ta ocupada
			}
			
			else {//if ocupada por snake or obstacle
				cellDeoccupied.await(); //espera q a cel fique desocupada
			
			}
			
		}finally {
			lockC.unlock();
		}
	}

	public void release() throws InterruptedException {//
		lockC.lock();
		
		try {
			if(isOcupiedBySnake()) {
				if (!ocuppyingSnake.getCells().isEmpty()) {
					Snake snakeTemp = ocuppyingSnake;
					ocuppyingSnake.getCells().removeLast();
					ocuppyingSnake=null;
					cellDeoccupied.signalAll();
				}

			}
			else if (isOcupiedByGoal()){
				
			}
			else if (isOccupiedByKiller()){
				cellOccupiedTemp.await();//fica a espera ate q uma cobra venha
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

	public void setGameElement(GameElement obstacle) throws InterruptedException {
		lockC.lock();
		try {
			while(isOcupied() || isOcupiedByGoal()) cellDeoccupied.await();
				gameElement=obstacle;
				//obstacle.
				//cellOccupiedTemp.signalAll();
		}finally {
			lockC.unlock();
		}

	}
	
	public void removeObstacle() { //
		gameElement = null;
	}


	public boolean isOcupied() { // fiz e n considerei goal nem killer ocupado
		return isOcupiedBySnake() || isOcupiedByObstacle();
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public Goal removeGoal() { //
		Goal goal = (Goal) gameElement;
		gameElement = null;
		return goal;
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

	//
	@Override
	public int compareTo(Cell other) {
	//	return this.position - other.position; //n pode pois n sao ints
//ent vou implementar comparable no boardposition
		return this.position.compareTo(other.position);
	}

}
