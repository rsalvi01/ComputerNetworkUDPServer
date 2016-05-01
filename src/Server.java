import java.net.*;
class Server {
    public static void main(String args[]) throws Exception
    {
        //create socket to send and receive datagrams
        DatagramSocket serverSocket = new DatagramSocket(9876);

        //variable to receive result from clients
        byte[] receiveData = new byte[1024];

        //maximum allowed winnings within a single execution of the server client is 5, set arbitrarily
        int winnings = 0;

        //variables required by the assignment
        int counter = 0;
        int storedClientID = 0;
        InetAddress storedClientIP = null;
        int storedClientPort = 0;

        while(true){
            //win or lose message
            String message = "The game is over.";

            //creates a datagram packet to store incoming datagrams
            //and then attempts to receive a datagram
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            //extracts the IP address and port of the client
            //these will be used to return a message to the client
            storedClientIP = receivePacket.getAddress();
            storedClientPort = receivePacket.getPort();

            //MESSAGE PROCESSING
            //transforms the incoming byte array from a client into an array
            String clientInput = new String(receivePacket.getData());
            clientInput = clientInput.replaceAll("\\u0000", "");

            //extracts and parses the client input
            storedClientID = Integer.parseInt(clientInput.substring(0,clientInput.indexOf("|")));
            int clientGuess = Integer.parseInt(clientInput.substring(clientInput.indexOf("|")+1,clientInput.length()));

            //check whether game is allowed to continue, i.e.: maximum number of winnings hasn't been reached
            if (winnings < 5) {
                counter = (counter + clientGuess) % 10;
                if (counter == 0) {
                    winnings++;
                    message = "You won.";
                }
                else message = "You lost.";
            }

            //prepares and sends a datagram packet back to the client
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, storedClientIP, storedClientPort);
            serverSocket.send(sendPacket);

            //outputting information to facilitate monitoring server functionality
            System.out.println("Client " + storedClientID + " input was " + clientGuess + ", resulting counter was " + counter +".");
            System.out.println("Resulting message: " + message);
            System.out.println("Winning counter at: " + winnings + "\n");
        }
    }
}