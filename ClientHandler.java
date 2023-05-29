import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{


    public static ArrayList<ClientHandler> clientHandlers=new ArrayList<>();
    //this arraylist is to allow to send messages to more people like to broadcast
    private Socket socket;
    private BufferedReader reader;
   private BufferedWriter writer;
   private String username;

   public  ClientHandler(Socket socket){
       try{
           this.socket=socket;
          this.writer=new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
          this.reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
          this.username=reader.readLine();
          clientHandlers.add(this);
          broadcastMessage("SERVER:"+username+" "+"has entered the chat");




       } catch (IOException e) {
          closeEverything(socket, writer, reader);
       }
   }
    @Override
    public void run() {
       //run on separate thread waiting for messages
        String messageFromClient;
        while (socket.isConnected()){
            try{
                messageFromClient=reader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
               closeEverything(socket, writer, reader);
               break;
            }
        }

    }
    public  void  broadcastMessage(String messageToSend){
       for (ClientHandler clientHandler:clientHandlers){
           try{
               if(clientHandler.username.equals(username)){
                   clientHandler.writer.write((messageToSend));
                   clientHandler.writer.newLine();
                   clientHandler.writer.flush();
               }
           } catch (IOException e) {
            closeEverything(socket,writer,reader);
           }
       }
    }
    public  void  removeClientHandler(){
       clientHandlers.remove(this);
       broadcastMessage("SERVER:"+username+"has left the chat!");
    }
    public  void  closeEverything(Socket socket,BufferedWriter writer,BufferedReader reader){
       removeClientHandler();
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
}
