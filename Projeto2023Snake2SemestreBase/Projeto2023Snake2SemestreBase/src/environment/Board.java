package environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import game.GameElement;
import game.Goal;
import game.Killer;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;

// Class is abstract to allow the creation of other kinds of Board, which is not necessary in this project.
public abstract class Board extends Observable {
	protected Cell[][] cells;
	private BoardPosition goalPosition;
	public static final long PLAYER_PLAY_INTERVAL = 300;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	protected LinkedList<Obstacle> obstacles= new LinkedList<Obstacle>(); //
	protected LinkedList<Killer> killers= new LinkedList<Killer>(); //
	protected LinkedList<GameElement> gameElements= new LinkedList<GameElement>(); //
	protected volatile boolean isFinished;

	private int random = ThreadLocalRandom.current().nextInt(1, 10);//ThreadLocalRandom efficient when working w multiple threads
	private Lock locksnakes = new ReentrantLock();//teste
	private Lock lockobs = new ReentrantLock();//teste
	
	public Board() {
		cells = new Cell[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y), this);
			}
		}

	}

	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}

	//changed to pub
	public BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *((WIDTH-1-1+1)))+1,(int) (Math.random() * HEIGHT));//pra n comecar no inicio
	}
/*
	public BoardPosition getGoalPosition() {
		return goalPosition;
	}*/

	public void setGoalPosition(Goal goal, BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}

	public void addGameElement(GameElement gameElement) { //
		if (gameElement instanceof Obstacle) {
			obstacles.add((Obstacle) gameElement);
		}
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<WIDTH-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<HEIGHT-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}

	public BoardPosition selectPositionClosestToGoal(List<BoardPosition> possibleDestinations) { //
		if (goalPosition == null) {
			System.out.println("O goal nao foi colocado no board.");
			return null;
			
		}
		else {
			BoardPosition closestPositionToGoal = null; //tenho q iniciar senao n retorna
			double minDistance = 100000;//inicial basta ser grande o suficiente
	
			for (BoardPosition pos : possibleDestinations) {
			double distance = pos.distanceTo(goalPosition);//v
				if (distance < minDistance) {
					minDistance = distance; //calcula qual a distancia com base no alg euclideano q o prof deu
					closestPositionToGoal = pos; //tou a dizer q vai ser a pos da lista de possiveis pos
				}
			}
			
			return closestPositionToGoal;
			}
	}

/*	protected Goal addGoal() {
		Goal goal = new Goal(this, random);
        addGameElement(goal);
        return goal;
	}*/

	public void addObstacles(Obstacle obs) {//mudei pra add Obstacle em vez de int numberObstacles
		obstacles.add(obs); //pq senao como adicionava o obs a lista???
															//pus public tbm
	}
	
/*	protected void addObstacles(int numberObstacles) {
		for (int i = 0; i < numberObstacles; i++) {
			Obstacle obstacle = new Obstacle(this);
			addGameElement(obstacle);
		}
	}*/

	public LinkedList<Snake> getSnakes() {
		locksnakes.lock();
		try {
			return snakes;
		}finally { //pra garantir q dou unlock por um try smp no q ta a ser feito
			locksnakes.unlock();
		}
	}


	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

/*	public void moveObstacle(Obstacle obstacle) throws InterruptedException { //
		//TODO
		List<BoardPosition> possiblePositions = getNeighboringPositions(getCell(obstacle.getPos()));
        BoardPosition newPosition = possiblePositions.get((int) (Math.random() * possiblePositions.size()));
        getCell(obstacle.getPos()).release();
        obstacle.setPos(newPosition);
        getCell(newPosition).setGameElement(obstacle);
        setChanged();
	}*/

	
	public void removeGoal(Goal goal) {//
		getCell(goalPosition).removeGoal();
		setGoalPosition(goal,null);
		setChanged();
	//	goal = null;
	}
	public boolean isFinished() {
		return isFinished;

	}

	public LinkedList<Obstacle> getObstacles() {
		//TODO
		// percorrer cells e acumular obstáculos numa lista
		lockobs.lock();
		try {
			return obstacles;
		}finally { //pra garantir q dou unlock por um try smp no q ta a ser feito
			lockobs.unlock();
		}
	}

	public LinkedList<Killer> getKillers() {
		lockobs.lock();
		try {
			return killers;
		}finally { 
			lockobs.unlock();
		}
	}


	public void addSnake(Snake snake) {
		snakes.add(snake);
	}

	//
	public void gameOver() {
		
		
		//nao funciona pq as snakes estao e' na lista de cells e nao
		//na lista de snakes
		for (Snake snake : snakes) {
			System.out.println(snakes+"____ all must die");
			snake.killSnake();//interrompe as threads snakes todas
			System.out.println(snakes+"____ all died");
			System.out.println("GAME OVEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEER");
		}
		isFinished = true;
		setChanged();
	}
	
	public abstract void init(); 

	
	// Ignorar: para johador humano
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();



	protected void setCells(Cell[][] cells) {
		this.cells=cells;
	}

	public Cell[][] getCells() {
		return cells;
	}

}