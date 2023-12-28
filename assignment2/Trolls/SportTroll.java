package Trolls;

import java.util.HashMap;
import java.util.Objects;

import AdventureModel.AdventureGame;
import AdventureModel.Player;
import AdventureModel.Weapon;

public class SportTroll extends Troll{
    /**
     * Class creates a troll who fight a player by asking sports related questions
     */

    public HashMap<String, String> q_and_a; // hashmap containing the questions and answers

    /**
     * SportTroll Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     * It populates the hashmap
     * @param diff States the difficulty of the game and adjust the troll's damage and hp accordingly.
     */
    public SportTroll(boolean diff, Weapon weapon){
        super(diff);
        this.troll_instructions = "Get the right answer and attack me! Get it wrong and I'll strike!";
        this.q_and_a = new HashMap<>();
        this.q_and_a.put("Which country has won the most World Cups?","Brazil");
        this.q_and_a.put("Which club has won the most Champion Leagues?", "Real Madrid");
        this.q_and_a.put("Who was Germany's main keeper during the 2014 FIFA World Cup(last name)?","Neuer");
        this.q_and_a.put("What part of the body can you not used during a football game?","Hand");
        this.q_and_a.put("How many goals did Lionel Messi score during his 2011-2012 season?","91");
        this.q_and_a.put("Who has won the most Ballond D'Or?","Messi");
        this.q_and_a.put("How many rings does Lebron James have?","4");
        this.q_and_a.put("Who won the 2017 NBA championship(full name)?", "Golden State Warriors");
        this.q_and_a.put("What team was Ja Morant drafted too?","Memphis Grizzlies");
        this.q_and_a.put("Which Ball brother entered the NBA first(first name only)?", "Lonzo");
        this.q_and_a.put("Who won the World Cup in 2006?","Italy");
        this.q_and_a.put("What number did Michael Jordan wear?","23");
        this.q_and_a.put("What number does Stephen Curry wear?","30");
        this.q_and_a.put("How many Piston cups has Lightning Mcqueen won?","7");
        this.q_and_a.put("How many losses does Floyd Mayweather have?","0");
        this.q_and_a.put("What team does Victor Wembenyama play for(full name)?","San Francisco Spurs");
        this.q_and_a.put("How many Champions League's has Neymar won?","1");
        this.q_and_a.put("How many rings does Michael Jordan have?","6");
        this.q_and_a.put("How many Ballon D'Ors has Cristiano Ronaldo won?","5");
        this.q_and_a.put("What was the most points scored by one player in a basketball game?","100");

        this.weapon = weapon;
    }

    /**
     * This method checks if the player's answer is right.
     *
     * If the player answered the question right, he will attack
     * @param question //the question asked by the troll
     * @param player_a //the answer of the player
     * @param player //the player playing the game
     */
    public void playGame(String question, String player_a, Player player){
        if(this.q_and_a.get(question).equalsIgnoreCase(player_a)){
            player.attack(this);
        }else {
            this.attack(player);

        }
    }

    /**
     * This method allows the troll to inflect damage to the player of the game
     * @param player
     */
    public void attack(Player player) {
        player.hitpoints = player.hitpoints - this.weapon.damage;
    }
}


