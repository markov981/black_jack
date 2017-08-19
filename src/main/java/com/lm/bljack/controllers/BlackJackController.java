package com.lm.bljack.controllers;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lm.bljack.models.Account;
import com.lm.bljack.models.GameStatus;
import com.lm.bljack.models.Hand;
import com.lm.bljack.models.Shuffle;


@Controller
@RequestMapping( {"", "/", "/blackjack"} )
public class BlackJackController 
{
	
	final int NUMBER_OF_CARDS = 52;
	
	// Set up for the Game.START  ?? use enumerations ??
	boolean isGameOver  = false;
	boolean isPlayOver  = false;
	boolean isPlayStart = false;
	boolean isGameStart = true;

	// Create Hands(s) for Player / Dealer - there is a reason why these are <String> not <Integer>
	public ArrayList<String> playerHandArr = new ArrayList<String>();
	public ArrayList<String> dealerHandArr = new ArrayList<String>();
	
	// Initial $ amount in your account is determined randomly in order not to hard-code it: [101 
	Random rnd = new Random();
	private int startWithAmount = 100 + rnd.nextInt(901);
	
	// Declared it a reference because: the very 1st time it goes into view, 
	// it has to return null and a primitive cannot do that
	private Integer bet;
	
	// Counts cards dealt (array index)
	private int i = 0; 
	private int cards_left = 52;
	
	// Cards are listed by 'value' (the dumb way) though you can tell '10' & Face cards apart
	public int[] deck = {2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,
    		     		 99,99,99,99,
    		     		 10,10,10,10,10,10,10,10,10,10,10,10,11,11,11,11};	
    
       
	
	public BlackJackController()  { 		    		
	    // Shuffle it 	
		Shuffle shfl = new Shuffle();
		shfl.shuffleArray(deck);
		}	    	
		
		
	// model.addAttribute("showUnaryOperator",  numStack.size() > 0);
	
	Account act = new Account(startWithAmount);
	Hand handPlayer = new Hand();
    Hand handDealer = new Hand();
	
    
    
	/* Make visible a single button: "START GAME"
	 * -----------------------------------------
	    --> Make visible START GAME button
	    --> Make visible BET        button
	 */
	@GetMapping("")    
	public String index(Model model)   { 
			
		// How many cards left in the deck
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);

		// 'max_value' = upper limit of <input> used to select a bet: right now it.equals(100% of Player's a/c)
		model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		// At this point Current Amount = Starting Amount
		model.addAttribute("current_amount", startWithAmount);
		model.addAttribute("win", 0);
		
		// Select what buttons to show/hide		
		model.addAttribute("isGameStart", isGameStart);
		model.addAttribute("isPlayStart", isPlayStart);
		// No info on Player/Dealer Hands
		model.addAttribute("points_p", " - ");
		model.addAttribute("points_d", " - ");
			
