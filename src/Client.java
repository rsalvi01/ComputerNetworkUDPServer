import java.io.*;
import java.net.*;

public class Client {

    public static void main(String args[]) throws Exception {
        //create reader to get user input from keyboard
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        //get user ID
        System.out.println("Please enter your ID. It must be an integer from 1 to 100.");
        String userID = inFromUser.readLine();

        //create socket to send and receive datagrams
        DatagramSocket clientSocket = new DatagramSocket();

        //IP address for datagram packet parameter
        InetAddress IPAddress = InetAddress.getByName("localhost");

        //variable to receive result from server
        byte[] receiveData = new byte[1024];

        boolean gameOver = false;

        while(!gameOver){
            //read user's input/guess for the winning number
            System.out.println("Please enter your guess. It must be an integer.");
            String userGuess = inFromUser.readLine();

            //transforms user ID and user guess into a single byte array, separating these with a pipe "|"
            //this byte array is the data to be send to the server and it's also a datagram packet parameter
            byte[] sendData = (userID +"|"+ userGuess).getBytes();

            //send packet to localhost at port 9876. Please notice this requires both the client and server application
            //to be running in the same machine
            //the port 9876 is chosen at random, more precisely, the one that was in the materials provided :P
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);

            //creates a data packet to store incoming data and then attempts to receive a datagram
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            //receives the byte array from the server and converts into a string
            String serverFeedback = new String(receivePacket.getData());

            System.out.println(serverFeedback+"\n");

            //check whether the server wishes to finish the game
            //this isn't in the specification but I didn't want to leave the client running indefinitely
            if(serverFeedback.contains("game")){
                gameOver = true;
                System.out.println("This server finished the game.");
            }
        }
        clientSocket.close();
    }
}