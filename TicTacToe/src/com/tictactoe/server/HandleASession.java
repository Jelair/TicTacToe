package com.tictactoe.server;

import java.io.*;
import java.net.*;

import com.tictactoe.constants.TicTacToeConstants;

//Define the thread class for handling a new session for two players
public class HandleASession implements Runnable, TicTacToeConstants {
	private Socket player1;
	private Socket player2;
	
	//Create and initialize cells
	private char[][] cell = new char[3][3];
	
	private DataInputStream fromPlayer1;
	private DataOutputStream toPlayer1;
	private DataInputStream fromPlayer2;
	private DataOutputStream toPlayer2;
	
	//Continue to play
	private boolean continueToPlay = true;
	
	/**Construct a thread*/
	public HandleASession(Socket player1, Socket player2){
		this.player1 = player1;
		this.player2 = player2;
		
		// Initialize cells
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				cell[i][j] = ' ';
	}
	
	@Override
	public void run() {
		
		try {
			//Create data input and output streams
			fromPlayer1 = new DataInputStream(player1.getInputStream());
			toPlayer1 = new DataOutputStream(player1.getOutputStream());
			fromPlayer2 = new DataInputStream(player2.getInputStream());
			toPlayer2 = new DataOutputStream(player2.getOutputStream());
			
			//Write anything to notify player 1 to start
			//This is just to let player1 know to start
			toPlayer1.writeInt(1);
			
			//Continuously serve the players and determine and report
			//the game status to the players
			while(true){
				//Receive a move from player1
				int row = fromPlayer1.readInt();
				int column = fromPlayer1.readInt();
				cell[row][column] = 'X';
				
				//Check if Player1 wins
				if(isWon('X')){
					toPlayer1.writeInt(PLAYER1_WON);
					toPlayer2.writeInt(PLAYER1_WON);
					sendMove(toPlayer2, row, column);
					break; //Break the loop
				}else if(isFull()){ //Check if all cells are filled
					toPlayer1.writeInt(DRAW);
					toPlayer2.writeInt(DRAW);
					sendMove(toPlayer2, row, column);
					break; //Break the loop
				}else{
					//Notify player2 to take the turn
					toPlayer2.writeInt(CONTINUE);
					
					//Send player1's selected row and column to player2
					sendMove(toPlayer2, row, column);
				}
				
				//Receive a move from Player2
				row = fromPlayer2.readInt();
				column = fromPlayer2.readInt();
				cell[row][column] = 'O';
				
				//Check if Player2 wins
				if(isWon('O')){
					toPlayer1.writeInt(PLAYER2_WON);
					toPlayer2.writeInt(PLAYER2_WON);
					sendMove(toPlayer1, row, column);
					break; //Break the loop
				}else if(isFull()){ //Check if all cells are filled
					toPlayer1.writeInt(DRAW);
					toPlayer2.writeInt(DRAW);
					sendMove(toPlayer1, row, column);
					break; //Break the loop
				}else{
					//Notify player1 to take the turn
					toPlayer1.writeInt(CONTINUE);
					
					//Send player2's selected row and column to player1
					sendMove(toPlayer1, row, column);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**Send the move to other player*/
	private void sendMove(DataOutputStream out, int row, int column) throws IOException{
		out.writeInt(row); //Send row index
		out.writeInt(column); //Send column index
	}
	
	/**Determine if the cells are all occupied*/
	private boolean isFull(){
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				if(cell[i][j] == ' ')
					return false; //At least one cell is not filled
		
		//All cells are filled
		return true;
	}
	
	/**Determine if the player with the specified token wins*/
	private boolean isWon(char token){
		//Check all rows
		for(int i = 0; i < 3; i++)
			if(cell[i][0] == token && cell[i][1] == token && cell[i][2] == token){
				return true;
			}
		
		//Check all columns
		for(int j = 0; j < 3; j++)
			if(cell[0][j] == token && cell[1][j] == token && cell[2][j] == token){
				return true;
			}
		
		//Check major diagonal
		if(cell[0][0] == token && cell[1][1] == token && cell[2][2] == token){
			return true;
		}
		
		//Check subdiagonal
		if(cell[0][2] == token && cell[1][1] == token && cell[2][0] == token){
			return true;
		}
		
		//All checked, but no winner
		return false;
	}
}
