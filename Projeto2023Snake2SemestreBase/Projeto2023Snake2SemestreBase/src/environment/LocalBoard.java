package environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;
import server.Server;
import game.AutomaticSnake;

public class LocalBoard extends Board{
	
	private static final int NUM_SNAKES = 3;
	private static final int NUM_OBSTACLES = 3;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 1;

	private int random = ThreadLocalRandom.current().nextInt(1, 10);//

	public LocalBoard() {		//
		for (int i = 0; i < NUM_SNAKES; i++) {
			Snake snake = new AutomaticSnake(i, this);
			addSnake(snake);	//n esquecer de adicionar a lista do board
		}
		
		for (int i = 0; i < NUM_OBSTACLES; i++) {
			Obstacle obs = new Obstacle(this);
			obs.doInitialPositioning();
		//	addObstacles(obs);
			//addGameElement(obs);
		}
		
		//criar goals
		Goal goal = new Goal(this,random);
		BoardPosition goalPos = getRandomPosition();
        setGoalPosition(goalPos);
        addGameElement(goal); 
        System.out.println("Goal of "+goal.getGoalValue()+"pos: "+goalPos);
        setChanged();
	}

	// synchronization in cell
	
	public void init() { //
		for(Snake s:snakes) {
			Thread snakey = new AutomaticSnake(s.getIdentification(), this);
			snakey.start();
			
			//tempo pra esperar pra que faca o run da thread pra ver onde ficou
			/*
			try {
				Thread.sleep(1500); //10000
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}*/
			System.out.print("Snake created: "+s.getIdentification()+"\n");
			
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		for (int i = 0; i < NUM_OBSTACLES; i++) {
			for(Obstacle obs: obstacles) {
				executor.submit(new ObstacleMover(obs,this));
			}
		}
		executor.shutdown();
		setChanged();
	}

	
	
	public void removeSnake(BoardPosition position) {
//		TODO
	}



	// Ignore these methods: only for remote players, which are not present in this project
	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}



}
