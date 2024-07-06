package game;

import java.util.concurrent.ThreadLocalRandom;

import environment.Board;
import environment.LocalBoard;

public class Goal extends GameElement  {
	private int value;
	public static final int MAX_VALUE=9;
	private int random = ThreadLocalRandom.current().nextInt(1, 10);//
	public Goal( Board board2, int value) {
		super(board2);
		this.value=1;//
		doInitialPositioning();//
	}
	public int getValue() {
		return value;
	}
	
	//se for maior que 10 nao incrementa ne
	public void incrementValue(){
		if(value<MAX_VALUE) {//
			synchronized(this) {
			value++;
			} //cobras vao interagir com isto
		}//
		else System.out.println("Valor maximo atingido");
		;//
		
	}

	//huh?
	public int getGoalValue() {
		return value;
	}
	
	public void setGoalValue(int value) {
		this.value=value;
	}
	
	@Override
	protected void doInitialPositioning() {
		
		pos=board.getRandomPosition();
		System.out.println(this+" is Goal em: "+ pos+" | classGoal");
		try {
			board.getCell(pos).setGameElement(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
