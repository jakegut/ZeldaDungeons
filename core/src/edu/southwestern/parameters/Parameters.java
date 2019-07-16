package edu.southwestern.parameters;

import edu.southwestern.tasks.gvgai.zelda.level.SimpleLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Used for processing and containing command line parameters.
 *
 * @author Jacob Schrum
 */
public class Parameters {

    public static Parameters parameters;
    public ParameterCollection<Integer> integerOptions;
    public ParameterCollection<Long> longOptions;
    public ParameterCollection<Boolean> booleanOptions;
    public ParameterCollection<Double> doubleOptions;
    public ParameterCollection<String> stringOptions;
    // Class can be any type, hence <T> details are inappropriate
    @SuppressWarnings("rawtypes")
    public ParameterCollection<Class> classOptions;

    /**
     * Initialize parameter collections of each needed type
     *
     * @param args Original String array of command line arguments
     */
    @SuppressWarnings("rawtypes")
    public Parameters(String[] args) {
        booleanOptions = new ParameterCollection<Boolean>();
        classOptions = new ParameterCollection<Class>();
        doubleOptions = new ParameterCollection<Double>();
        integerOptions = new ParameterCollection<Integer>();
        longOptions = new ParameterCollection<Long>();
        stringOptions = new ParameterCollection<String>();

        fillDefaults();
        parseArgs(args, true);
    }

    /**
     * Initialize the static Parameters instance using command line parameters.
     *
     * @param args String array from command line
     */
    public static void initializeParameterCollections(String[] args) {
        String logFile = getLogFilename(args);
        parameters = new Parameters(args);

        if (logFile != null) {
            System.out.println("File exists? " + logFile);
            File f = new File(logFile);
            if (f.getParentFile().exists() && f.exists()) {
                System.out.println("Load parameters: " + logFile);
                initializeParameterCollections(logFile);
                // Commandline can overwrite save file
                parameters.parseArgs(args, true);
            }
        }
        String base = parameters.stringParameter("base");
        if (base != null && !base.equals("")) {
            File baseDir = new File(base);
            if (!baseDir.exists() || !baseDir.isDirectory()) {
                System.out.println("Made directory: " + base);
                baseDir.mkdir();
            }
        }
        CommonConstants.load();
    }

    /**
     * Load file name filled with parameters and use contents to fille all parameter
     * collections in the standard static Parameters instance
     *
     * @param parameterFile file to load from
     */
    public static void initializeParameterCollections(String parameterFile) {
        if (parameters == null) {
            parameters = new Parameters(new String[0]);
        }
        System.out.println("Loading parameters from " + parameterFile);
        parameters.loadParameters(parameterFile);
        CommonConstants.load();
    }

    /**
     * Based on the String arguments passed at the command line, extract the path
     * and file name of the parameter log file (if it exists). This makes it easy to
     * check for an existing experiment run and resume it.
     *
     * @param args Same arguments passed to main from command line
     * @return path and file name of potential parameter log file.
     */
    public static String getLogFilename(String[] args) {
        String base = "";
        String saveTo = "";
        String log = "";
        String run = "";

        StringTokenizer st;
        String entity = "";
        String value = "";
        for (String arg : args) {
            try {
                st = new StringTokenizer(arg, ":");
                entity = st.nextToken();
                value = st.nextToken();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Problem parsing parameter tokens");
                System.exit(1);
            }
            if (entity.equals("saveTo")) {
                saveTo = value;
            } else if (entity.equals("log")) {
                log = value;
            } else if (entity.equals("runNumber")) {
                run = value;
            } else if (entity.equals("base")) {
                base = value;
            }
        }
        String logAfterHypen = "";
        for (int i = 0; i < log.length(); i++) {
            if (log.charAt(i) == '-') {
                logAfterHypen = log.substring(i + 1);
                break;
            }
        }
        if (!logAfterHypen.equals(saveTo)) {
            throw new IllegalArgumentException("string of log must equal string after hypen in saveTo");
        }
        if (base.equals("") && saveTo.equals("")) {
            return null;
        }
        return base + "/" + saveTo + run + "/" + log + run + "_parameters.txt";
    }

