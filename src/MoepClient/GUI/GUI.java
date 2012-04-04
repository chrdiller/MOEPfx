package MoepClient.GUI;

import Moep.Karte;
import MoepClient.Interface;
import java.awt.event.MouseAdapter;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Beschreibt die GUI, über die der User mit dem Programm interagiert
 * @author Christian Diller
 */

public class GUI
{
    private Interface interfaceI;
    private Stage primaryStage;

    public GUI(Interface _interfaceI, MouseAdapter[] adapter, Stage _primaryStage)
    {
        interfaceI = _interfaceI;
        primaryStage = _primaryStage;
        start();
    }
    
    public void start()
    {
        primaryStage.setTitle("MOEPfx :-)");
        BorderPane root = new BorderPane();        

        //final TablePane table = new TablePane();
        final OwnHandPane cwp = new OwnHandPane(200);
        final CardWheelPane cwpLeft = new CardWheelPane(200);
        final CardWheelPane cwpTop = new CardWheelPane(200);
        final CardWheelPane cwpRight = new CardWheelPane(200);
        
        cwp.setStyle("-fx-background-color: brown");
        cwp.setTranslateY(125);
        //cwpLeft.setStyle("-fx-background-color: green");
        cwpLeft.setRotate(90);
        cwpLeft.setScaleX(0.5);
        cwpLeft.setScaleY(0.5);
        cwpLeft.setTranslateX(-75);
        //cwpTop.setStyle("-fx-background-color: red");
        cwpTop.setRotate(180);
        cwpTop.setScaleX(0.5);
        cwpTop.setScaleY(0.5);
        cwpTop.setTranslateY(-125);
        //cwpRight.setStyle("-fx-background-color: blue");
        cwpRight.setRotate(270);
        cwpRight.setScaleX(0.5);
        cwpRight.setScaleY(0.5);
        cwpRight.setTranslateX(75);
        
        root.setOnKeyTyped(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                switch (event.getCharacter()) {
                    case "w":
                        cwpTop.addCard(new Karte("2|2"));
                        break;
                    case "a":
                        cwpLeft.addCard(new Karte("2|2"));
                        break;
                    case "s":
                        cwp.addCard(new Karte("2|2"));
                        break;
                    case "d":
                        cwpRight.addCard(new Karte("2|2"));
                        break;
                }
            }
        });
       
        root.setLeft(cwpLeft);
        root.setTop(cwpTop);
        root.setRight(cwpRight);
        root.setBottom(cwp);
        //root.setCenter(table);

        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: #336699");

        primaryStage.setScene(new Scene(root, 1024, 768, Color.BLACK));
        primaryStage.show();
        root.setFocusTraversable(true);
    }

    public void ablageAktualisieren(Karte karte)
    {
        
    }

    public void ablageReset()
    {
        
    }

    public void setStatus(String stat)
    {
        
    }

    public void setSpielStatus(String stat)
    {
        
    }

    public void handAktualisieren(List<Karte> karten)
    {
        
    }

    public void serverGefunden(String serverName)
    {
        
    }

    public void spielerZahlAendern(int wert)
    {
        
    }
}
