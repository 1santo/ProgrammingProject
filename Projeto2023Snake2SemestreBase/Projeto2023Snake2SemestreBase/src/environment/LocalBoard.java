package environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CyclicBarrier;
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
	private static final int NUM_OBSTACLES = 6;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;

	private int random = ThreadLocalRandom.current().nextInt(1, 10);//

	public LocalBoard() {		//
		for (int i = 0; i < NUM_SNAKES; i++) {
			Snake snake = new AutomaticSnake(i, this);
			addSnake(snake);	//isto e' n esquecer de adicionar a lista do board
		}
		
		for (int i = 0; i < NUM_OBSTACLES; i++) {
			Obstacle obs = new Obstacle(this);
			addGameElement(obs);
			System.out.println("ULTIMO OBSTACULO"+obstacles.getLast());
		//	addObstacles(obs);
		
		}
		
		//for number of times goals got captured until they reach 9
		//criar goals
        for(int i=0;i<5;i++) {
        Goal goal2 = new Goal(this,random);
        setGoalPosition(goal2,goal2.pos);
        }

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
		//send the message to the 3 obstacle movers at once, at the same time
		//to avoid one receiving one first
		
		CyclicBarrier barrier=new CyclicBarrier(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		for (int i = 0; i < NUM_OBSTACLES; i++) {
			for(Obstacle obs: obstacles) {
				executor.submit(new ObstacleMover(obs,this,barrier));
			}
		}
		executor.shutdown();
		setChanged();
	}

	
	
	public void removeSnake(BoardPosition position) {
//		TODO
	}

	
	public int getNUM_OBSTACLES() {
		return NUM_OBSTACLES;
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
