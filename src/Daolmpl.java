import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lxl,qh
 */
public class Daolmpl implements Dao {

    private List<Record> records;

    public Daolmpl(){
        records = new ArrayList<Record>();
    }

    @Override
    public void doDelete() {

    }

    @Override
    public void doAdd(Record record){
        records.add(record);
    }

    @Override
    public void writeFile(String path){
        try{
            ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream(path));
            for(Record player: records){
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
        records = new ArrayList<>();
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            Record player = (Record) ois.readObject();
            while(player != null){
                records.add(player);
                player = (Record) ois.readObject();
            }
            ois.close();
        } catch (EOFException e) {
            System.out.println("read finish");
        } catch (IOException e) {
            System.out.println("File Not Find ! Please Created a new file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(records);

    }

    @Override
    public Record findByName(String name){
        for (Record record : records) {
            if(record.getName().equals(name)){
                return record;
            }
        }
        return null;
    }

    @Override
    public boolean compareByPassword(Record record, String password){
        boolean isEqual;
        isEqual = record.getPassword().equals(password);
        return  isEqual;
    }
}
