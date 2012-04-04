
package MoepClient.GUI;

import Moep.Karte;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Christian
 */
public class TablePane extends BorderPane
{
    private final Image kartenSet = new Image(this.getClass().getResource("grafik/kartenSet.png").toString().substring(6));
    
    private ImageView openCard = new ImageView(kartenSet);
    private ImageView defaultCard = new ImageView(kartenSet);

    public TablePane()
    {
        super();
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setManaged(false);
        updateTable();
    }
    
    public void changeCard(Karte card)
    {
        //...
        updateTable();
    }
    
    public void playDrawAnimation()
    {
        
    }

    private void updateTable()
    {
        this.getChildren().clear();
        defaultCard.setViewport(new Rectangle2D(12 * 166, 4 * 250, 166, 250));
        this.setLeft(defaultCard);
        openCard.setViewport(new Rectangle2D(12 * 166, 4 * 250, 166, 250));
        this.setRight(openCard);
    }
}
