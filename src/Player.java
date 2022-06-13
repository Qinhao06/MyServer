/**
 * @author lxl
 */
public class Player {

    private String name;
    private int score;
    private Player opponent;
    private boolean match = false;

    private boolean isMatch;

    public Player(String name, int score){
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName(){
        return name;
    }

    public boolean getMatch(){return match;}

    public Player getOpponent(){
        return opponent;
    }


    public void setMatch(boolean match) {this.match = match; }

    public void setOpponent(Player opponent){
        this.opponent = opponent;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setScore(int score){
        this.score = score;
    }
}
