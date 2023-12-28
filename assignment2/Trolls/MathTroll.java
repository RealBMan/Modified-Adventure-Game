package Trolls;

import AdventureModel.AdventureGame;
import AdventureModel.Player;
import AdventureModel.Room;
import AdventureModel.Weapon;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

public class MathTroll extends Troll{
    /**
     * DeathTroll Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param The string which is the question that the troll will be asked.
     */
    public HashMap<String, String> questions;
    private ArrayList<String> asked;
    private String current_question;
    public MathTroll(boolean diff, Weapon weapon) {
        super(diff);
        this.troll_instructions = "This is a MathTroll to battle this troll you will be asked a math question and for every wrong asnwer the troll attacks the player and health points is decreased.";

        this.asked = new ArrayList<String>();
        this.questions = new HashMap<String, String>();

        Random rand = new Random();
        int int_random = rand.nextInt(0,20);
        double num = Math.sqrt(int_random);
        this.questions.put("What is square root of " +int_random, Double.toString(Math.round(num)));

        int square = rand.nextInt(0,20);
        int num_2 = square * square;
        this.questions.put("What is "+square+" squared", Integer.toString(num_2));

        int cubic = rand.nextInt(0,20);
        double num_3 = Math.cbrt(cubic);
        this.questions.put("What is the cubic root of "+cubic, Double.toString(Math.round(num_3)));

        int cubed = rand.nextInt(0,20);
        int num_4 = cubed * cubed * cubed;
        this.questions.put("What is "+cubed+" cubed", Integer.toString(num_4));

        int num_5 = rand.nextInt(20);
        int num_6 = rand.nextInt(20);
        int num_7 = rand.nextInt(20);
        int num_8 = rand.nextInt(20);
        int answer  = num_5 + num_6 - num_7 * num_7 - num_8;
        this.questions.put("Solve the equation: "+num_5+" + "+num_6 +" - "+num_7+" x "+num_7+" - "+num_8, Integer.toString(answer));

        this.weapon = weapon;

    }

    /**
     * askQuestion
     * ___________________________
     * This method return the math question that the player would be asked when they encounter this troll.
     *
     * @return String This is current player object that the troll is attacking.
     */
    public String askQuestion(){
        Random rand = new Random();
        int r_int = rand.nextInt(0,5);
        ArrayList<String> q = new ArrayList<String>(this.questions.keySet());
        String question = q.get(r_int);
        if (this.asked.contains(question)){
            Random rand_2 = new Random();
            int r = rand_2.nextInt(0,5);
            this.current_question = q.get(r_int);
        } else {
            this.current_question = question;
        }
        this.asked.add(question);
        return this.current_question;
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
        String correct = this.questions.get(this.current_question);
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
