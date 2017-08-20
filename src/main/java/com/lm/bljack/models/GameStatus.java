package com.lm.bljack.models;

import java.util.ArrayList;

import org.springframework.ui.Model;

public class GameStatus {

	private int pointsTotal;
	public GameStatus(int points) { pointsTotal = points; }

	
	// Is it a Bust?
    public boolean isBust() {
    	if(pointsTotal > 21) return true;
    	return false;}

    
	// Is it 21 in total?
    public boolean is21Total(ArrayList<String> hand) {
    	int total = 0;
    	
    	for(String itrt : hand) 
    		total += Integer.valueOf(itrt);
    	
    	return false;   	
    }
    
    
	// Is it a BlackJack?
    public boolean isBlackJack(ArrayList<String> hand) {
    	int checkA = 0; int checkF = 0;
    	
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
    		   
    	if ((checkA + checkF) == 2) return true; 
    	
    	System.out.println("checkA: " + checkA + "  checkF: " + checkF + " Return type: " + checkA + checkF);
    	
    	return false;}
    
    
	// If it's a BlackJack...
    public void isStopHitting(Model model, GameStatus status, ArrayList<String> hand) {
        model.addAttribute("isPlayStart", false);
        model.addAttribute("isStand", true);
        model.addAttribute("isNewPlay", false);
        model.addAttribute("msg", status.is21Msg());  
        model.addAttribute("msg1", status.isStandMsg());}
  
       
	// BlackJack message 
    public String is21Msg()           { return "In case you didn't notice, you've got 21 points.";}
    
	// Busted message
    public String isBustMsg()          { return "You are BUSTED. What else is new...";}

	// Press STAND message
    public String isStandMsg()         { return "Please press STAND button for payoffs & to start a new play.";}

	// Play outcome message
    public String isPlayOutcomeMsg(String playOutcome)   { return "Play outcome: " + playOutcome;}

	// Play outcome message
    public String isAccountUpdateMsg(String accOutcome)  { return "You managed to " + accOutcome;}
}
