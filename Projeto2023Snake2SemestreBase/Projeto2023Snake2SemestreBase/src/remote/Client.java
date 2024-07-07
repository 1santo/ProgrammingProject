package remote;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;

public class Client {

	private InetAddress ipAddress;
	private int CLIENT_PORT;
	//private LocalBoard boardLimpo1;
	//private LocalBoard boardSujo1;
	private ActionResult action;
	//private ObjectInputStream boardSujo;
	//private ObjectOutputStream boardLimpo;
	private Scanner boardLimpo;
	private PrintWriter boardPotencialmenteSujo;
	private Socket connectionSocket;
	private boolean GameOver=false;
	
	public Client (InetAddress byName, int i) {
		this.ipAddress=byName;
		this.CLIENT_PORT=i;
		
	}
	public void runClient() {
		//1. connect to server
		try {
			connectionSocket= new Socket(ipAddress,CLIENT_PORT);
			
			//2. get board updates - communication
			getStreamChannel();
			//3. process connections - changes in boards on both sides
			processConnection();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//4. close connection
			closeConnection();
		}
	}
	
	
	//inicializar os canais com a connectionsocket
	private void getStreamChannel() throws IOException {
		//Atualizacao/limpeza do board aqui
		boardPotencialmenteSujo=new PrintWriter(connectionSocket.getOutputStream(),true);//aqui nao leva true
		//true-> autoflush= toda vez q gerar 1 msg (ActionResult) n vai esperar o buffer ficar cheio pra enviar, mas envia sim automaticamente
		//no ObjectOutputStream preciso mesmo de indicar ***
		//boardLimpo.flush();
		
		
		//Aqui leitura do board recebido
		boardLimpo=new Scanner(connectionSocket.getInputStream());

	}
		
	private void closeConnection() {
		try {
			if(boardPotencialmenteSujo !=null)//quando fecho o board?
				boardPotencialmenteSujo.close();
			if(boardLimpo!=null)
				boardLimpo.close();
			if(connectionSocket !=null)
				connectionSocket.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
		
		
		//para ir buscar oq ta a ser comunicado entre canais
		private void processConnection(){
		
			try {
			//new
			while(!GameOver) {
			Random random = new Random();
			String randomX = String.valueOf(random.nextInt(Board.WIDTH));
			String randomY = String.valueOf(random.nextInt(Board.HEIGHT));
			String coordinates = "("+randomX+","+randomY+")";
			boardPotencialmenteSujo.println(coordinates);
			
			
			//ler do server
			//new
			if(boardLimpo.hasNextLine()) {
				
			String seeRespostaActionServer=boardLimpo.nextLine();
			action=ActionResult.fromString(seeRespostaActionServer);
			
			//\\
				if(action.wasSuccessful()){
					System.out.println("Elemento removido na pos: "+coordinates);
				} else{
					System.out.println("Nada removido: "+coordinates);
				}

				if(action.isGameEnded()){
					GameOver=true;
					System.out.println("Game over");
					break;
				}
			}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //um tempinho pra ir removendo 
			}
			
			}finally {
				closeConnection();
			}
			
			/*try {
				Random random = new Random();

			 while (true){
				 BoardPosition randomPosition = new BoardPosition(random.nextInt(30), random.nextInt(30));
				 
				 
				 	boardLimpo.writeObject(randomPosition);
					boardLimpo.flush(); //***
					ActionResult action =(ActionResult)boardSujo.readObject();

					 if(action.wasSuccessful()){
						 System.out.println("Elemento removido na pos: "+randomPosition);
					 } else{
						 System.out.println("Nada removido: "+randomPosition);
					 }

					 if(action.isGameEnded()){
						 System.out.println("Game over");
						 break;
					 }

					 Thread.sleep(1000); //um tempinho pra ir removendo 
 
			 }
			}catch(SocketException e ) {	 
			 } catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				closeConnection(); //ter a certeza q fecha
			} */

		}

	
	public static void main(String [] args) throws UnknownHostException {
		
		Client client= new Client(InetAddress.getByName("localhost"), 8084); //127.0.0.1 e o endereco do localhost
		client.runClient();
		System.out.println(client+" just joined*********************************");
	} 
}

