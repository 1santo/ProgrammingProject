package remote;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;
import gui.SnakeGui;
import server.Server;

public class Client {

	private InetAddress ipAddress;
	private int port;
	private LocalBoard boardLimpo;
	private LocalBoard boardSujo;
	private ActionResult action;
	
	private Socket connection;
	
	public Client (InetAddress byName, int i) {
		this.ipAddress=byName;
		this.port=i;
	}
	public void runClient() {
		//1. connect to server
		try {
			connection= new Socket(ipAddress,port);
			
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
			if(boardSujo !=null)
				//
			if(boardLimpo!=null)
				//
			if(connection !=null)
				connection.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
		
		
		//para ir buscar os canais
		private void processConnection(){
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
			action.setGamedEnded();
		}
		
		private void getBoardUpdates() throws IOException {
			//Atualizacao/limpeza do board aqui
			
			//true-> autoflush= toda vez q escrever 1 msg n vou esperar o buffer ficar cheio pra enviar, mas eu envio sim automaticamente
	
			//Aqui leitura do board recebido
	
		}
		
		
	
	public static void main(String [] args) throws UnknownHostException {
		Client client= new Client(InetAddress.getByName("localhost"), 8080); //127.0.0.1 e o endereco do localhost
		client.runClient();
	} 
}

