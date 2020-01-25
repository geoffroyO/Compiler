package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getPrintMethodsTable() {
        return printMethodsTable;
    }

    public boolean getParse() {
        return parse;
    }

    public boolean isVerification() {
        return verification;
    }

    public boolean isNoCheck() {
        return noCheck;
    }

    public boolean isParseRegistersNumber() {
        return parseRegistersNumber;
    }

    public int getRegistersNumber() {
        return registersNumber;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean printMethodsTable = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private boolean parseRegistersNumber = false;
    private int registersNumber = 16;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
        // - Commands Manager
        for(String arg : args){

            // - Check if we need to parse the registers number after the option '-r'
            if(parseRegistersNumber){
                registersNumber = Integer.parseInt(arg);
                if (registersNumber <= 4 || registersNumber > 16){
                    throw new UnsupportedOperationException("Registers number should be between 4 and 16 included");
                }
                // - after finishing parsing the registers number
                parseRegistersNumber = false;
            } else{
                switch (arg){
                    case "-b":
                        this.printBanner = true;
                        if (args.length!=1) {
                            throw new UnsupportedOperationException("The -b option is only to be used alone.");
                        }
                        break;
                    case "-p":
                        this.parse = true;
                        break;
                    case "-mt":
                        this.printMethodsTable = true;
                        break;
                    case "-v":
                        verification = true;
                        break;
                    case "-n":
                        noCheck = true;
                        break;
                    case "-r":
                        parseRegistersNumber = true;
                        break;
                    case "-d":
                        debug++;
                        break;
                    case "-P":
                        this.parallel = true;
                        break;
                    default:
                        // - create a file and add it to source files
                        File file = new File(arg);
                        if (!sourceFiles.contains(file)){
                            // Each file is compile once
                            sourceFiles.add(file);
                        }
                        break;

                }
            }
        }


        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

//        throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
