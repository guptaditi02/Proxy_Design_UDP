//Aditi Gupta - argupta@andrew.cmu.edu - Project2Task4
// Used EchoClientUDP.java from Coulouris textbook to make the changes
//Used code from Lab 5 for separation of concerns

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class AddingClientUDP {
    // Server's listening port number
    private static int serverPort;

    public static void main(String args[]) {
        try {
            // Announce that the client is running
            System.out.println("The client is running.");

            // Create a BufferedReader to read input from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Prompt the user for the server side port number
            System.out.print("Enter the server side port number (e.g., 6789): ");
            serverPort = Integer.parseInt(reader.readLine());

            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

            // Continue to process user input until "halt!" command is given
            while ((nextLine = typed.readLine()) != null) {
                if (nextLine.trim().equalsIgnoreCase("halt!")) {
                    System.out.println("Client side quitting.");
                    break;
                }

                // Convert the input integer to a byte array and send it to the server
                int num = Integer.parseInt(nextLine);
                int updatedSum = add(num);

                System.out.println("The server returned : " + updatedSum);
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    // Used ChatGPT help to complete this method
    /**
     * Sends the provided integer value to the server and receives an updated sum
     * in response. This method handles the conversion of the integer to a byte array,
     * sending the request, and receiving and processing the server's reply.
     *
     * @param i The integer to be sent to the server for addition.
     * @return The updated sum received from the server, or -1 in case of an error.
     */

    public static int add(int i) {
        DatagramSocket socket = null;
        try {
            // Change the server address to "localhost"
            InetAddress host = InetAddress.getByName("localhost");

            // Create a DatagramSocket for sending and receiving UDP packets
            socket = new DatagramSocket();

            // Convert the integer 'i' to a 4-byte array
            byte[] intBytes = new byte[4];
            intBytes[0] = (byte) (i >> 24);
            intBytes[1] = (byte) (i >> 16);
            intBytes[2] = (byte) (i >> 8);
            intBytes[3] = (byte) i;

            // Send the byte array to the server
            DatagramPacket request = new DatagramPacket(intBytes, intBytes.length, host, serverPort);
            socket.send(request);

            // Receive the reply from the server (containing the updated sum)
            byte[] buffer = new byte[4];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);

            // Convert the received byte array back to an integer (the updated sum)
            // Used ChatGPT to get this line of code
            int updatedSum = ByteBuffer.wrap(reply.getData()).getInt();

            return updatedSum;
        } catch (IOException e) {
            System.out.println("Error in add method: " + e.getMessage());
            return -1; // Return an error value
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}
