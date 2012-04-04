
package MoepClient.GUI;

import Moep.Karte;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Christian
 */
public class OwnHandPane extends BorderPane
{   
    private CardWheelPane cwp;
    private final Image kartenSet = new Image(this.getClass().getResource("grafik/kartenSet.png").toString().substring(6));
    
    private ImageView openCard = new ImageView(kartenSet);
    private ImageView defaultCard = new ImageView(kartenSet);
    
    public OwnHandPane(int radius)
    {
        cwp = new CardWheelPane(radius);
        this.setCenter(cwp);
        defaultCard.setViewport(new Rectangle2D(12 * 166, 4 * 250, 166, 250));
        openCard.setViewport(new Rectangle2D(12 * 166, 4 * 250, 166, 250));
        this.setLeft(defaultCard);
        defaultCard.setTranslateY(-125);
        this.setRight(openCard);
        openCard.setTranslateY(-125);
    }
    
    public void addCard(Karte k)
    {
        cwp.addCard(k);
    }
}
