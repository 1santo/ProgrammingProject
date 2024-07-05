package game;

import environment.Board;
import environment.LocalBoard;

public class Goal extends GameElement  {
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;
	public Goal( Board board2) {
		this.board = board2;
	}
	public int getValue() {
		return value;
	}
	
	//se for maior que 10 nao incrementa ne
	public void incrementValue() throws InterruptedException {
		if(value<MAX_VALUE) {//
			value++;
		}//
		else return;//
		
	}

	//huh?
	public int getGoalValue() {
		return value;
	}
	
	//no outro
	public void captureGoal() { //
		try {
			incrementValue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
