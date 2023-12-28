package AdventureModel;

public class HealthPack extends AdventureObject {
    /**
     * The amount of health the healthpack gives.
     */
    protected int hp;
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
     * @param hp The value of health the pack gives.
     */
    public HealthPack(String name, String description, Room location, int hp){
        super(name, description,location);
        this.hp = hp;
    }
}
