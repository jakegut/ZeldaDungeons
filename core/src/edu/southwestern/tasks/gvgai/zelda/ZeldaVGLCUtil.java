package edu.southwestern.tasks.gvgai.zelda;

import edu.southwestern.util.random.RandomNumbers;
import me.jakerg.rougelike.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ZeldaVGLCUtil {

    public static final String ZELDA_LEVEL_PATH = "data/VGLC/Zelda/Processed/";
    public static final int ZELDA_ROOM_COLUMNS = 16; // This is actually the room height from the original game, since VGLC rotates rooms
    public static final int ZELDA_ROOM_ROWS = 11; // Equivalent to width in original game
    public static final boolean USE_OLD_ENCODING = false;

    /**
     * Takes VGLC room and converts it to a list of lists of numeric codes
     *
     * @param room String array representing one dungeon room
     * @return List of lists representing one room
     */
    public static List<List<Integer>> convertZeldaLevelVGLCtoRoomAsList(String[] room) {
        List<List<Integer>> result = new ArrayList<>(ZELDA_ROOM_ROWS);
        for (int i = 0; i < room.length; i++) {
            result.add(new ArrayList<Integer>(ZELDA_ROOM_COLUMNS)); // Next row of room
            for (int j = 0; j < room[i].length(); j++) {
                int code = (USE_OLD_ENCODING) ? convertZeldaTileVGLCtoNumberCode(room[i].charAt(j)) :
                        convertZeldaModifiedTileVGLCtoNumberCode(room[i].charAt(j));
                result.get(i).add(code);
            }
        }
        return result;
    }

    /**
     * Take Zelda level as a String array (converted from VGLC file) and
     * return a version with tiles swapped to those used by GVG-AI.
     *
     * @param level
     * @param startLocation This location is replaced with the 'A' tile for the avatar, unless it is null
     * @return
     */
    public static String[] convertZeldaLevelVGLCtoGVGAI(String[] level, Point startLocation) {
        String[] result = new String[level.length];
        for (int i = 0; i < level.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < level[i].length(); j++) {
                char tile = convertZeldaTileVGLCtoGVGAI(level[i].charAt(j));
                if (new Point(i, j).equals(startLocation)) { // Replace designated start location with Zelda avatar
                    tile = 'A';
                }
                sb.append(tile);
            }
            result[i] = sb.toString();
        }
        return result;
    }

    /**
     * Take a single list of lists of tile codes representing a single Zelda room
     * and convert it into a GVG-AI level String representation.
     *
     * @param room          One room as a list of lists
     * @param startLocation Where the Zelda avatar should be placed in the room (override tile code)
     * @return String array of the level representation
     */
    public static String[] convertZeldaRoomListtoGVGAI(List<List<Integer>> room, Point startLocation) {
        String[] result = new String[room.size()];
        for (int i = 0; i < room.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < room.get(i).size(); j++) {
                char tile = convertZeldaTileCodestoGVGAI(room.get(i).get(j));
                if (new Point(i, j).equals(startLocation)) { // Replace designated start location with Zelda avatar
                    tile = 'A';
                }
                sb.append(tile);
            }
            result[i] = sb.toString();
        }
        return result;
    }

    /**
     * VGLC uses the following tiles:
     * F = FLOOR
     * B = BLOCK
     * M = MONSTER
     * P = ELEMENT (LAVA, WATER)
     * O = ELEMENT + FLOOR (LAVA/BLOCK, WATER/BLOCK)
     * I = ELEMENT + BLOCK
     * D = DOOR
     * S = STAIR
     * W = WALL
     * - = VOID
     * <p>
     * Whereas GVG-AI uses these tiles
     * w = wall
     * g = gate
     * + = key
     * A = avatar (This is never used)
     * 1,2,3 = different enemies
     * . = floor
     * <p>
     * The original game is much more complex. This is an attempt at a simple mapping.
     * The mapping also assigns arbitrary number codes for GAN learning.
     * F -> 0:.
     * B -> 1:w (in Zelda, blocks are sometimes movable, though the corpus does not indicate which ones can move)
     * M -> 2:Random choice between 1, 2, and 3
     * P -> 1:w (in Zelda, projectiles can move over these tiles)
     * O -> 0:. (not fully clear what this represents, but I think it is a passable tile)
     * I -> 1:w (not clear what this is either)
     * D -> 3:g (This is probably not a semantically appropriate choice for GVG-AI, but it should look good)
     * S -> 3:g (Also questionable, though a staircase probably is a way of exiting an area)
     * W -> 1:w
     * - -> 0:.
     *
     * @param tile From VGLC
     * @return Corresponding tile for GVG-AI version of Zelda
     */
    public static char convertZeldaTileVGLCtoGVGAI(char tile) {
        switch (tile) {
            case 'F':
            case 'O':
            case '-':
                return '.';
            case 'P':
                return 'p';
            case 'B':
            case 'I':
            case 'W':
                return 'w';
            case 'M':
                return (char) ('1' + RandomNumbers.randomGenerator.nextInt(3)); // 1, 2, or 3
            case 'D':
            case 'S':
                return 'g';
            default:
                throw new IllegalArgumentException("Invalid Zelda tile from VGLV: " + tile);
        }
    }

    /**
     * Take the code for an individual tile output by the ZeldaGAN and convert to a GVG-AI
     * tile character.
     *
     * @param code 0 through 3
     * @return Corresponding GVG-AI code
     */
    public static char convertZeldaTileCodestoGVGAI(int code) {
        switch (code) {
            case 0:
                return '.';
            case 1:
                return 'w';
            case 2:
                return (char) ('1' + RandomNumbers.randomGenerator.nextInt(3)); // 1, 2, or 3 : Random Monster
            case 3:
                return 'g';
            case 4:
                return 'e';
            case 5:
                return 'K';
            case 6:
                return '+';
            case 7:
                return 'B';
            case 8:
                return 'b';
            case 9:
                return 'R';
            case 10:
                return 'G';
            case -7:
                return 'B';
            case -5:
                return 'K';
            default:
                throw new IllegalArgumentException("Invalid GAN code for Zelda: code = " + code);
        }
    }

    /**
     * Shares the mapping above, but returns the arbitrary number code.
     * Used for GAN learning.
     *
     * @param tile A VGLC tile
     * @return Number code associated with different classes of tile
     */
    public static int convertZeldaTileVGLCtoNumberCode(char tile) {
        switch (tile) {
            case 'F':
            case 'O':
            case '-':
                return 0;    // Passable
            case 'B':
            case 'P':
            case 'I':
            case 'W':
                return 1;    // Impassable
            case 'M':
                return 0;    // Monster
            case 'D':
            case 'S':
                return 3;    // Door
            default:
                throw new IllegalArgumentException("Invalid Zelda tile from VGLV: " + tile);
        }
    }

    /**
     * Shares the mapping above, but returns the arbitrary number code.
     * Used for GAN learning.
     *
     * @param tile A VGLC tile
     * @return Number code associated with different classes of tile
     */
    public static int convertZeldaModifiedTileVGLCtoNumberCode(char tile) {
        switch (tile) {
            case 'F':
            case '-':
            case 'O':
                return 0; // Passable
            case 'W':
            case 'B':
            case 'I':
                return 1;
            case 'P':
                return 5;
            case 'M':
                return 5;    // Monster
            case 'D':
                return 1;
            case 'S':
                return 1;    // Door
            case '#':
                return -6; // Ladder block
            case 'U':
                return Tile.MOVABLE_BLOCK_UP.getNum();
            case 'L':
                return Tile.MOVABLE_BLOCK_LEFT.getNum();
            case 'V':
                return Tile.MOVABLE_BLOCK_DOWN.getNum();
            case 'R':
                return Tile.MOVABLE_BLOCK_RIGHT.getNum();
            default:
                throw new IllegalArgumentException("Invalid Zelda tile from VGLV: " + tile);
        }
    }


    /**
     * Used to generate the original json files for training the Zelda GAN
     * @param args
     */
