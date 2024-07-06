package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;
import remote.ActionResult;

public class Server {
	public static final int SERVER_PORT =8080;
	public static final int NUM_CONNECTS=30;
	
	private LocalBoard board;//
	private ServerSocket server;//
	private boolean gameOverTemp;
	private ActionResult action;
	
	// TODO
	public Server(Board board) {
		this.board=(LocalBoard) board;
		runServer();
		System.out.println("SERVIDORRRRRRRRRRRRRRRRRRRRRRRRRRRRRR A CORRERRRRRRRR-----------------");
	}
	
	private void runServer() {
		//1. create serversocket
		try {
			server= new ServerSocket(SERVER_PORT,NUM_CONNECTS); //port need to be grater than 1024
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
		LocalBoard boardLimpo1;
		LocalBoard boardSujo1=board;
		ObjectInputStream boardSujo;
		ObjectOutputStream boardLimpo;
		
		public ConnectionHandler(Socket connection) {
			this.connection=connection;
}
		
		
		//para ir buscar o board
		private void processConnection() throws IOException{ //pode n ter nd pra ler/mandar

			do {
				//leitura do board sujo com obstaculos cobras mortas etc
				BoardPosition position;
				try {
					position = (BoardPosition)boardSujo.readObject(); //readObject lanca excecao
					
					//aqui se o cliente entretanto mover //Espera!! posso criar uma thread dedicada pra fzr esta espera
					action = handlePosition(position);
					
					//aqui tem q fzr eco do board pros outros clientes
					boardLimpo.writeObject(action);
					boardLimpo.flush(); //***
					System.out.println("TESTE do board que cliente mandou ");
					
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				if (action.isGameEnded()) {
					gameOverTemp=true;
                  }
				
			} while(!gameOverTemp); //enquanto nao ha gameOver
		}
		
		private void getBoardUpdates() throws IOException {
			//Atualizacao/limpeza do board aqui
			boardLimpo=new ObjectOutputStream(connection.getOutputStream());//aqui nao leva true
				// no PrintWriter tinha true-> autoflush= toda vez q escrever 1 msg n vou esperar o buffer ficar cheio pra enviar, mas eu envio sim automaticamente
				//neste preciso mesmo de indicar ***
				
			//Aqui leitura do board recebido
			boardSujo=new ObjectInputStream(connection.getInputStream());
		}
		
	private ActionResult handlePosition(BoardPosition position) {
		Cell cell=board.getCell(position);
		boolean wasSuccessful=false;
		
			if (cell.isOcupiedByObstacle()) {
			cell.removeObstacle();
			wasSuccessful = true;
			} else if (cell.isOcupiedBySnake() && cell.getOcuppyingSnake().wasKilled()) {
			cell.removeSnake(cell.getOcuppyingSnake());
			wasSuccessful = true;
			}
		
		boolean gameEnded = board.isFinished();
		
		return new ActionResult(wasSuccessful, gameEnded);
	}
		
		private void closeConnection() {
			try {
				if(boardSujo !=null)
					//tenho que...
				if(boardLimpo!=null)
					//hmmm
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
