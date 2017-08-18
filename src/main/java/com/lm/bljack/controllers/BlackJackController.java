package com.lm.bljack.controllers;

import java.util.ArrayList;
import java.util.Stack;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lm.bljack.models.GameStatus;
import com.lm.bljack.models.Hand;
import com.lm.bljack.models.Shuffle;




@Controller
@RequestMapping( {"/blackjack"} )
public class BlackJackController 
{

	// Set up for the Game.START  ?? use enumerations ??
	boolean isGameOver = false;
	boolean isPlayOver = false;
	boolean isPlayStart = false;
	boolean isGameStart = true;

	// Yes, this is the Deck
	// public int[] deck;
	// Create Hands(s) for Player / Dealer - there is a reason why these are <String> not <Integer>
	public ArrayList<String> playerHand = new ArrayList<String>();
	public ArrayList<String> dealerHand = new ArrayList<String>();
	// Counts cards dealt (array index)
	private int i = 0; 
	
 
	// Cards are listed by 'value' (the dumb way, no OOP) though you can tell '10' & Face cards apart
	public int[] deck = {2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,
    		     		 99,99,99,99,
    		     		 10,10,10,10,10,10,10,10,10,10,10,10,
    		     		 11,11,11,11};	
    
       
	public BlackJackController()  { 		    		
	    // Shuffle it 	
		Shuffle shfl = new Shuffle();
		shfl.shuffleArray(deck);};	    	
		
		
	// model.addAttribute("showUnaryOperator",  numStack.size() > 0);
	
	// Make visible a single button: "START GAME"
	@GetMapping("")    
	public String index(Model model)   {         
			model.addAttribute("isGameStart", isGameStart);
			model.addAttribute("isPlayStart", isPlayStart);
			model.addAttribute("points_p", " - ");
			model.addAttribute("points_d", " - ");
			return "bljack/front";}
	
					
	// Pressing 'START GAME' 
	//    --> 'START GAME' button hides (turns into 'EXIT GAME')
	//    --> 'HIT' & 'STAND' appear 
	//    -->  Player & Dealer & some other some reference info 
	@PostMapping("/gamestart")
	public String dealCardsToPlayer(Model model)   
	{   // ?? funny way to do it...
		isGameStart = false;
		isPlayStart = true;
		model.addAttribute("isGameStart", isGameStart);
		model.addAttribute("isPlayStart", isPlayStart);
		
		Hand handPlayer = new Hand();
        Hand handDealer = new Hand();
		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHand);
        handPlayer.loadOneCard(String.valueOf(deck[i + 2]), playerHand);
        handDealer.loadOneCard(String.valueOf(deck[i + 1]), dealerHand);
        
        // Pass to form: total points & list of cards for Player/Dealer(+ the Hole) after the first round
        String temp="";
        temp = String.valueOf(handPlayer.getTotalPoints(playerHand)) + "   " +
        	   String.valueOf(handPlayer.getMyCards(playerHand));
        model.addAttribute("points_p", temp);
        
        temp = String.valueOf(handDealer.getTotalPoints(dealerHand)) + "   " +
         	   String.valueOf(handDealer.getMyCards(dealerHand)) + "<HOLE>";        
        model.addAttribute("points_d", handDealer.getTotalPoints(dealerHand));
        
                
        // Check for terminal event(s): Player goes Blackjack (he cannot go Bust at this point)
        GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHand));
        model.addAttribute("is_blackjack", status.isBlackJack());
              
        // return "redirect:/blackjack";    
		return "bljack/deal-form-ST"; 
	}	
	
	
	
	
	// Get that number from the PUSH and place in the Stack
	@PostMapping("/hit")
	public String dealCardToPlayer() 
	{
		//numStack.push(numb);
		// Related to restarting a page  where stuff will be pushed again just for the refresh
        return "redirect:/blackjack";
	}

	
	@PostMapping("/stand")
	public String activePlayStops(Model model)   
	{  
		//numStack.clear();
		return "redirect:/blackjack"; 
	}
}


