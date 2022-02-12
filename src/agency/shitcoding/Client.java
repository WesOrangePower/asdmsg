package agency.shitcoding;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static final byte PING = 0;
    public static final byte GET_KEY = 1;
    public static final byte SIGN_UP = 2;
    public static final byte SIGN_IN = 3;
    public static final byte SEND_TEXT = 4;
    public static final byte SEND_FILE = 5;

    public Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void handle() throws IOException{
        byte command = this.dataInputStream.readByte();

        Main.LOGGER.debug("Received command " + command);

        switch (command){
            case PING:
                break;
            case GET_KEY:
                int byteCount = this.dataInputStream.readInt();
                byte[] bytes = new byte[byteCount];
                int bytesRead = this.dataInputStream.read(bytes, 0, byteCount);
                Main.LOGGER.debug("Read " + bytesRead);
                break;
            case SIGN_IN:
                break;
            case SIGN_UP:
                break;
            default:
                throw new IOException();
        }
    }

    public void authorization() throws IOException {

        // Client sends a pubkey/authorization request

        // If pubkey is unknown, server replies with registration notice.

        // Server creates a random message for client to sign

        // Server sends the random message

        // Client sends the signed message

        // Server decrypts the signed message

        // If messages are equal, the client is considered authorized.
//        this.stream
    }

}