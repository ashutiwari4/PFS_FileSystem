package fileSystem;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * class to set the bitmap to determine the free blocks
 */
public class FileSystemUtils {

    /**
     * @param i
     * @return
     */
    public static byte[] convertIntToArrayBytes(int i) {

        final ByteBuffer tempByteBuffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        tempByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        tempByteBuffer.putInt(i);
        return tempByteBuffer.array();

    }

    /**
     * @param b
     * @return
     */
    public static int convertByteArray(byte[] b) {
        final ByteBuffer tempByteBuffer = ByteBuffer.wrap(b);
        tempByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return tempByteBuffer.getInt();

    }


    /**
     * Creates Root folder with the name PFS
     */
    public static void createDirectory(String name) {
        String path = System.getProperty("user.home") + "/Desktop/" + name;
        System.out.println(path);
        File theDir = new File(path);
        if (!theDir.exists()) {
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                se.printStackTrace();
            }
            if (result) {
                System.out.println("Directory successfully created!");
            }
        }
    }

}
