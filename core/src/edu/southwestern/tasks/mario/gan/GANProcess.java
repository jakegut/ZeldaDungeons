package edu.southwestern.tasks.mario.gan;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;

import edu.southwestern.parameters.Parameters;
import edu.southwestern.tasks.gvgai.zelda.ZeldaVGLCUtil;
import edu.southwestern.util.PythonUtil;

public class GANProcess extends Comm {
	
	public static final String PYTHON_BASE_PATH = "." + File.separator + "python" + File.separator + "GAN" + File.separator;
	// Program for converting a latent vector to a level via a GAN
	public static final String WASSERSTEIN_PATH = PYTHON_BASE_PATH + "generator_ws.py";
	
	////////// These are static variables representing the active GAN process (only one) ////////////////
	
	private static GANProcess ganProcess = null;

	public enum GAN_TYPE {MARIO, ZELDA};
	
	public static GAN_TYPE type = GAN_TYPE.MARIO;
	
	public static int latentVectorLength() {
		return getGANProcess().getLatentVectorSize();
	}
	
	/**
	 * Destroy GAN process so a new one can be started
	 */
	public static void terminateGANProcess() {
		if(ganProcess != null) {
			ganProcess.process.destroy();
			ganProcess = null;
		}
	}
	
	/**
	 * Start the GAN process running in Python if it has not started already.
	 * Otherwise, just return the reference to the process.
	 * @return Process running the Mario GAN
	 */
	public static GANProcess getGANProcess() {
		// This code comes from the constructor for MarioEvalFunction in the MarioGAN project
		if(ganProcess == null) {
			PythonUtil.setPythonProgram();
			// set up process for GAN
			switch(type) {
			// Default constructor is for Mario
			case MARIO: 
				ganProcess = new GANProcess(); 
				break;
			// Details for Zelda will change as code develops
			case ZELDA: 
				ganProcess = new GANProcess(PYTHON_BASE_PATH+"ZeldaGAN"+ File.separator +Parameters.parameters.stringParameter("zeldaGANModel"),
											Parameters.parameters.integerParameter("GANInputSize"),
											Parameters.parameters.booleanParameter("zeldaGANUsesOriginalEncoding") ? 4 : 6);
				break;
			}
			ganProcess.start();
			// consume all start-up messages that are not data responses
			String response = "";
			while(!response.equals("READY")) {
				response = ganProcess.commRecv();
			}
		}
		return ganProcess;
	}
	
	/**
	 * From MarioGAN
	 * 
	 * Map the value in R to (-1, 1)
	 * @param valueInR
	 * @return Range restricted value
	 */
	public static double mapToOne(double valueInR) {
		return valueInR;
		
		// Jacob: The code below is part of the original Dagstuhl MarioGAN repo, likely written by
		// Vanessa or Jialin, but I can't figure out why this was done. Perhaps it has something to
		// do with the way that CMA-ES genotypes work or with how those values are mapped, but in
		// my code the genotype values seem to already be properly restricted to the right range,
		// which makes this weird code unnecessary.
		
		//return ( valueInR / Math.sqrt(1+valueInR*valueInR) );
	}

	/**
	 * From MarioGAN
	 * 
	 * Perform the operation above to a whole array
	 * 
	 * @param arrayInR
	 * @return Array with values in range
	 */
	public static double[] mapArrayToOne(double[] arrayInR) {
		double[] newArray = new double[arrayInR.length];
		for(int i=0; i<newArray.length; i++) {
			double valueInR = arrayInR[i];
			newArray[i] = mapToOne(valueInR);
		}
		return newArray;
	}

	//////////Code below here is associated with the GAN instance ////////////////
	
	String GANPath = null;
	int GANDim = -1; 
	int GANTileTypes = -1;

	/**
	 * Loads the Mario GAN trained on the specified model with the specified latent vector size
	 */
	public GANProcess() {
		this(PYTHON_BASE_PATH + "MarioGAN" + File.separator + Parameters.parameters.stringParameter("marioGANModel"), 
			 Parameters.parameters.integerParameter("GANInputSize"), 
			 Parameters.parameters.booleanParameter("marioGANUsesOriginalEncoding") ? 10 : 13);
	}

	/**
	 * This option allows for different GAN models than the default one.
	 * These models could be trained on different level sets, or may use
	 * different numbers of inputs for the latent variable.
	 * @param GANPath Path to GAN pth file
	 * @param GANDim Input size
	 */
	public GANProcess(String GANPath, int GANDim, int numTiles) {
		super();
		this.threadName = "GANThread";
		this.GANPath = GANPath;
		this.GANDim = GANDim;
		this.GANTileTypes = numTiles;
	}

	/**
	 * Length of each latent input vector
	 * @return
	 */
	public int getLatentVectorSize() {
		return GANDim;
	}
	
	/**
	 * Launch GAN, this should be called only once
	 */
	public void launchGAN() {
		if(!(new File(PythonUtil.PYTHON_EXECUTABLE).exists())) {
			throw new RuntimeException("Before launching this program, you need to place the path to your "+
									   "Python executable in my_python_path.txt within the main MM-NEAT directory.");
		}

		// Run program with model architecture and weights specified as parameters
		ProcessBuilder builder = new ProcessBuilder(PythonUtil.PYTHON_EXECUTABLE, WASSERSTEIN_PATH, this.GANPath, ""+this.GANDim, ""+GANTileTypes);
		builder.redirectError(Redirect.INHERIT); // Standard error will print to console
		try {
			System.out.println(builder.command());
			this.process = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Buffers used for communicating with process via stdin and stdout
	 */
	@Override
	public void initBuffers() {
		//Initialize input and output
		if (this.process != null) {
			this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			this.writer = new PrintStream(this.process.getOutputStream());
			System.out.println("Process buffers initialized");
		} else {
			printErrorMsg("GANProcess:initBuffers:Null process!");
		}
	}

	/**
	 * GAN process running in background, ready to accept latent vectors
	 */
	@Override
	public void start() {
		try {
			launchGAN();
			initBuffers();
			printInfoMsg(this.threadName + " has started");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}