package AdventureModel;

import Trolls.Troll;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player implements Serializable {
    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;

    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;

    /**
     * The amount of healthpoints a player has.
     */
    public int hitpoints;

    /**
     * The current amount of time that the player has been playing for
     */
    public String timeTaken = "";

    /**
     * An attribute storing the current weapon the player is carrying.
     */
    public Weapon stored_weapon;

    /**
     * The name that the player enters at the start of the game.
     */
    public String username = "";
    /**
     * Adventure Game Player Constructor
     */
    /**
     * Boolean that stores if difficulty is hard or not.
     */
    public Boolean difficultyHard;

    public Player(Room currentRoom) {
        this.stored_weapon = new Weapon("PICKAXE", "The default weapon", new Room("",0,"",""),10);
        this.inventory = new ArrayList<AdventureObject>();
        this.inventory.add(this.stored_weapon);
        this.currentRoom = currentRoom;
        this.hitpoints = 100;
    }

    /**
     * getHitpoints()
     * _________________________
     * Returns the health points for the current player
     *
     * @returns returns the current health points for the player
     */
    public int getHitpoints(){
        return this.hitpoints;
    }

    /**
     * setHitpoints(int points)
     * _________________________
     * Sets the new health points for the player
     *
     * @params points The new health points for the player
     */
    public void  setHitpoints(int points){
        this.hitpoints = points;
    }

    /**
     * This method adds an object into players inventory if the object is present in
     * the room and returns true. If the object is not present in the room, the method
     * returns false.
     *
     * @param object name of the object to pick up
     * @return true if picked up, false otherwise
     */
    public boolean takeObject(String object){
        if(this.currentRoom.checkIfObjectInRoom(object)){
            AdventureObject object1 = this.currentRoom.getObject(object);
            this.currentRoom.removeGameObject(object1);
            this.addToInventory(object1);
            if(object1 instanceof Weapon){
                this.inventory.remove(this.stored_weapon);
                this.stored_weapon = (Weapon) object1;
            }
            return true;

        } else {
            return false;
        }
    }


    /**
     * checkIfObjectInInventory
     * __________________________
     * This method checks to see if an object is in a player's inventory.
     *
     * @param s the name of the object
     * @return true if object is in inventory, false otherwise
     */
    public boolean checkIfObjectInInventory(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s) && !(this.inventory.get(i) instanceof Weapon)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
            }
        }
    }

    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }

    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            objects.add(this.inventory.get(i).getName());
        }
        return objects;
    }
    /**
     * This method allows the player to deal damage to a troll using his weapon
     * @param t //troll the player is fighting
     */
    public void attack(Troll t){
        t.troll_hp = t.troll_hp - this.stored_weapon.damage;
    }


    /**
     * methods checks if the player has a healingpack in its inventory.
     *
     * If the player has 100 health points, this method terminates
     *
     * If the player's health points is under 100 check if the play has a healing pack and apply
     * it to the player(remove it from the inventory afterwards)
     */
    public boolean heal_player(){
        for(AdventureObject object:this.inventory){
            if (this.hitpoints>= 100){
                this.hitpoints = 100;
                return false;
            }else{
                if(object instanceof HealthPack){
                    this.hitpoints+= ((HealthPack) object).hp;
                    this.inventory.remove(object);
                    return true;
                }
            }
        }
        return false;
    }


}
