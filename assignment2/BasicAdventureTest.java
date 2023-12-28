
import java.io.IOException;

import AdventureModel.AdventureGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        //assertEquals("DOWN,NORTH,IN,WEST,UP,SOUTH", commands); /Wrong test case **ordering does not matter according to Piazza 1038
        assertEquals("WEST,UP,NORTH,IN,SOUTH,DOWN", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    @Test
    void getCommandsTest_2() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("WEST");
        String commands = game.player.getCurrentRoom().getCommands();
        //assertEquals("DOWN,NORTH,IN,WEST,UP,SOUTH", commands); /Wrong test case **ordering does not matter according to Piazza 1038
        assertEquals("EAST,WEST,DOWN", commands);
    }

    @Test
    void getObjectString_2() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("WEST");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a pirate chest", objects);
    }

    @Test
    void getObjectString_3() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("SOUTH");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("", objects);
    }

    @Test
    void getCommandsTest_3() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        game.movePlayer("NORTH");
        String commands = game.player.getCurrentRoom().getCommands();
        //assertEquals("DOWN,NORTH,IN,WEST,UP,SOUTH", commands); /Wrong test case **ordering does not matter according to Piazza 1038
        assertEquals("SOUTH,WEST,OUT,NORTH,XYZZY,PLUGH", commands);
    }
}
