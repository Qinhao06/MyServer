

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class MyServer {
    private String content = "";
    public static void main(String args[]){
        new MyServer();
    }
    public  MyServer(){
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
        private Socket socket;
        private BufferedReader in = null;

        private PrintWriter pout = null;

        private static ObjectOutputStream fout;

        private static ObjectInputStream fin;


        static {
            try {
                fout = new ObjectOutputStream(new FileOutputStream(new File("src/record.dat")));
                fin = new ObjectInputStream(new FileInputStream(new File("src/record.dat")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Service(Socket socket){
            this.socket = socket;
            try{
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);

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


        public void sendMessge(Socket socket, String message) {
            System.out.println("messge to client:" + message);
            pout.println(message);
        }

        public void login() throws IOException {
            String name = in.readLine();
            String password = in.readLine();
            System.out.println(name);
            System.out.println(password);

            if(Objects.equals(name, "1") && Objects.equals(password, "1")){
                pout.println("finish");
            }


        }

        public void enroll() throws IOException {
            String name = in.readLine();
            String password = in.readLine();
            Record record = new Record(name, password);
            fout.writeObject(record);
            pout.println("finish");
            pout.println("enroll success");
            System.out.println("client enroll");
        }
    }
}