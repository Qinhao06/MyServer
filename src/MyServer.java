

import UserDao.UserDao;
import UserDao.UserDaolmpl;
import UserDao.Record;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lxl,qh
 */
public class MyServer {

    private static final String PATH = "src/record.dat";

    private static final String END = "finish";
    private static final String EASY_GAME = "easy";
    private static final String MEDIUM_GAME = "medium";
    private static final String HARD_GAME = "difficult";
    private static final String GAME_OVER = "game over!";
    private static final String START = "start";
    private static final Pattern PATTERN = Pattern.compile("[0-9]*");


    private static final List<Player> EASY_PLAYER_LIST = new ArrayList<>();
    private static final List<Player> MEDIUM_PLAYER_LIST = new ArrayList<>();
    private static final List<Player> DIFFICULT_PLAYER_LIST = new ArrayList<>();

    private final UserDao daolmpl = new UserDaolmpl();

    public static void main(String[] args){
        new MyServer();
    }
    public MyServer(){
        try{
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("listen port 9999");

            while(true){
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                System.out.println("accept client connect" + socket);
                new Thread(new Service(socket)).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    class Service implements Runnable{

        private BufferedReader in = null;

        private PrintWriter pout = null;



        public Service(Socket socket){
            try{
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)),true);

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }


        @Override
        public void run() {
            System.out.println("wait client message " );
            try {
                if(Integer.parseInt(in.readLine()) == 1) {
                    enroll();
                }
                else {
                    login();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        public void sendMessage(String message) {
            System.out.println("message to client:" + message);
            pout.println(message);
        }

        public void login() throws IOException {
            Record record;
            String name = in.readLine();
            String password = in.readLine();
            daolmpl.readFile(PATH);
            if(daolmpl.findByName(name) != null){
               record = daolmpl.findByName(name);
               if(daolmpl.compareByPassword(record, password)){
                   sendMessage(END);
                   System.out.println("client login success");
                   System.out.println("waiting for match");
                   playerMatch(name);
               }else{
                   System.out.println("password wrong");
                   System.out.println("client login failed");
               }
            }else{
                System.out.println("name not exist!");
                System.out.println("client login failed");
            }
        }

        public void playerMatch(String name){
            String message = null;
            Player player = null;
            try{
                message = in.readLine();
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(message != null && !isNumeric(message) && !message.equals(GAME_OVER)){
                player = new Player(name, 0);
                createMatch(message, player);
                try{
                    message = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            while(message == null){
                if(player != null)
                {
                    if(player.getMatch()){
                        sendMessage(START);
                    }
                }
                try{
                    message = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while(!message.equals(GAME_OVER) && player != null){
                player.setScore(Integer.parseInt(message));
                int opponentScore = player.getOpponent().getScore();
                sendMessage(Integer.toString(opponentScore));
                try{
                    message = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void createMatch(String message, Player player){
            switch (message) {
                case EASY_GAME:
                    synchronized (EASY_PLAYER_LIST) {
                        if (EASY_PLAYER_LIST.size() == 1) {
                            player.setOpponent(EASY_PLAYER_LIST.get(0));
                            EASY_PLAYER_LIST.get(0).setOpponent(player);
                            player.setMatch(true);
                            EASY_PLAYER_LIST.get(0).setMatch(true);
                            EASY_PLAYER_LIST.remove(0);
                        } else if (EASY_PLAYER_LIST.size() == 0) {
                            EASY_PLAYER_LIST.add(player);
                        }
                    }
                    break;
                case MEDIUM_GAME:
                    synchronized (MEDIUM_PLAYER_LIST) {
                        if (MEDIUM_PLAYER_LIST.size() == 1) {
                            player.setOpponent(MEDIUM_PLAYER_LIST.get(0));
                            MEDIUM_PLAYER_LIST.get(0).setOpponent(player);
                            player.setMatch(true);
                            EASY_PLAYER_LIST.get(0).setMatch(true);
                            MEDIUM_PLAYER_LIST.remove(0);
                        } else if (MEDIUM_PLAYER_LIST.size() == 0) {
                            MEDIUM_PLAYER_LIST.add(player);
                        }
                    }
                    break;
                case HARD_GAME:
                    synchronized (DIFFICULT_PLAYER_LIST) {
                        if (DIFFICULT_PLAYER_LIST.size() == 1) {
                            player.setOpponent(DIFFICULT_PLAYER_LIST.get(0));
                            DIFFICULT_PLAYER_LIST.get(0).setOpponent(player);
                            player.setMatch(true);
                            EASY_PLAYER_LIST.get(0).setMatch(true);
                            DIFFICULT_PLAYER_LIST.remove(0);
                        } else if (DIFFICULT_PLAYER_LIST.size() == 0) {
                            DIFFICULT_PLAYER_LIST.add(player);
                        }
                    }
                    break;
                default: break;
            }
        }

        public boolean isNumeric(String message){
            return PATTERN.matcher(message).matches();
        }


        public void enroll() throws IOException {
            String name = in.readLine();
            String password = in.readLine();
            if(daolmpl.findByName(name) != null){
                System.out.println("client enroll failed");
            }else{
                Record record = new Record(name, password);
                daolmpl.doAdd(record);
                daolmpl.writeFile(PATH);
                sendMessage(END);
                System.out.println("client enroll success");
            }
        }
    }
}