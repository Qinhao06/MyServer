package GamerDao;

import UserDao.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author lxl
 */
public class GamerDaolmpl implements GamerDao{
    private List<Gamer> gamers;

    public GamerDaolmpl(){
        gamers = new ArrayList<>();
    }

    @Override
    public void doDelete(String name) {

    }

    @Override
    public void doAdd(Gamer gamer){
        boolean hasExisted = false;
        for (Gamer player : gamers) {
            if(player.getName().equals(gamer.getName())){
                hasExisted = true;
                if(gamer.getScore() > player.getScore()){
                    gamers.add(gamer);
                    gamers.remove(player);
                }
            }
        }
        if(!hasExisted){
            gamers.add(gamer);
        }
    }

    @Override
    public void writeFile(String path){
        sortByScore();
        try{
            ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream(path));
            for(Gamer player: gamers){
                oss.writeObject(player);
            }
            oss.writeObject(null);
            oss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFile(String path){
        ObjectInputStream ois;
        gamers = new ArrayList<>();
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            Gamer player = (Gamer) ois.readObject();
            while(player != null){
                gamers.add(player);
                player = (Gamer) ois.readObject();
            }
            ois.close();
        } catch (EOFException e) {
            System.out.println("read finish");
        } catch (IOException e) {
            System.out.println("File Not Find ! Please Created a new file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(gamers);
    }

    @Override
    public Gamer findByName(String name){
        for (Gamer player : gamers) {
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    @Override
    public List<Gamer> getAll(){
        return gamers;
    }

    public void sortByScore(){
        Comparator<Gamer> scoreComparator = Comparator.comparing(Gamer::getScore);
        gamers.sort(scoreComparator.reversed());
    }
}
