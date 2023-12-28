package views;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    /**
     * This ia class following hte singleton pattern. It creates on instance of a Leaderboard, stores all player names and times in an arraylist;
     * Writes to a file when the game is over and reads from it when the game starts
     */
    private static Leaderboard board;//the instance of a leaderboard

    private ArrayList<List<Object>> top_players;// array list containing at most 10 player times combination

    /**
     * Constructor
     * -------------------------
     * Populates the top_players attribute using the Leaderboard.txt file
     */
    private Leaderboard(){
        this.top_players = readLeaderBoardFile();
    }

    /**
     * Creates the only instance of Leaderboard
     * @return a Leaderboard object
     */
    public static Leaderboard getInstance(){
        if (board == null){
            board = new Leaderboard();
        }
        return board;
    }

    /**
     * Reads the Leaderboard.txt file to populate an arraylist with usernames and times
     * @return an arraylist contiaing unsernames and times
     */
    public ArrayList<List<Object>> readLeaderBoardFile()  {
        try {
            FileReader lead = new FileReader("Games/TinyGame/Leaderboard.txt");
            BufferedReader buff = new BufferedReader(lead);
            ArrayList<List<Object>> temp_p = new ArrayList<>();
            while (buff.ready()) {
                List<Object> l_p = new ArrayList<Object>();
                l_p.add(buff.readLine().strip());
                l_p.add((Integer.parseInt(buff.readLine().strip())));
                temp_p.add(l_p);
            }
            return temp_p;
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sees if the player can be added in the top 10 leader board.
     *
     * If the player can, we add the player in the position, he should be based on his time.
     * If the arraylist of  top_players already contains 10 players, we remove the last player-time combination
     * If we add a player, the method writes the new array list to the Leaderboard.txt file
     * @param p a list containing a username and a time in int
     */
    public void addPlayer(List<Object> p){
        int i = 0;
        while(i<this.top_players.size()){
            if((int) this.top_players.get(i).get(1)>(int)p.get(1)){
                if(this.top_players.size() == 10){
                    this.top_players.remove(9);
                }
                break;
            }
            ++i;
        }
        if(this.top_players.size()<10){
            this.top_players.add(i,p);
            writetoLeaderBoardFile();
        }
    }

    /**
     * This method writes the top_players arraylist to the Leaderboard.txt file
     */
    private void writetoLeaderBoardFile(){
        try {
            FileWriter lead_w = new FileWriter("Games/TinyGame/Leaderboard.txt", false);
            BufferedWriter buff_w = new BufferedWriter(lead_w);
            for (List<Object> pl: this.top_players){
                buff_w.write(pl.get(0).toString());
                buff_w.newLine();
                buff_w.write(pl.get(1).toString());
                buff_w.newLine();
            }
            buff_w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return top_players attributes
     */
    public ArrayList<List<Object>> getTop_players(){
        return this.top_players;
    }

}
