

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author lxl,qh
 */
public class MyServer {

    private static final String PATH = "src/record.dat";

    private final Dao daolmpl = new Daolmpl();

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
                   pout.println("finish");
                   System.out.println("login success");
               }else{
                   System.out.println("password wrong");
               }
            }else{
                System.out.println("name not exist!");
            }
        }

        public void enroll() throws IOException {
            String name = in.readLine();
            String password = in.readLine();
            if(daolmpl.findByName(name) != null){
                pout.println("name has existed");
                System.out.println("client enroll failed");
            }else{
                Record record = new Record(name, password);
                daolmpl.doAdd(record);
                daolmpl.writeFile(PATH);
                pout.println("finish");
                pout.println("enroll success");
                System.out.println("client enroll success");
            }

        }
    }
}