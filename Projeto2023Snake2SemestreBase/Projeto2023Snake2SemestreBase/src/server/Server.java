package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import environment.Board;
import environment.LocalBoard;

public class Server {
	public static final int SERVER_PORT =8080;
	
	private LocalBoard board;//
	private ServerSocket server;//
	private boolean gameOverTemp;
	
	// TODO
	public Server(Board board) {
		this.board=(LocalBoard) board;
		runServer();
		System.out.println("SERVIDORRRRRRRRRRRRRRRRRRRRRRRRRRRRRR A CORRERRRRRRRR-----------------");
	}
	
	private void runServer() {
		//1. create serversocket
		try {//50 nr de conexoes
			server= new ServerSocket(SERVER_PORT,50); //port need to be grater than 1024
		while(true) {
			//2. waiting for connections
				waitForConnections();
		}
	} catch (IOException e) {
		e.printStackTrace();

	}			
	}

	private void waitForConnections() throws IOException {
		System.out.println("Waiting for connections....");
		Socket connection = server.accept(); ///Espera!!!
		
		//Create a new connection handler -> vai fzr a gestao da conexao de 1 cliente, thread dedicada para aquela conexao, aquela e' o connect
		ConnectionHandler handler = new ConnectionHandler(connection);
		handler.start();//cria  thread e começa a gestao da conexao com o start
		
		System.out.println("New client connection: "+ connection.getInetAddress().getHostName()); //endereço ip e o nome do host
		
	}

	private class ConnectionHandler extends Thread{
		private Socket connection;
		LocalBoard boardLimpo;
		LocalBoard boardSujo=board;
		
		public ConnectionHandler(Socket connection) {
			this.connection=connection;
}
		
		
		//para ir buscar o board
		private void processConnection(){
			do {
				//leitura do board sujo com obstaculos cobras mortas etc
				//aqui se o cliente entretanto mover //Espera!! posso criar uma thread dedicada pra fzr esta espera
				System.out.println("TESTE do board que cliente mandou ");

				
				//aqui tem q fzr eco do board pros outros clientes
				
			} while(!gameOverTemp); //enquanto nao ha gameOver
		}
		
		private void getBoardUpdates() throws IOException {
			//Atualizacao/limpeza do board aqui
			
					//true-> autoflush= toda vez q escrever 1 msg n vou esperar o buffer ficar cheio pra enviar, mas eu envio sim automaticamente
			
			//Aqui leitura do board recebido
			
		}
		
		private void closeConnection() {
			try {
				if(boardSujo !=null)
					//tenho que...
				if(boardLimpo!=null)
					//
				if(connection !=null)
					connection.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			//1. getBoard
			try {
				getBoardUpdates();
				//2. processConnection
				processConnection();
			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				//3. closeConnection
				closeConnection();
			}
		}
	}
}
