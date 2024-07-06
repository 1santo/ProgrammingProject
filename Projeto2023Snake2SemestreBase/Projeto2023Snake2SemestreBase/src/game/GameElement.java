package game;

import java.util.LinkedList;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

public abstract class GameElement { //abstrata pros outros herdarem
	protected LinkedList<Cell> gameElements = new LinkedList<Cell>();//
	protected int size;//
	protected Board board;//
	protected BoardPosition init; //
	public BoardPosition pos;//

	protected abstract void doInitialPositioning(); //tds os elementos devem ter forma
							//de iniciar a posicao dependendo doq sao
	
	public GameElement(Board board){
		this.board=board;
	}
	
	protected int getSize() {
		return size;
	}
	
	protected int getCurrentLength() {
		return gameElements.size();
	}
	
	public LinkedList<Cell> getCells() { //protected pois
		return gameElements;
	}
	
}
