package fr.ensimag.deca;

import java.io.File;

import fr.ensimag.deca.tree.AbstractProgram;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.util.concurrent.*;


/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl13
 * @date 01/01/2020
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();

        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("[Project GL 2020][TEAM gl13]");
            System.out.println(">> MORIN Lucas\n" +
                    ">> NAVARRO Jérémy\n" +
                    ">> ODEH Majd\n" +
                    ">> OUDOUMANESSAH Geoffroy\n" +
                    ">> POUGET Sylvain");
            // Exit program (0 = successfully)
            System.exit(0);

        }

        // - if command is "decac" without arguments
        if (args.length == 0) {
            System.out.println("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]\n");
            System.out.println("-b (banner) : affiche une bannière indiquant le nom de l’équipe\n");
            System.out.println("-p (parse) : arrête decac après l’étape de construction de" +
                    "l’arbre, et affiche la décompilation de ce dernier" +
                    "(i.e. s’il n’y a qu’un fichier source à" +
                    "compiler, la sortie doit être un programme" +
                    "deca syntaxiquement correct)\n");
            System.out.println("-v (verification) : arrête decac après l’étape de vérifications" +
                    "(ne produit aucune sortie en l’absence d’erreur)\n");
            System.out.println("-n (no check) : supprime les tests de débordement à l’exécution" +
                    "- débordement arithmétique" +
                    "- débordement mémoire" +
                    "- déréférencement de null\n");
            System.out.println("-r X (registers) : limite les registres banalisés disponibles à" +
                    "R0 ... R{X-1}, avec 4 <= X <= 16\n");
            System.out.println("-d (debug) : active les traces de debug. Répéter" +
                    "l’option plusieurs fois pour avoir plus de" +
                    "traces.\n");
            System.out.println("-P (parallel) : s’il y a plusieurs fichiers sources," +
                    "lance la compilation des fichiers en" +
                    "parallèle (pour accélérer la compilation)\n");
            System.out.println("N.B. Les options ’-p’ et ’-v’ sont incompatibles.\n");
            // Exit program (0 = successfully)
            System.exit(0);
        }

        if (options.getSourceFiles().isEmpty()) {
            System.out.println("File not found, please choose a '.deca' file.");
            // Exit program (1 = successfully)
            System.exit(1);
            //            throw new UnsupportedOperationException("decac without argument not implemented!");
        }

        if (options.getParallel()) {
            // Compilation en parallele

            List<Callable<Boolean>> taskToDo = new LinkedList<Callable<Boolean>>(); // List of task to do

            for (File f : options.getSourceFiles()) {
                // Create a task for each file

                Callable<Boolean> task = () -> {
                    DecacCompiler compiler = new DecacCompiler(options, f);
                    return(compiler.compile());
                };

                taskToDo.add(task);
            }

            ExecutorService pCompiler = Executors.newFixedThreadPool(java.lang.Runtime.getRuntime().availableProcessors()); // Init a threat for each core available


            List<Future<Boolean>> resCompiler = pCompiler.invokeAll(taskToDo); // Run all the tasks

            for (Future<Boolean> res : resCompiler)
            {
                if (res.get())
                // Error detected
                {
                   System.exit(1);
                }
            }

            System.exit(0);

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
