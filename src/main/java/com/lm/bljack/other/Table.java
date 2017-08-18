//package com.lm.bljack.other;
//
//import com.lm.bljack.models.GameStatus;
//import com.lm.bljack.models.Hand;
//import com.lm.bljack.models.Shuffle;
//
//public class Table {
//
//	private static int i = 0;
//	
//	
//	  public static void main(String args[]) {
//	
//					
//        // 1. Create the deck
//        //    52 Cards, Aces = 11 or 1, faces = 10
//        int[] deck = {2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,
//        		     99,99,99,99,
//        		     10,10,10,10,10,10,10,10,10,10,10,10,
//        		     11,11,11,11};
//
//        
//        // 2. Shuffle it
//        Shuffle shfl = new Shuffle();
//        shfl.shuffleArray(deck);
//
//       
//        /* 3. Start a game
//         *  Create Hand object(s) 
//              - for Player (first & third cards)  & count her Total points
//              - for Dealer (second & forth cards) & count her Total points (the 4th card being in the "hole" for now)
//            Check Player for Blackjack & Bust - after getting initial 2 cards
//         */
//        Hand handPlayer = new Hand();
//        handPlayer.loadOneCard(String.valueOf(deck[i]));
//        handPlayer.loadOneCard(String.valueOf(deck[i + 2]));
//        Hand handDealer = new Hand();
//        handDealer.loadOneCard(String.valueOf(deck[i + 1]));
//
//        
//        // TBD
//        System.out.println(handPlayer.getTotalPoints());
//        System.out.println(handPlayer.getMyCards());        
//               
//        GameStatus status = new GameStatus(handPlayer.getTotalPoints());
//        // TBD
//        System.out.println(handPlayer.getTotalPoints()); 
//        
//        // TBD
//        //System.out.println("My HAND: " + handPlayer.playerHand_AL);
//        System.out.println("Is blackjack: " + status.isBlackJack());
//        System.out.println("Is bust:      " + status.isBust());        
//         
//        
//        
//        /* 4. Let Player get as many cards as necessary ()
//              Loop will be skipped, if Player is Busted OR has BlackJack   
//              Index i == 4 at this point
//        */
//        while(!status.isBlackJack() || !status.isBust() || i < 52) {
//        	i++;
//        	// handDealer.setTotalPoints(handDealer.checkPointsOneCard(deck[i]));
//        }            
//	 }       
//}	