package environment;


import game.GameElement;
import game.Goal;
import game.Killer;
import game.Obstacle;
import game.Snake;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.AutomaticSnake;

public class Cell implements Comparable<Cell>{//meti implements comaprable
	//pra qnd formos mover no jogador, saber qual o lock da cell
//que adquirimos primeiro
	private BoardPosition position;
	private Board board;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	
	private Lock lockC = new ReentrantLock();//
	private Condition cellOccupiedTemp= lockC.newCondition();//temporaria com cobra, killer ta a espera pra matar cobras
	private Condition cellDeoccupied = lockC.newCondition();//temporaria
	private Condition cellOccupiedByGoal = lockC.newCondition();//vai notificar que o sitio do goal é outro	
	private Condition goalCaptured = lockC.newCondition();//vai notificar que goal ja foi captured pra nao continuarem a andar

	//se todas as cobras estao a emperradas uma na outra, obstacle nao move
		public Cell(BoardPosition position,Board board)  {
		//super();
		this.position = position;
		this.board=board;
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
			if(!isOcupied() && !isOccupiedByKiller()) {
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
					//System.out.println("BIGGGGGGGGGG TESTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
					//snake.killSnake();
					//se o premio for o valor maximo (9) termina o jogo
					if(getGoal().getValue()==Goal.MAX_VALUE) {
					//	getGoal().captureGoal();
						//tem q dizer q agr o sitio do goal e outro
						//cellOccupiedByGoal.signalAll();

								snake.getBoard().gameOver();

						//termina o jogo
						//goalCaptured.signalAll();
					}
					else {
						int novo= getGoal().getGoalValue()+1; //se for goal incremnta premio, devia ser na funcao debaixo
						snake.captureGoal(getGoal()); 
						removeGoal(); //remove goal da celula
						//dar notificacao q o valor daquele premio incrementou pra nova posicao poder mudar praquele sitio
						
						//falta meter objeto goal noutro sitio
						
					//	Random random=new Random();
					//	int novo=random.nextInt(9-1+1)+1;
						Goal goal = new Goal(board,novo);
				        board.setGoalPosition(goal,goal.pos);
				        goal.setGoalValue(novo);
					}
						
				}

			}
			
			else if(isOccupiedByKiller()) {
				//System.out.println("DIEEEEE");
				System.out.println(snake+"____ will die by Killer");
				snake.killSnake(); //mata cobra
				System.out.println(snake+"____ dead by Killer");
				cellDeoccupied.signalAll();  //avisa quem estava a espera q n ta ocupada
			}
			else {//if ocupada por snake
				
				while(isOcupied() | isOccupiedByKiller()) {//mudei
				cellDeoccupied.await(); //espera q a cel fique desocupada
				}
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

	public void setGameElement(GameElement element) throws InterruptedException {
		lockC.lock();
		try {
			while(isOcupied() || isOcupiedByGoal()) cellDeoccupied.await();//deadlock se tiver no lugar de cobra emperrada
			/*while(isOcupied() || isOcupiedByGoal()) {
			if(isOcupiedBySnake())setGameElement(element);
				else cellDeoccupied.await();
			}*///Potentially dangerous stack overflow in ReservedStackAccess
			
			if(element instanceof Obstacle){
					gameElement=element; //celula passa a ter
					Obstacle obstacle = (Obstacle) element;
					obstacle.getCells().addFirst(this); //metodo de GameElement mas quero so pra meter no obstacle
				}
				else if(element instanceof Goal) {
					gameElement=element; //celula passa a ter
					
					Goal goal = (Goal) element;
					goal.getCells().addFirst(this); //metodo de GameElement mas quero so pra meter no obstacle
					//no localboard antes tinha isto
					//Goal goal = new Goal(this,random);
					//	BoardPosition goalPos = getRandomPosition();
				     //   setGoalPosition(goalPos);
				      //  addGameElement(goal); 
				      //  System.out.println("Goal of "+goal.getGoalValue()+"pos: "+goalPos);
				      //  setChanged();
				}
				else if(element instanceof Killer) {
					gameElement=element;
					//System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"+gameElement);
					Killer killer = (Killer) element;
					killer.getCells().addFirst(this);
				}
				
		}finally {
			lockC.unlock();
		}

	}
	
	public void removeObstacle() { //
		((Obstacle)gameElement).getCells().removeLast();
		gameElement = null;
		cellDeoccupied.signalAll();
	}


	public boolean isOcupied() { // fiz e n considerei goal nem killer ocupado
		return isOcupiedBySnake() || isOcupiedByObstacle();
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public Goal removeGoal() { //
		Goal goal = (Goal) gameElement;
		System.out.println("irei remover goal> "+goal.getCells());
		gameElement = null;
		System.out.println("irei remover goal> "+goal.getCells());
		goal.getCells().removeLast();
		System.out.println("removi goal> "+goal.getCells());
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
		lockC.lock();
		try {
			//LinkedList<Cell> cells=snake.getCells(); //nao modificar cells diretamente, em vez disso modificar copia
			LinkedList<Cell> cells=new LinkedList<Cell>(snake.getCells()); //desta forma e nao da de cima^
			//	snake.getCells().clear();
		//	ocuppyingSnake = null;
			//mas e as outras celulas?
			for (Cell cell : cells) { //nao posso ir iterando e removendo da lista senao dps da erro aqui
				cell.lockC.lock();
				try {
					cell.release();
					System.out.println("Snake removed from "+cell.getPosition()+"____ | "+this.getClass());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					cell.lockC.unlock();
				}
			}
		}finally {
			lockC.unlock();
		}
		
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
