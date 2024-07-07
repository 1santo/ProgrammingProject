package gui;

import java.io.Console;
import java.io.IOException;

import javax.net.ssl.StandardConstants;

import environment.Board;
import environment.LocalBoard;
import server.Server;

public class Main {
	public static void main(String[] args) {
		LocalBoard board=new LocalBoard();
		SnakeGui game = new SnakeGui(board,600,0);
		game.init();
		// Launch server
		// TODO
		Server server = new Server((LocalBoard)board);
		System.out.println("______________THIS IS THE START OF THE SERVER____________"+server);
		
	}
}
