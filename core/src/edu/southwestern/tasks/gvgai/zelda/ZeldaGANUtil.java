package edu.southwestern.tasks.gvgai.zelda;

import edu.southwestern.tasks.mario.gan.GANProcess;
import edu.southwestern.tasks.mario.gan.reader.JsonReader;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ZeldaGANUtil {

    /**
     * Generate a Zelda room with the GAN, and then convert it to a String representation
     * that GVG-AI can turn into a level to play.
     *
     * @param latentVector  Vector that generates level
     * @param startLocation Where to place Zelda avatar in the level
     * @return String representation
     */
    public static String[] generateGVGAILevelFromGAN(double[] latentVector, Point startLocation) {
        List<List<Integer>> room = generateOneRoomListRepresentationFromGAN(latentVector);
        return ZeldaVGLCUtil.convertZeldaRoomListtoGVGAI(room, startLocation);
    }

    /**
     * Get one room in list form from a latent vector using the GAN.
     * The GANProcess type must be set to ZELDA before executing this method.
     *
     * @param latentVector Latent vector to generate room
     * @return One room in list form
     */
    public static List<List<Integer>> generateOneRoomListRepresentationFromGAN(double[] latentVector) {
        List<List<List<Integer>>> roomInList = getRoomListRepresentationFromGAN(latentVector);
        return roomInList.get(0); // Only contains one room
    }

    public static List<List<List<Integer>>> getRoomListRepresentationFromGAN(double[] latentVector) {
        assert GANProcess.type.equals(GANProcess.GAN_TYPE.ZELDA);
        latentVector = GANProcess.mapArrayToOne(latentVector); // Range restrict the values
        // Generate room from vector
        try {
            GANProcess.getGANProcess().commSend("[" + Arrays.toString(latentVector) + "]");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Cannot continue without the GAN process
        }
        String oneRoom = GANProcess.getGANProcess().commRecv(); // Response to command just sent
        oneRoom = "[" + oneRoom + "]"; // Wrap room in another json array
        // Create one room in a list
        List<List<List<Integer>>> roomInList = JsonReader.JsonToInt(oneRoom);
        return roomInList;
    }
}
