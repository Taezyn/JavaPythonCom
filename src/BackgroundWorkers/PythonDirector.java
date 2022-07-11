package BackgroundWorkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonDirector {
    public PythonDirector(){

    }

    public void call() throws IOException, InterruptedException {
        try {
            String winCommand = "python";
            String scriptPath = "pythonCode/trypython.py";

            // winCommand : Variable d'environnement
            // scriptPath : Chemin du script python à éxecuter
            // ID de la fonction python
            // Paire d'arguments nom-valeur (str uniquement)
            System.out.println("\n----- Python code begins -----\n");
            ProcessBuilder processBuilder = new ProcessBuilder(winCommand, scriptPath, "2", "a");
            processBuilder.redirectErrorStream();
            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
            String returnedString = null;

            while (line != null) {
                returnedString = line;
                System.out.println(returnedString);
                line = bufferedReader.readLine();
            }

//            String returnedValue = returnedString.split("returned ")[1];
//            System.out.println(returnedValue);

            int exitCode = process.waitFor();
            System.out.println("Python exit code : " + exitCode);
            process.destroy();
            System.out.println("\n----- Python code ended -----");
        } catch (Exception exception){
            System.err.println(exception);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PythonDirector pythonDirector = new PythonDirector();
        pythonDirector.call();
    }
}