//	public static void main(String[] args) {
//		Parameters.initializeParameterCollections(new String[] {});
//		//MMNEAT.loadClasses();
//
//		VGDLFactory.GetInstance().init();
//		VGDLRegistry.GetInstance().init();
//
//		String game = "zeldacopy";
//		String gamesPath = "data/gvgai/examples/gridphysics/";
//		String game_file = gamesPath + game + ".txt";
//		int playerID = 0;
//		int seed = 0;
//
//		HashSet<List<List<Integer>>> roomSet = new HashSet<>();
//		
//		for(int i = 1; i <= 9; i++) {
//			for(int j = 1; j <= 2; j++) {
//				String file = "tloz"+i+"_"+j+".txt";
//				String[] level = convertZeldaLevelFileVGLCtoGVGAI(ZELDA_LEVEL_PATH+file, new Point(2,2));
//
//				for(String line : level) {
//					System.out.println(line);
//				}
//
//				List<List<List<Integer>>> roomList = convertZeldaLevelFileVGLCtoListOfRooms(ZELDA_LEVEL_PATH+file);
//
//				roomSet.addAll(roomList);
//				
//				//System.out.println(roomList);
//
//				
//				// PLAY THE LEVEL
//				Agent agent = new Agent();
//				agent.setup(null, 0, true); // null = no log, true = human 
//
//				Game toPlay = new VGDLParser().parseGame(game_file); // Initialize the game
//				GVGAIUtil.runOneGame(toPlay, level, true, agent, seed, playerID);
//
//				MiscUtil.waitForReadStringAndEnterKeyPress();
//			}
//		}
//		
//		System.out.println(roomSet);
//	}

}
