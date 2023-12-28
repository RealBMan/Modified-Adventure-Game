package Trolls;


import AdventureModel.Player;
import AdventureModel.Room;
import AdventureModel.Weapon;

/**
 * Troll interface for Trolls used in the adventure game. It has been modified and sutied
 * to fit the game for our final 207 project.
 * Course code tailored by the CSC207 instructional
 * team at UTM, with special thanks to:
 *
 * @author anshag01
 * @author mustafassami
 * @author guninkakr03
 *  */
public abstract class Troll {
    public int troll_hp; //The trolls health points assigned

    public Weapon weapon; //The weapon the troll has
    public String troll_instructions; //The instructions for the given troll and how to play the troll

    /**
     * Troll Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     *
     * @param diff States the difficulty of the game and adjust the troll hp and damage accordingly.
     */
    public Troll(boolean diff){
        if (diff){
            this.troll_hp = 100;
        }else {
            this.troll_hp = 50;
        }

    }

    /**
     * giveInstructions()
     * _________________________
     * Returns the instructions on how to defeat the current troll
     *
     * @return String containing the instructions of the troll
     */
    public String giveInstructions() {
        return this.troll_instructions;
    }

    /**
     * This method allows the troll to inflect damage to the player of the game
     * @param player
     */
    public abstract void attack(Player player);
}