    /**
     * Load file name filled with parameters and use contents to fill all parameter
     * collections.
     *
     * @param filename File name to load parameters from
     */
    public void loadParameters(String filename) {
        try (Scanner file = new Scanner(new File(filename))) {
            ArrayList<String> args = new ArrayList<String>();
            while (file.hasNextLine()) {
                String line = file.nextLine();
                args.add(line);
            }
            String[] sArgs = new String[args.size()];
            parseArgs(args.toArray(sArgs), false);
        } catch (FileNotFoundException ex) {
            System.out.println("Could not read parameter file");
            System.exit(1);
        }
    }

    /**
     * Save parameters to the path and filename specified by the "base", "saveTo",
     * "log", and "runNumber" parameters
     */
    public void saveParameters() {
        String path = stringParameter("base") + "/" + stringParameter("saveTo") + integerParameter("runNumber");
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();

        }
        String name = stringOptions.get("log") + integerParameter("runNumber") + "_parameters.txt";
        this.saveParameters(path + "/" + name);
    }

    /**
     * Save parameters to specified filename
     *
     * @param filename Name of file to save parameters in
     */
    public void saveParameters(String filename) {
        // PrintStream will be cleaned up as part of the try
        try (PrintStream stream = new PrintStream(new FileOutputStream(filename))) {
            integerOptions.writeLabels(stream);
            longOptions.writeLabels(stream);
            booleanOptions.writeLabels(stream);
            doubleOptions.writeLabels(stream);
            stringOptions.writeLabels(stream);
            classOptions.writeLabels(stream);
        } catch (FileNotFoundException ex) {
            System.out.println("Could not save parameters");
            System.exit(1);
        }
    }

    /**
     * Define all parameter labels, default values, and help text
     */
    public final void fillDefaults() {
        // Integer parameters
        integerOptions.add("zeldaMaxHealth", 15, "Set the max health for the main character in the rouge-like.");
        integerOptions.add("rougeEnemyHealth", 2, "Max health of enemies");
        // Long parameters
        longOptions.add("lastGenotypeId", 0l, "Highest genotype id used so far");
        longOptions.add("lastInnovation", 0l, "Highest innovation number used so far");
        // Boolean parameters
        booleanOptions.add("gvgAIForZeldaGAN", false, "Use GVG-AI representation of Zelda game");

        booleanOptions.add("zeldaGANUsesOriginalEncoding", true, "True if the number of tiles for the GAN is 4, otherwise 10.");
        // Double parameters

        stringOptions.add("zeldaGANModel", "ZeldaDungeon01_5000_10.pth", "File name of GAN model to use for Zelda GAN level evolution");
        stringOptions.add("zeldaType", "original", "Specify which type of dungeon to load: original, generated, tutorial");
        // Class options
        classOptions.add("zeldaLevelLoader", SimpleLoader.class, "Loader to use when the dungeon is picking levels");
    }

    /**
     * Get boolean parameter with given label
     *
     * @param label Parameter label
     * @return corresponding boolean parameter label
     */
    public boolean booleanParameter(String label) {
        return booleanOptions.get(label);
    }

    /**
     * Get int parameter with given label
     *
     * @param label Parameter label
     * @return corresponding int parameter label
     */
    public int integerParameter(String label) {
        return integerOptions.get(label);
    }

    /**
     * Get long parameter with given label
     *
     * @param label Parameter label
     * @return corresponding long parameter label
     */
    public long longParameter(String label) {
        return longOptions.get(label);
    }

    /**
     * Get double parameter with given label
     *
     * @param label Parameter label
     * @return corresponding double parameter label
     */
    public double doubleParameter(String label) {
        return doubleOptions.get(label);
    }

    /**
     * Get String parameter with given label
     *
     * @param label Parameter label
     * @return corresponding String parameter label
     */
    public String stringParameter(String label) {
        return stringOptions.get(label);
    }

    /**
     * Get Class parameter with given label
     *
     * @param label Parameter label
     * @return corresponding Class parameter value
     */
    // Class needs to be raw because any type can be returned
    @SuppressWarnings("rawtypes")
    public Class classParameter(String label) {
        return classOptions.get(label);
    }

    /**
     * Parse all command line parameters of each type
     *
     * @param args                    The original String parameters
     * @param terminateOnUnrecognized Whether to exit program on invalid parameter
     */
    private void parseArgs(String[] args, boolean terminateOnUnrecognized) {
        if (args.length > 0 && args[0].equals("help")) {
            System.out.println("Paremeter help:");
            usage(0);
        }
        StringTokenizer st;
        String entity = "";
        String value = "";
        for (String arg : args) {
            try {
                st = new StringTokenizer(arg, ":");
                entity = st.nextToken();
                if (st.hasMoreTokens()) {
                    value = st.nextToken();
                } else {
                    value = "";
                }
            } catch (Exception e) {
                System.out.println("Problem parsing \"" + arg + "\"");
                usage(1);
            }
            if (integerOptions.hasLabel(entity)) {
                integerOptions.change(entity, Integer.parseInt(value));
                System.out.println("Integer value \"" + entity + "\" set to \"" + value + "\"");
            } else if (longOptions.hasLabel(entity)) {
                longOptions.change(entity, Long.parseLong(value));
                System.out.println("Long value \"" + entity + "\" set to \"" + value + "\"");
            } else if (doubleOptions.hasLabel(entity)) {
                doubleOptions.change(entity, Double.parseDouble(value));
                System.out.println("Double value \"" + entity + "\" set to \"" + value + "\"");
            } else if (booleanOptions.hasLabel(entity)) {
                booleanOptions.change(entity, Boolean.parseBoolean(value));
                System.out.println("Boolean value \"" + entity + "\" set to \"" + value + "\"");
            } else if (stringOptions.hasLabel(entity)) {
                stringOptions.change(entity, value);
                System.out.println("String value \"" + entity + "\" set to \"" + value + "\"");
            } else if (classOptions.hasLabel(entity)) {
                try {
                    classOptions.change(entity, Class.forName(value));
                } catch (ClassNotFoundException ex) {
                    System.out.println(value + " is not a valid class");
                    System.exit(1);
                }
                System.out.println("Class value \"" + entity + "\" set to \"" + value + "\"");
            } else {
                System.out.println("Did not recognize \"" + entity + "\" with value \"" + value + "\"");
                if (terminateOnUnrecognized) {
                    throw new IllegalArgumentException(entity + " is not a valid parameter");
                    // usage(1);
                }
            }
        }
    }

    /**
     * Show the descriptive help message of each parameter
     *
     * @param status The status that the program will exit with after showing the
     *               information.
     */
    public void usage(int status) {
        System.out.println("Usage:");
        System.out.println("Integer parameters:");
        integerOptions.showUsage();
        System.out.println("Long parameters:");
        longOptions.showUsage();
        System.out.println("Double parameters:");
        doubleOptions.showUsage();
        System.out.println("Boolean parameters:");
        booleanOptions.showUsage();
        System.out.println("String parameters:");
        stringOptions.showUsage();
        System.out.println("Class parameters:");
        classOptions.showUsage();
        System.exit(status);
    }

    /**
     * Set integer option value
     *
     * @param label label for int parameter
     * @param value new value
     */
    public void setInteger(String label, int value) {
        this.integerOptions.change(label, value);
    }

    /**
     * Set long option value
     *
     * @param label label for long parameter
     * @param value new value
     */
    public void setLong(String label, long value) {
        this.longOptions.change(label, value);
    }

    /**
     * Set double option value
     *
     * @param label label for double parameter
     * @param value new value
     */
    public void setDouble(String label, double value) {
        this.doubleOptions.change(label, value);
    }

    /**
     * Set boolean option value
     *
     * @param label label for boolean parameter
     * @param value new value
     */
    public void setBoolean(String label, boolean value) {
        this.booleanOptions.change(label, value);
    }

    /**
     * Change a boolean parameter to its opposite value
     *
     * @param string label for boolean parameter
     */
    public void changeBoolean(String label) {
        this.booleanOptions.change(label, !booleanOptions.get(label));
    }

    /**
     * Set String option value
     *
     * @param label label for String parameter
     * @param value new value
     */
    public void setString(String label, String value) {
        this.stringOptions.change(label, value);
    }

    /**
     * Set Class option value
     *
     * @param label label for Class parameter
     * @param value new value
     */
    @SuppressWarnings("rawtypes")
    public void setClass(String label, Class value) {
        this.classOptions.change(label, value);
    }
}
