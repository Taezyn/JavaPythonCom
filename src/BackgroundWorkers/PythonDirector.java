package BackgroundWorkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PythonDirector {
    public HashMap<String, String> functions;
//    String winCommand = "python"; // Si tu as python en variable d'environnement windows
    String winCommand = "C:\\Program Files\\Python\\WPy64-3950\\python-3.9.5.amd64\\python.exe"; // Le chemin complet sinon
    String scriptPath = "pythonCode/JavaReciever.py"; // Le chemin (relatif suffit) du script contenant les fonction a executer en python

    public PythonDirector(){
        // Maintenir une coherence entre ce dictionnaire et celui en python
        functions = new HashMap<String, String>();
        functions.put("function1", "1");
        functions.put("function2", "2");
    }

    public ArrayList call(String function, ArrayList<String> javaArguments) throws IOException, InterruptedException {
        String functionID = functions.get(function);

        String pythonArguments = "";
        // Ne sachant pas d'avance combien d'argument on va appeler, on va mettre une seule longue chaine
        // de caracteres avec un délimiteur bien indentifiable (ici '@') sur lequel on va iterer en python.
        // Nota : on ne peut transmettre que des strings. Pour ce qui est trop contraignant --> fichiers (txt, csv, ...)
        switch (functionID){
            case "1":
                // Ici je sais que je suis dans la fonction 1, j'ai donc besoin de trois arguments.
                // je vais donc effectuer autant de fois l'appel que j'ai d'arguments a passer en
                // les mettant sous la forme 'arg1@val1@arg2@val2@arg3@val3'
                pythonArguments += "arg1_f1@" + javaArguments.get(0) + "@";
                pythonArguments += "arg2_f1@" + javaArguments.get(1) + "@";
                pythonArguments += "arg3_f1@" + javaArguments.get(2);
                // On note que le dernier argument n'est pas marque par un @.
                break;

            case "2":
                pythonArguments += "arg1_f2@" + javaArguments.get(0) + '@';
                pythonArguments += "arg2_f2@" + javaArguments.get(1);
                break;
        }

        try {
            // winCommand : Variable d'environnement // chemin vers python
            // scriptPath : Chemin du script python à éxecuter
            // ID de la fonction python
            // Paire d'arguments nom-valeur (str uniquement)
            System.out.println("\n----- Python code begins -----\n");

            // Trust my shit :)
            ProcessBuilder processBuilder = new ProcessBuilder(winCommand, scriptPath, functionID, pythonArguments);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Va lire tout les prints de python et les placer dans une liste qu'on traitera ensuite.
            String line = bufferedReader.readLine();
            ArrayList<String> returnedStrings = new ArrayList<String>();
            while (line != null) {
                returnedStrings.add(line);
                System.out.println(line);
                line = bufferedReader.readLine();
            }

            // Recupere l'entier qui termine les fonctions python dans l'instruction exit().
            // Peut servir a lever des erreurs/exceptions ici
            int exitCode = process.waitFor();
            System.out.println("Python system exit code : " + exitCode);
            process.destroy();

            System.out.println("\n----- Python code ended -----");

            // Ici on va traiter la liste returnedStrings. Sachant ce que l'on a print en python,
            // on sait exactement quoi recuperer et ou. On va mettre ces resultats, directement au bon type
            // dans une liste multi-types finalResults, qui sera renvoyee, meme vide, par notre fonction.
            ArrayList finalResults = new ArrayList();
            switch (functionID){
                case "1":
                    // On ne fait rien, la fonction est informative.
                    break;

                case "2":
                    System.out.println(returnedStrings);
                    // On recupere le resultat de la multiplication
                    finalResults.add(Float.parseFloat(returnedStrings.get(1).split(" : ")[1]));
                    // et de l'addition
                    finalResults.add(Float.parseFloat(returnedStrings.get(2).split(" : ")[1]));
                    break;
            }
            return finalResults;

        } catch (Exception exception){
            System.err.println(exception);
        }
        return new ArrayList(); // Parce que java fait chier
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PythonDirector pythonDirector = new PythonDirector();

        System.err.println("Exemple fonction 1 : ");
        ArrayList<String> javaArguments = new ArrayList<String>();
        javaArguments.add("text");
        javaArguments.add("3.14");
        javaArguments.add("True");
        pythonDirector.call("function1", javaArguments);

        Thread.sleep(500); // Important pour laisser le buffer de retour se vider entierement.

        System.err.println("\nExemple fonction 2 : ");
        javaArguments.clear();
        javaArguments.add("5");
        javaArguments.add("2");
        ArrayList res = pythonDirector.call("function2", javaArguments);
        // Attention, ici c'est une arraylist d'Objects. Il sera peut etre necessaire de transtyper ensuite.

        // Si les fonctions python sont longues a l'execution, il peut etre benefique de faire l'appel
        // a pythonDirector.call() dans un thread parallele, au risque de geler l'interface.
    }
}
