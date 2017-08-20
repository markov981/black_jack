package com.lm.bljack.models;

import java.util.ArrayList;

/* 
   Keeps account updated 
   Calculates Payoff 
*/ 
public class Account {
	
	private int account;
	public Account(int initialAmount)       {account = initialAmount;}
			
	public int  getAccountAmount()           { return account;}	
	public void setAccount(int account)      {this.account = account;}
	public int  getAccountFreeToBet(int bet) { return account - bet; }	
	public int  getWinLoss(int startAmount)  { return account - startAmount; }	
	

	// Define the play outcome 
	public String setPlayOutcome(int pointsPlayer, int pointsDealer, GameStatus statusP, GameStatus statusD,
			ArrayList<String> handArr, ArrayList<String> dealArr) {
		
			if      (statusP.isBust())                                                        return "bust";
			else if (statusP.isBlackJack(handArr) && statusP.isBlackJack(dealArr))            return "blj_blj";
			else if (statusP.isBlackJack(handArr) && !statusP.isBlackJack(dealArr))           return "blj_notblj";
			else if (pointsPlayer < 22 && pointsDealer < 22 && pointsPlayer < pointsDealer)   return "21u_more";		
			else if (pointsPlayer < 22 && pointsDealer < 22 && pointsPlayer == pointsDealer)  return "21u_same";
			else if (pointsPlayer < 22 && pointsDealer < 22 && pointsPlayer > pointsDealer)   return "21u_less";
			else if (pointsPlayer < 22 && statusD.isBust())                                   return "21u_bust";	
			
			return "bust";}

		
	// Calculate payoffs given bet amount & play outcome 
	// ?? The casting (double to int) robs Player of any cents he might get ?? 
	public int setAccountAfterPayoff(String type, int bet) {
		int  result = 0;
		
		// PLayer goes bust
		if(type.equals("bust"))		 			account -= bet;
		
		// Player gets 21-, Dealer gets more or same
		else if(type.equals("21u_more")) 		account -= bet;
		else if(type.equals("21u_same")) 		return account;		
				
		// Player gets 21-, Dealer goes bust or gets less than Player
		else if(type.equals("21u_bust")) 		account =  account + 2 * bet;
		else if(type.equals("21u_less")) 		account =  account + 2 * bet;
				
		// Player gets BlackJack, Dealer gets BlackJack or he doesn't
		else if(type.equals("blj_blj")) 		return account;
		else if(type.equals("blj_notblj")) 		account =  account + (int)(1.5 * bet);		
		
		return account;
	}
	
    
	// Generate play outcome messages
    public String generateOutcomeMsg(String accOutcome){ 
    	String message = "";
    	
		if  (accOutcome.equals("bust")) message = "You lost (busted).";
		else if (accOutcome.equals("blj_blj"))    message = "You and delear both have BlackJack - you keep your money.";
		else if (accOutcome.equals("blj_notblj")) message = "You've got BlackJack and the delear didn't - you win 1.5 of your bet.";
		else if (accOutcome.equals("21u_more"))   message = "You didn't go bust, but the dealer has more points: you loose you bet.";		
		else if (accOutcome.equals("21u_same"))   message = "You didn't go bust, but the dealer has the same number points: keep your money.";
		else if (accOutcome.equals("21u_less"))   message = "You didn't go bust and beat the dealer: you win twice the amount of your bet.";
		else if (accOutcome.equals("21u_bust"))   message = "You didn't go bust, but the dealer did: you win twice the amount of your bet.";
		
    	return message;}
    
	// Generate play win/loose messages
    public String generateWinLooseMsg(String betOutcome, int bet){ 
    	String message = ""; double temp = 0; 
    	
		if  (betOutcome.equals("bust")) 		  message = "You loose you bet of $" + bet;
		else if (betOutcome.equals("blj_blj"))    message = "You lost/won $0.";
		else if (betOutcome.equals("blj_notblj")) {temp = 1.5 * bet; message = "You won $" + temp + "(but we don't count pennies here...)"; }
		else if (betOutcome.equals("21u_more"))   message = "You loose you bet of $" + bet;		
		else if (betOutcome.equals("21u_same"))   message = "You lost/won $0.";
		else if (betOutcome.equals("21u_less"))   {temp = 2 * bet; message = "You win $" + temp;}
		else if (betOutcome.equals("21u_bust"))   {temp = 2 * bet; message = "You win $" + temp;}
		
    	return message;
    	
    }
}
