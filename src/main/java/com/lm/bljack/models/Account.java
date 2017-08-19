package com.lm.bljack.models;

/* Keeps account updated with respect to 
 *    - Bets
 *    - Payoff calculations
*/ 
public class Account {
	
	// ?? Use Money. Not worth it... ?? 
	private int account;

	public Account(int initialAmount) {account = initialAmount;}
		
	public int getAccountAmount()          {return account;}
	
	public int getAccountCash(int bet) 
		{account -= bet; 
		 return account - bet;}
	
	
	public int getWinLoss(int startAmount) { return startAmount - account; }	
	
	
	// Calculate payoffs given bet amount and game outcome 
	// ?? The casting (double to int) robs Player of any cents he might get ?? 
	public int setAccountAfterPayoff(String type, int bet) {
		
		// PLayer goes bust
		if(type.equals("bust"))
			return account - bet;

		
		// Player gets 21-, Dealer gets more or same
		else if(type.equals("21u_more"))
			return account - bet;

		else if(type.equals("21u_same"))
			return account;		
		
		
		// Player gets 21-, Dealer goes bust or gets less than Player
		else if(type.equals("21u_bust"))
			return account + 2 * bet;

		else if(type.equals("21u_less"))
			return account + 2 * bet;
		
		
		// Player gets BlackJack, Dealer gets BlackJack or he doesn't
		else if(type.equals("blj_blj"))
			return account;
		
		else if(type.equals("blj_notblj"))
			return account + (int)(1.5 * bet);		
		
		return account;
	}




}
