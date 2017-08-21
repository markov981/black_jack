package com.lm.bljack.models;

import java.util.ArrayList;

import org.springframework.ui.Model;

public class GameStatus {

	private int pointsTotal;
	public GameStatus(int points) { pointsTotal = points; }

	
	// Is a Bust
    public boolean isBust() {
    	if(pointsTotal > 21) return true;
    	return false;}

    
	// Is 21 in total
    public boolean is21Total(ArrayList<String> hand) {
    	int total = 0;
    	
    	for(String itrt : hand) 
    		total += Integer.valueOf(itrt);
    	if(total == 21) return true;
    	
    	return false;   	
    }
    
    
	// Is BlackJack
    public boolean isBlackJack(ArrayList<String> hand) {
    	int checkA = 0; int checkF = 0; int total = 0;
    	
    	// Since the card of '10' == 99 itrt.equals("10") will select Face cards only 
    	for(String itrt : hand) 
    	{
           if(itrt.equals("11")) {
        	   checkA++;
        	   break; }
    	}
    	
        for(String itrt : hand) 
        {    
    	   if(itrt.equals("10")) {
    		   checkF++;
    		   break;}
        }
        
    	for(String itrt : hand) 
    		total += Integer.valueOf(itrt);
    	   		   
    	if ((checkA + checkF) == 2 && total == 21) return true; 
    	
    	return false;}
 
    
	// BlackJack message 
    public String isEmptyMsg()        { return "";}
    
	// BlackJack message 
    public String is21Msg()           { return "In case you didn't notice, you've got 21 points.";}
    
	// Busted message
    public String isBustMsg()          { return "You are BUSTED. What else is new...";}

	// Press STAND message
    public String isStandMsg()         { return "Please press STAND button for the payoff amount.";}

	// Start New Play message
    public String newPlayMsg()         { return "The game continues. Check your account balance and start a new play. If you think it's a good idea...";}   
       
	// Play outcome message
    public String isPlayOutcomeMsg(String playOutcome)   { return "Play outcome: " + playOutcome;}

	// Play outcome win/loose
    public String isAccountUpdateMsg(String accOutcome)  { return "You managed to " + accOutcome;}

    
	// Out-of-money message 1
    public String isOutOfMoneyMsg1()  { return "You are out of money, dear player.";}
	// Out-of-money message 2
    public String isOutOfMoneyMsg2()  { return "We cannot even begin to tell you how sorry we are. Really...";}    
    
    
	// Out-of-cards message 
    public String isOutOfCardsMsg()  { return "We ran out of cards. Game's over.";}  

	// No funds to accept the Even Money offer message 
    public String isEvenMoneyMsg(boolean suffice)  { 
    	if (!suffice) return "You don't have funds to accept the Even Money offer. The offer is withdrawn.";
    	              return "";
    }   
}
