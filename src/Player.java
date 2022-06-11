/**
 * @author lxl
 */
public class Player {

    private String name;
    private int score;
    private Player opponent;

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

    public Player getOpponent(){
        return opponent;
    }

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
