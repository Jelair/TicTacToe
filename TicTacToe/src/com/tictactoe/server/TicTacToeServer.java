package com.tictactoe.server;

import java.io.*;
import java.net.*;
import java.util.Date;

import com.tictactoe.constants.TicTacToeConstants;

public class TicTacToeServer implements TicTacToeConstants {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new TicTacToeServer();
	}

	public TicTacToeServer(){
		//Create a server socket
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(8000);
			System.out.println(new Date() + ":Server started at socket 8000");
			
			//Number a session
			int sessionNo = 1;
			
			//Ready to create a session for every two players
			while(true){
				System.out.println(new Date() + ": Wait for players to join session " + sessionNo);
				
				//Connect to player1
				Socket player1 = serverSocket.accept();
				System.out.println(new Date() + ": Player1 joined session " + sessionNo);
				System.out.println("Player1 's IP address" + player1.getInetAddress().getHostAddress());
				
				//Notify that the player is Player1
				new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);
				
				//Connect to player2
				Socket player2 = serverSocket.accept();
				System.out.println(new Date() + ": Player2 joined session " + sessionNo);
				System.out.println("Player2 's IP address" + player2.getInetAddress().getHostAddress());
				
				//Notify that the player is Player2
				new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);
				
				System.out.println(new Date() + ": Start a thread for session " + sessionNo);
				
				//Create a new thread for this session of two players
				
				
				//Start the new thread
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
