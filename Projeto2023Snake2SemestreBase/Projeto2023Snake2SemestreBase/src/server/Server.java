package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import remote.ActionResult;

public class Server {
	public static final int SERVER_PORT =8088;
	public static final int NUM_CONNECTS=30;
	
	private Board board;//
	private ServerSocket server;//
	private boolean gameOver;
	private ActionResult action;
	
	
	// TODO
	public Server(Board board) {
		this.board= board;
		runServer();
		System.out.println("aqui nao aparece nd pois o run ta smp while true");
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
		finally {
			
		}
	}

	//esperar por conexoes e' criar socket aceite pelo server
	//e dps ter uma thread dedicadaa a gerir conexoes
	private void waitForConnections() throws IOException {
		System.out.println("______________________________-> Waiting for connections....<-___________________");
		Socket connectionSocket = server.accept(); ///Espera!!!
		
		//Create a new connection handler -> vai fzr a gestao da conexao de 1 cliente, thread dedicada para aquela conexao, aquela e' o connect
		ConnectionHandler handler = new ConnectionHandler(connectionSocket);
		handler.start();//cria  thread e começa a gestao da conexao com o start
		
		System.out.println("-------------------------->New client connection: "+ connectionSocket.getInetAddress().getHostName()); //endereço ip e o nome do host
		
	}

	private class ConnectionHandler extends Thread{//classe anonima pra gerir socket
		private Socket connection;
		//LocalBoard boardLimpo1;
		//LocalBoard boardSujo1=board;
		//private ObjectInputStream boardSujo;
		//private ObjectOutputStream boardLimpo;
		private PrintWriter boardLimpo;
		private Scanner boardPotencialmenteSujo; //pode tar sujo ou nao, depende das coordenadas q cliente envia
		
		public ConnectionHandler(Socket connection) {
			this.connection=connection;
		}
		
		
		//para ir buscar oq ta a ser comunicado entre canais
		private void getStreamChannel() throws IOException {
			//Atualizacao/limpeza do board aqui
			boardLimpo=new PrintWriter(connection.getOutputStream(),true);
				//true-> autoflush= toda vez q gerar 1 msg (ActionResult) n vai esperar o buffer ficar cheio pra enviar, mas envia sim automaticamente
				//no ObjectOutputStream preciso mesmo de indicar ***
				
			//Aqui leitura do board recebido
			boardPotencialmenteSujo=new Scanner(connection.getInputStream());
		}
		
		
		//enquanto jogo nao termina vai haver esta comunicacao
		private void processConnection() throws IOException{ //pode n ter nd pra ler/mandar

			do {
				//leitura de potencial coordenada do board suja q pode ter obstaculos cobras mortas etc
				String pos;
				//BoardPosition position;
				
				//try {
				
					pos=boardPotencialmenteSujo.nextLine(); //deteta separador automaticamente
					//position = (BoardPosition)boardPotencialmenteSujo.readObject(); //readObject lanca excecao
					//excecao ClassNotFoundException e
					
					//aqui se o cliente entretanto mover //Espera!! posso criar uma thread dedicada pra fzr esta espera
					//action = handlePosition(position);
					action = handlePosition(pos);

					//aqui tem q fzr eco do board pros outros clientes
					
					boardLimpo.println(action.toString());
					//boardLimpo.writeObject(action);
					//boardLimpo.flush(); //***
					System.out.println("TESTE do board que cliente mandou ");
					
					
			/*	} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
				
				if (action.isGameEnded()) {
					gameOver=true;
                  }
				
			} while(!gameOver); //enquanto nao ha gameOver
		}
		
		
	private ActionResult handlePosition(String position) {
	//private ActionResult handlePosition(BoardPosition position) {

		//new
		String [] coordinates=position.split(",");
		int x=Integer.parseInt(coordinates[0]);
		int y=Integer.parseInt(coordinates[1]);
		BoardPosition pos=new BoardPosition(x,y);
		
		
		Cell cell=board.getCell(pos);
		boolean wasSuccessful=false;
			
		if (cell.isOcupiedByObstacle()) {
			System.out.println("Obstacle to remove");
			cell.removeObstacle();
			wasSuccessful = true;
		} else if (cell.isOcupiedBySnake() && cell.getOcuppyingSnake().wasKilled()) {
			System.out.println("Snake to remove");
			cell.removeSnake(cell.getOcuppyingSnake());
			wasSuccessful = true;
		}
			boolean gameEnded = board.isFinished();
			board.setChanged();
			System.out.println("_________________________________________teste");
		return new ActionResult(wasSuccessful, gameEnded);
	}
		
	
	private void closeConnection() {
		try {
			if(boardPotencialmenteSujo !=null)//quando fecho o board?
				boardPotencialmenteSujo.close();
			if(boardLimpo!=null)
				boardLimpo.close();	
			if(connection !=null)
				connection.close();
		}catch (IOException e) {
				e.printStackTrace();
		}
	}
		
		
		@Override
		public void run() {
			//1. getposicoes e comunicacao
			try {
				getStreamChannel();
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
