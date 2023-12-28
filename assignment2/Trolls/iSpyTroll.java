package Trolls;

import AdventureModel.Player;
import AdventureModel.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class iSpyTroll extends Troll{

    public HashMap<String, Integer> fruits_answers;
    private Weapon troll_weapon; //the weapon the troll has

    private String current_question;
    /**
     * Troll Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param diff   States the difficulty of the game and adjust the troll hp and damage accordingly.
     * @param weapon
     */
    public iSpyTroll(boolean diff, Weapon weapon) {
        super(diff);
        this.troll_instructions = "Count the number of objects I say or I'll hit you!";
        this.weapon = weapon;
        this.fruits_answers = new HashMap<>();
        this.load_answers();

    }
    public void load_answers(){
        this.fruits_answers.put("peaches",6);
        this.fruits_answers.put("watermelons",8);
        this.fruits_answers.put("cherries",4);
        this.fruits_answers.put("kiwis",4);
        this.fruits_answers.put("pineapples",6);
        this.fruits_answers.put("grapes",7);
        this.fruits_answers.put("oranges",7);
        this.fruits_answers.put("strawberries",3);

    }

    public String askQuestion() {
        Random rand = new Random();
        int r_int = rand.nextInt(0,this.fruits_answers.size());
        ArrayList<String> q = new ArrayList<String>(this.fruits_answers.keySet());
        String question = q.get(r_int);
        this.current_question = question;
        return "How many "+this.current_question+" can you spot?";
    }
    /**
     * guessAnswer
     * ___________________________
     * This method is for when the player gueses the answer to a question. If the answer is wrong then the
     * player takes damage which is decided based on whether the game is on hard or easy mode. If the answer
     * is right then player does not take any damage. The method returns a string prompt which gives the player the
     * choice to decide what their next move is going to be.
     *
     * @param player This is current player object that the troll is attacking.
     * @param answer This is the value of the answer that the player guess.
     * @return The method returns a string which gives the user a prompt on what they want to do next.
     */
    public boolean guessAnswer(String answer, Player player){
        String correct = String.valueOf(this.fruits_answers.get(this.current_question));
        if (!correct.equals(answer)){
            return false;
        } else {
            return true;
        }
    }

    public void playGame(boolean answer, Player player){
        if (answer){
            player.attack(this);
        } else {
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
