package AdventureModel;

public class Weapon extends AdventureObject{
    /**
     * The amount of damage the weapon does.
     */
    public int damage;
    /**
     * The name of the object.
     */
    private String objectName;

    /**
     * The description of the object.
     */
    private String description;

    /**
     * The location of the object.
     */
    private Room location = null;

    /**
     * Adventure Object Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param name The name of the Object in the game.
     * @param description One line description of the Object.
     * @param location The location of the Object in the game.
     * @param damage The damage the weapons does to opponents.
     */
    public Weapon(String name, String description, Room location, int damage){
        super( name, description,location);
        this.damage = damage;
    }
}
