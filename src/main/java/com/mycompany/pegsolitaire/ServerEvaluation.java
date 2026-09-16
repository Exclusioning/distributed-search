package com.mycompany.pegsolitaire;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerEvaluation {
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        try{
            int port;

            if(args.length > 0){
                port = Integer.parseInt(args[0]);
            } else{
                port = 26700;
            }

            System.out.println("Listening on port " + port);
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                Socket socket = serverSocket.accept();
                new Worker(socket).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static class Worker extends Thread{
        private Socket socket;
        public Worker(Socket socket){
            this.socket = socket;
        }

        public void run(){
            try{
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Board board = (Board)in.readObject();
                int depth = in.readInt();
                int threads = in.readInt();
                List<Move> moves = (List<Move>) in.readObject();
                System.out.println("Depth: " + depth);
                System.out.println("Threads: " + threads);
                System.out.println("Received " + moves.size() + " moves:");
                for(Move move : moves) {
                    System.out.println("  " + move);
                }

                Evaluator eval = new Evaluator();
                eval.setDepth(depth);
                eval.threads(threads);

                Move bestMove = null;
                int bestScore = Integer.MIN_VALUE;

                for(Move move : moves){
                    Board temp = new Board(board);
                    temp.move(move.fromX, move.fromY, move.toX, move.toY);
                    int score = eval.searchBoard(temp, depth - 1);
                    if(score > bestScore) {
                        bestScore = score;
                        bestMove = move;
                    }
                }

                out.writeObject(bestMove);
                out.writeInt(bestScore);
                out.flush();

                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
