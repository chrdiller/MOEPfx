
import MoepClient.Interface;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Herzlich Willkommen im MOEPfx-Quellcode! :)
 * Dies ist Version 1.0 (Testbuild)
 * -- Christian Diller am 04.04.2012
 * 
 * Die Startklasse mit der Main-Methode
 * Hier wird eine neue Interface-Instanz erzeugt
 * @author Christian Diller
 */

public class MOEPfx extends Application
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        new Interface(primaryStage);
    }
}
