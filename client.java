import javax.imageio.IIOException;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public  client(Socket socket,String username){
        try{
            this.socket=socket;
            this.writer=new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));
            this.reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username=username;


        }
        catch (IOException e) {
        closeEverything(socket,writer,reader);
        }

    }
    public  void sendMessage(){
        try{
            writer.write(username);
            writer.newLine();
            writer.flush();
            Scanner scanner=new Scanner(System.in);
            while ((socket.isConnected())){
                String messageToSend=scanner.nextLine();
                writer.write(username+":"+messageToSend);
                writer.newLine();
                writer.flush();

            }

        } catch (IOException e) {
            closeEverything(socket, writer, reader);

        }
    }
    public void listenformssage(){
       new Thread(new Runnable() {
           @Override
           public void run() {
               String msgFromGroupChat;
               while (socket.isConnected()){
                   try{
                       msgFromGroupChat=reader.readLine();
                       System.out.println(msgFromGroupChat);

                   } catch (IOException e) {
                       closeEverything(socket, writer, reader);
                   }

               }
           }
       }).start();
    }
    public void  closeEverything(Socket socket,BufferedWriter writer,BufferedReader reader){
        try {
            if (reader!=null){
                reader.close();
            }
            if (writer !=null){
                writer.close();
            }
            if(socket !=null){
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("enter your username for groupchat:");
        String username=scanner.nextLine();
        try {
            Socket socket1=new Socket("localhost",1234);
            client client=new client(socket1,username);
            client.listenformssage();
            client.sendMessage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
