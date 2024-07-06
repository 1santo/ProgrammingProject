package server;

import environment.Board;
import environment.LocalBoard;

public class Server {
	public static final int SERVER_PORT =8080;
	private LocalBoard board;
	// TODO
	public Server(Board board) {
		this.board=(LocalBoard) board;
	}

}
