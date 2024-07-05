package game;

import java.util.LinkedList;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

public abstract class GameElement { //abstrata pros outros herdarem
	protected LinkedList<Cell> gameElements = new LinkedList<Cell>();//
	protected int size;//
	private Board board;//
	private BoardPosition init; //
	//protected abstract void doInitialPositioning(); //tds os elementos devem ter forma
							//de iniciar a posicao dependendo doq sao
	
	public int getSize() {
		return size;
	}
	
	public int getCurrentLength() {
		return gameElements.size();
	}
	
	public LinkedList<Cell> getCells() {
		return gameElements;
	}
	
}