		return "bljack/front";}
	
	
	
					
	/*  1. After you press 'START GAME' button
	 *  --------------------------------------
        --> 'START GAME' button hides (turns into 'EXIT GAME') ??
        -->  BET <input> hides
	    --> 'HIT' & 'STAND' appear 
	    -->  Player / Dealer total points & cards
	    -->  Player account $, number of cards left 
	 */ 
	@PostMapping("gamestart")
	public String dealCardsToPlayerDealer(Model model, int bet)   
	{   
		
		// Define what buttons are visible ?? funny way to do it... ??
		isGameStart = false;
		isPlayStart = true;
		model.addAttribute("isGameStart", isGameStart);
		model.addAttribute("isPlayStart", isPlayStart);
		
		// Deal 4 cards to Player/Dealer
		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);
        handPlayer.loadOneCard(String.valueOf(deck[i + 2]), playerHandArr);
        handDealer.loadOneCard(String.valueOf(deck[i + 1]), dealerHandArr);
        handDealer.loadOneCard(String.valueOf(deck[i + 3]), dealerHandArr);
        
        // Pass to form: total points & list of cards for Player/Dealer(+ the Hole) after the first round
        String temp="";
        temp = String.valueOf(handPlayer.getTotalPoints(playerHandArr)) + "   " +
        	   String.valueOf(handPlayer.getMyCards(playerHandArr));
        model.addAttribute("points_p", temp);
        
        temp = String.valueOf(handDealer.getTotalPoints(dealerHandArr)) + "   " +
         	   String.valueOf(handDealer.getMyCards(dealerHandArr)) + "<HOLE>";        
        model.addAttribute("points_d", temp);
        
                
        // Player goes Blackjack (he cannot go Bust at this point)
        //   - Remove the HIT button from the form
        //   - Wait until Player pushes the STAND button - ?? Should we proceed 'automatically' ??
        GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
        if (status.isBlackJack()) {
	        model.addAttribute("isPlayStart", false);
	        model.addAttribute("isBlackJack", status.isBlackJack());
	        model.addAttribute("msg", status.isBlackJackMsg());
        }
		
		// 'max_value' = upper limit of <input> used to select a bet: right now it.equals(100% of Player's a/c)
		model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));
			
		// How many cards left in the deck
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);

        // return "redirect:/blackjack      bljack/blackjack   bljack/deal-form-ST"  redirect:/blackjack;    
		return "redirect:/blackjack";
	}	
	
	
	
	
	// Get that number from the PUSH and place in the Stack
	@PostMapping("/hit")
	public String dealACardToPlayer(Model model) 
	{
		
		isGameStart = false;
		isPlayStart = true;
		model.addAttribute("isGameStart", isGameStart);
		model.addAttribute("isPlayStart", isPlayStart);
		
        GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
        if (status.isBlackJack()) {
	        model.addAttribute("isPlayStart", false);
	        model.addAttribute("isBlackJack", status.isBlackJack());
	        model.addAttribute("msg", status.isBlackJackMsg());
        }
        
        
		i = playerHandArr.size() + dealerHandArr.size();
		System.out.println("index  HIT" + i + "   " + deck[i]);

		// Pass to form (add to existing data) total points & list of cards for Player

		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);
        String temp="";
        temp = String.valueOf(handPlayer.getTotalPoints(playerHandArr)) + "   " +
        	   String.valueOf(handPlayer.getMyCards(playerHandArr));
		System.out.println("TEMP  " + temp);
        model.addAttribute("points_p", temp);
		
		// How many cards left in the deck
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);
		
		//model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));
		
		return "redirect:/blackjack";
        // return "bljack/hit";
	}

	
	
	
	// No more actions (from both Player & Dealer) are expected 
	// Draw cards for the dealer - Finalize Player account - Show the 'NEW GAME' message
	@PostMapping("/stand")
	public String activePlayStops(Model model)   
	{  
		isGameStart = false;
		isPlayStart = true;
		model.addAttribute("isGameStart", isGameStart);
		model.addAttribute("isPlayStart", isPlayStart);
		
        GameStatus status = new GameStatus(handPlayer.getTotalPoints(playerHandArr));
        if (status.isBlackJack()) {
	        model.addAttribute("isPlayStart", false);
	        model.addAttribute("isBlackJack", status.isBlackJack());
	        model.addAttribute("msg", status.isBlackJackMsg());
        }
        
        
		i = playerHandArr.size() + dealerHandArr.size();
		System.out.println("index  STAND " + i + "   " + deck[i]);

		// Pass to form (add to existing data) total points & list of cards for Player

		handPlayer.loadOneCard(String.valueOf(deck[i]), playerHandArr);
        String temp="";
        temp = String.valueOf(handPlayer.getTotalPoints(playerHandArr)) + "   " +
        	   String.valueOf(handPlayer.getMyCards(playerHandArr));
		System.out.println("TEMP  " + temp);
        model.addAttribute("points_p", temp);
		
		// How many cards left in the deck
		cards_left = NUMBER_OF_CARDS - playerHandArr.size() - dealerHandArr.size();
		model.addAttribute("cards_left", cards_left);
		
		//model.addAttribute("max_value", startWithAmount);
		model.addAttribute("start_with_amount", startWithAmount);
		model.addAttribute("current_amount", act.getAccountAmount());
		model.addAttribute("win", act.getWinLoss(startWithAmount));

		return "redirect:/blackjack"; 
		//return "bljack/deal-form-ST";
	}
}


