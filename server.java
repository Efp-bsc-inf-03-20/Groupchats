import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//our server class will be responsible for listening to clients
public class server {

    //object responsible for handling request of client
    private  ServerSocket serverSocket;

    server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;

    }
    //here is amethod that keep our server running
    public  void startServer() throws IOException {
        try {
            while(!serverSocket.isClosed()){
                Socket socket=serverSocket.accept();
                System.out.println("a client is connected");
                ClientHandler clientHandler=new ClientHandler(socket);

                Thread thread=new Thread(clientHandler);
                thread.start();
            }

        }
       catch (IOException e){
            e.printStackTrace();

       }


  }

    public static void main(String[] args) throws IOException {
        ServerSocket mysocket=new ServerSocket(1234);
        server server=new server(mysocket);
        server.startServer();
    }


}
