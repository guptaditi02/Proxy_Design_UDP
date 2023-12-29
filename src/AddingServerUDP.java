//Aditi Gupta - argupta@andrew.cmu.edu - Project2Task2
// Used Lab5 for separation of concerns
// Used EchoServerUDP.java from Coulouris textbook to make the changes

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class AddingServerUDP {
    // Static variable to store the shared integer sum
    private static int sum = 0;

    public static void main(String args[]) {
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[4]; // Use a 4-byte buffer for integers
        try {
            // Announce that the server is running
            System.out.println("Server started");

            // Create a BufferedReader to read input from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Prompt the user for the port number to listen on
            System.out.print("Enter the port number for the server to listen on (e.g., 6789): ");
            int serverPort = Integer.parseInt(reader.readLine());

            // Create a DatagramSocket to listen for incoming UDP packets
            aSocket = new DatagramSocket(serverPort);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while (true) {
                // Receive an incoming UDP packet (request) from a client
                aSocket.receive(request);

                // Used ChatGPT to get this line of code
                // Extract the integer value from the received packet
                int num = ByteBuffer.wrap(request.getData()).getInt();

                // Print the received request and the integer value
                System.out.println("Adding: " + num + " to "+sum);

                // Call method add with the received integer and get the updated sum
                int updatedSum = add(num);

                // Prepare and send the updated sum as a reply to the client
                // Used ChatGPT to get this line of code
                byte[] replyData = ByteBuffer.allocate(4).putInt(updatedSum).array();
                DatagramPacket reply = new DatagramPacket(replyData, replyData.length, request.getAddress(), request.getPort());
                aSocket.send(reply);

                // Print the new sum
                System.out.println("Returning sum of " + updatedSum + " to client");
            }
        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    /**
     * Adds the specified integer value to the shared sum.
     *
     * @param i The integer value to be added to the sum.
     * @return The updated sum after the addition.
     */
    public static int add(int i) {
        sum += i;
        return sum;
    }
}
