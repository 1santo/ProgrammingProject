package environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;
import server.Server;
import game.AutomaticSnake;

public class LocalBoard extends Board{
	
	private static final int NUM_SNAKES = 3;
	private static final int NUM_OBSTACLES = 3;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 1;

	public LocalBoard() {		//
		for (int i = 0; i < NUM_SNAKES; i++) {
			Snake snake = new AutomaticSnake(i, this);
			addSnake(snake);	//n esquecer de adicionar a lista do board
		}
		
		for (int i = 0; i < NUM_OBSTACLES; i++) {
			Obstacle obs = new Obstacle(this);
			//obs.doInitialPositioning();
		}
	}

	// synchronization in cell
	
	public void init() {
		// TODO
		// Start Threads
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
