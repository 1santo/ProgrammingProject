package remote;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import environment.BoardPosition;
import environment.LocalBoard;

public class Client {

	private InetAddress ipAddress;
	private int port;
	//private LocalBoard boardLimpo1;
	//private LocalBoard boardSujo1;
	private ActionResult action;
	ObjectInputStream boardSujo;
	ObjectOutputStream boardLimpo;
	
	private Socket connectionSocket;
	
	public Client (InetAddress byName, int i) {
		this.ipAddress=byName;
		this.port=i;
		
	}
	public void runClient() {
		//1. connect to server
		try {
			connectionSocket= new Socket(ipAddress,port);
			
			//2. get board updates - communication
			getBoardUpdates();
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
		
	private void closeConnection() {
		try {
			if(boardSujo !=null)//quando fecho o board?
				boardSujo.close();
			if(boardLimpo!=null)
				boardLimpo.close();
			if(connectionSocket !=null)
				connectionSocket.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
		
		
		//para ir buscar os canais
		private void processConnection(){
			try {
			 Random random = new Random();

			 while (true){
				 BoardPosition randomPosition = new BoardPosition(random.nextInt(30), random.nextInt(30));

					boardLimpo.writeObject(randomPosition);
					boardLimpo.flush();
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
			} 

			
			/*
			//Board
			for (int i = 0; i < 1000; i++) {//em vez de 1000 tam do board
				//outro for por ser board, x e y
				//escrita
				//acao do boardLimpo
				System.out.println("enviado  o board limpo");
				//leitura do eco
				System.out.println("leitura do board sujo do cliente");
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//um tempinho pra visualizar
				
			}
			//finish connection
			action.setGameEnded(true);
			
			*/
		}
		
		private void getBoardUpdates() throws IOException {
			//Atualizacao/limpeza do board aqui
			boardLimpo=new ObjectOutputStream(connectionSocket.getOutputStream());//aqui nao leva true
				// no PrintWriter tinha true-> autoflush= toda vez q escrever 1 msg n vou esperar o buffer ficar cheio pra enviar, mas eu envio sim automaticamente
				//neste preciso mesmo de indicar ***
			boardLimpo.flush();
			//Aqui leitura do board recebido
			boardSujo=new ObjectInputStream(connectionSocket.getInputStream());
	
		}
		
		
	
	public static void main(String [] args) throws UnknownHostException {
		
		Client client= new Client(InetAddress.getByName("localhost"), 8088); //127.0.0.1 e o endereco do localhost
		client.runClient();
		System.out.println("HAAAAALLOOOOO CLIENTE!!!!!!!!");
	} 
}

