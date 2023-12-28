package Trolls;

import AdventureModel.Player;
import AdventureModel.Room;
import AdventureModel.Weapon;

import java.util.Random;

public class DeathTroll extends Troll{
    /**
     * DeathTroll Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param room This is where the toll can be found.
     */
    private Weapon troll_weapon; //The weapon the trolls has
    public boolean defend; //Whether the troll is able to defend the attack

    public DeathTroll(boolean diff, Weapon weapon) {
        super(diff);
        this.troll_instructions = "This is a DeathTroll to battle this troll you have to use inventory to attack the troll. Although beware that the Troll might attack you back as well with the weapon this troll possess.";
        this.weapon = weapon;
    }

    /**
     * This method allows the troll to inflect damage to the player of the game
     * @param player
     */
    public void attack(Player player) {
        player.hitpoints = player.hitpoints - this.weapon.damage;
    }

    /**
     * defendPlayer(Player player)
     * ___________________________
     * This method is for when the troll defends a player's attack. Depending on the difficulty of the game
     * the troll can defend against the player or not. If the game is on hard mode the troll might defend the
     * attack depending on the result of the random number generator (i.e. when the number is even). If the game
     * is on easy then it means that the troll cannot defend the attack.
     *
     * @param player This is current player object that the troll is attacking.
     */
    public boolean defendAttack(Player player){
        Random rand = new Random();
        int int_random = rand.nextInt(101);
        if (player.difficultyHard){
            if (int_random % 2 == 0){
                this.defend = true;
            } else {
                this.defend = false;
            }
        } else {
            this.defend = false;
        }
        return this.defend;
    }
}
