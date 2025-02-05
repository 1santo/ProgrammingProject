package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import remote.ActionResult;

public class Server {

	public static final int SERVER_PORT =8081;
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
	}

	//esperar por conexoes e' criar socket aceite pelo server
	//e dps ter uma thread dedicadaa a gerir conexoes
	private void waitForConnections() throws IOException {
		System.out.println("______________________________-> Waiting for connections....<-___________________");
		Socket connectionSocket = server.accept(); ///Espera!!!
		
		//Create a new connection handler -> vai fzr a gestao da conexao de 1 cliente, thread dedicada para aquela conexao, aquela e' o connect
		ConnectionHandler handler = new ConnectionHandler(connectionSocket);
		handler.start();//cria  thread e come�a a gestao da conexao com o start
		
		System.out.println("-------------------------->New client connection: "+ connectionSocket.getInetAddress().getHostName()); //endere�o ip e o nome do host
		
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
				//boardLimpo.flush();

			
			//Aqui leitura do board recebido
			boardPotencialmenteSujo=new Scanner(connection.getInputStream());
		}
		
		
		//enquanto jogo nao termina vai haver esta comunicacao
		private void processConnection() throws IOException{ //pode n ter nd pra ler/mandar

			while(!gameOver) {
				//leitura de potencial coordenada do board suja q pode ter obstaculos cobras mortas etc
				String pos;
				BoardPosition position;
				System.out.println("OI");
				//try {
					//boardPotencialmenteSujo.useDelimiter("\\),\\(");
					
				pos=boardPotencialmenteSujo.nextLine(); //deteta separador automaticamente
				//position = (BoardPosition)boardPotencialmenteSujo.readObject(); //readObject lanca excecao
				//excecao ClassNotFoundException e
				System.out.println(pos);
				//position=new BoardPosition(, );
						
				System.out.println("TETS");
				position = BoardPosition.fromString(pos);
				System.out.println("oi"+position);
						
				//action = handlePosition(position);
				
				//aqui se o cliente entretanto mover //Espera!! posso criar uma thread dedicada pra fzr esta espera
				//action = handlePosition(position);
				Thread t = new Thread(new Runnable () {
					public void run() {
						
						//System.out.println("______887868576THREAD VAI TRATAR DAQUELA CELULA");
						action = handlePosition(position);
						//System.out.println("____686THREAD TRATOU DAQUELA CELULA");
						//aqui tem q fzr eco do board pros outros clientes
						boardLimpo.println(action.toString()); //problematic
						//boardLimpo.writeObject(action);
						//boardLimpo.flush(); //***
						System.out.println("TESTE do que cliente mandou ");
										
						if (action.isGameEnded()) {
							gameOver=true;
								System.out.println("XXXXXXXXXXXXXXXXXX_GAME OVER_XXXXXXXXXXXXXXXXXXX");
						}
					}			
				});
				t.start();
				
				
			}
			/*	} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */

		}
		
	
		//metodo para a thread
		private ActionResult handlePosition(BoardPosition position) {
			//private ActionResult handlePosition(BoardPosition position) {
			
			//este metodo acede a varias celulas ao mesmo tempo
			//meter aqui por cada celula 1 cadeado
			//ha possibilidade de celulas terem ao msm tempo o lock?
						
				Cell cell=board.getCell(position);
				boolean wasSuccessful=false;
					Lock lock=cell.getLock();
				if (cell.isOcupiedByObstacle()) {
					System.out.println("Obstacle to remove");
					lock.lock();
					try {
						cell.removeObstacle(); //coordenacao?
					}finally {
						lock.unlock();
					}
					System.out.println("_______________Obstacle Actually Removed>>>"+cell.getGameElement());
					wasSuccessful = true;
				} else if (cell.isOcupiedBySnake() && cell.getOcuppyingSnake().wasKilled()) {
					System.out.println("Snake to remove");
					lock.lock();
					try {
						cell.removeSnake(cell.getOcuppyingSnake()); //esta parte coordenacao
					}finally {
						lock.unlock();
					}
					
					System.out.println("_______________Snake Actually Removed>>>"+cell.getOcuppyingSnake());
					board.setChanged();
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
