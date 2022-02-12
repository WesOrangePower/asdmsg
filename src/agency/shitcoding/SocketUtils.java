package agency.shitcoding;

import java.io.*;
import java.net.Socket;

public class SocketUtils
{
    public static boolean readBoolean(Socket socket) throws IOException
    {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        return dataInputStream.readBoolean();
    }

    public static void writeBoolean(Socket socket, boolean value) throws IOException
    {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeBoolean(value);
        dataOutputStream.flush();
    }

    public static int readInt(Socket socket) throws IOException
    {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        return dataInputStream.readInt();
    }

    public static void writeInt(Socket socket, int value) throws IOException
    {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeInt(value);
        dataOutputStream.flush();
    }

    public static String readUTF(Socket socket) throws IOException
    {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        return dataInputStream.readUTF();
    }

    public static void writeUTF(Socket socket, String value) throws IOException
    {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(value);
        dataOutputStream.flush();
    }

    public static Object readObject(Socket socket) throws IOException, ClassNotFoundException
    {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        return objectInputStream.readObject();
    }

    public static void writeObject(Socket socket, Object value) throws IOException
    {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(value);
        objectOutputStream.flush();
    }

    public static void readFile(Socket socket, File destination) throws IOException
    {
        destination.getParentFile().mkdirs();
        if (destination.exists()) destination.delete();


        // Piece of code stolen from https://github.com/04xRaynal/File-Transfer_Java-Socket begins
        int bytesRead, current;
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        int fileLength = dataInputStream.readInt();
        byte[] byteArray = new byte[fileLength];

        BufferedInputStream bufferedInputStream = new BufferedInputStream(dataInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));

        bytesRead = bufferedInputStream.read(byteArray, 0, byteArray.length);
        current = bytesRead;

        do
        {
            bytesRead = bufferedInputStream.read(byteArray, current, (byteArray.length - current));

            if (bytesRead >= 0)
                current += bytesRead;
        } while (bytesRead > 0);

        bufferedOutputStream.write(byteArray, 0, current);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        // Piece of code stolen from https://github.com/04xRaynal/File-Transfer_Java-Socket ends
    }

    public static void writeFile(Socket socket, File source) throws IOException
    {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] byteArray = new byte[(int) source.length()];
        dataOutputStream.writeInt(byteArray.length);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(dataOutputStream);

        int count;
        while ((count = bufferedInputStream.read(byteArray)) != -1) bufferedOutputStream.write(byteArray, 0, count);

        bufferedInputStream.close();
        bufferedOutputStream.flush();
    }
}
