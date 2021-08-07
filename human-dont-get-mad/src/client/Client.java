package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import game.Log;
import game.LogController;


/**
* <h1>Client</h1>
* <p>This is a demo CLI client for reference when hooking up the GUI.
* You can use this code mainly for the launcher. (This basically needs to be executed
* when pressing the connect button)</p>
* <b>Note:</b> Please delete this class once the code is implemented and
* do not write test cases for this since it is going to get removed anyway!
*
* @author  Paul Braeuning
* @author Tim Menzel
* @version 0.1
* @since   2021-07-23
*/
public class Client extends Thread{
	private Socket socket;
	private ClientController controller;
	private Scanner in;
	private PrintWriter out;
	private String bufferIn;
	public String serverAdress = "";
	public Integer port = 0;
	private GameWindow gamewindow;
	private Launcher launcher;
	
	
	public Client(ClientController controller, GameWindow gamewindow, Launcher launcher) {
		this.controller = controller;
		this.gamewindow = gamewindow;
		this.launcher = launcher;
		
	}
	
	@Override
	public void run() {
		requestConnection();
	}
	
	
	public void requestConnection() {
		//Client client = new Client(controller);
		start(serverAdress, port);
	}
	
	Main main = new Main();
	//start client socket and listen for new lines which are getting decoded by controller
	private void start(String host, Integer port) {
		try {
			socket = new Socket(host, port);
			LogController.log(Log.INFO, "Connected to server: " + socket);
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			
		
			gamewindow.setVisible(true);
			
			while (in.hasNextLine()) {
				bufferIn = in.nextLine();
				LogController.log(Log.TRACE, "RX: " + bufferIn);
				controller.decoder(bufferIn);
			}
		}
		catch (Exception e) { 
			
			LogController.log(Log.ERROR, e.toString());
			}
		
		finally {
			LogController.log(Log.INFO, "Disconnected from server: " + socket);
			launcher.setVisible(true);
			gamewindow.setVisible(false);
		}
	}
	
	//transmission / output to server
	public void out(String data) {
		out.println(data);
		LogController.log(Log.TRACE, "TX: " + data);
	}
}
