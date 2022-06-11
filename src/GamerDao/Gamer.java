package GamerDao;

/**
 * @author lxl
 */
public class Gamer {
    private final String name;
    private final String playTime;
    private final int score;

    public Gamer(String name, String playTime, int score){
        this.name = name;
        this.playTime = playTime;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getPlayTime() {
        return playTime;
    }

    public int getScore() {
        return score;
    }

}
