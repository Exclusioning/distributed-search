package com.mycompany.pegsolitaire;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author ZMill
 */
public class ServerDistribution {
    public String host;
    public int port;
    
    public ServerDistribution(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    public Object[] bestMove(Board board, int depth, int threads, List<Move> moves) throws IOException{
        try(Socket socket = new Socket(host, port)){
            
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        
            out.writeObject(board);
            out.writeInt(depth);
            out.writeInt(threads);
            out.writeObject(moves);
            out.flush();
        
            Move bestMove = (Move) in.readObject();
            int score = in.readInt();

            return new Object[]{ bestMove, score};
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } 
    }
    
}
