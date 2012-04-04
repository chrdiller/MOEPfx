
package MoepClient.GUI;

import Moep.Karte;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author Christian
 */
public class CardWheelPane extends StackPane
{
    private double radius;
    private ArrayList<Karte> cards = new ArrayList<Karte>();
    private final Image kartenSet = new Image(this.getClass().getResource("grafik/kartenSet.png").toString().substring(6));
    
    public CardWheelPane(int _radius)
    {
        super();
        radius = _radius;
        this.setAlignment(Pos.CENTER);
    }
    
    public void addCard(Karte card)
    {
        cards.add(card);
        //CARDS SORTIEREN!!
        updateCardWheel();
    }

    private void removeCard(Karte card)
    {
        cards.remove(card);
        //CARDS SORTIEREN!!
        updateCardWheel();
    }
    
    private void updateCardWheel()
    {
        this.getChildren().clear(); 
        
        double degrees = (cards.size() < 20 ? 180 / 20 : 180 / cards.size());
            double initRot = degrees * (cards.size() - 1) / 2;

            for (Karte k : cards) {
                final Timeline wheelTimeline = new Timeline();
                final ImageView imgView = new ImageView(kartenSet);
                final double rot = initRot - degrees * cards.indexOf(k);
                
                imgView.setViewport(new Rectangle2D(12 * 166, 4 * 250, 166, 250));
                imgView.setSmooth(true);
                imgView.setId(""+cards.indexOf(k));
                imgView.setOpacity(0.75);
                
                wheelTimeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO,
                        new KeyValue(imgView.rotateProperty(), 0),
                        new KeyValue(imgView.translateXProperty(), 0),
                        new KeyValue(imgView.translateYProperty(), -radius)
                    ),
                    new KeyFrame(new Duration(500),
                        new KeyValue(imgView.rotateProperty(), rot),
                        new KeyValue(imgView.translateXProperty(), radius * Math.sin(rot/360*2*Math.PI)),
                        new KeyValue(imgView.translateYProperty(), radius * -Math.cos(rot/360*2*Math.PI))
                    )
                );
                
                imgView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent me)
                    {
                        if(wheelTimeline.getStatus() == Animation.Status.RUNNING)
                            return;
                        Timeline timeline = new Timeline();
                        timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                new KeyValue(imgView.rotateProperty(), rot),
                                new KeyValue(imgView.translateXProperty(), radius * Math.sin(rot/360*2*Math.PI)),
                                new KeyValue(imgView.translateYProperty(), radius * -Math.cos(rot/360*2*Math.PI)),
                                new KeyValue(imgView.opacityProperty(), 0.75)
                            ),
                            new KeyFrame(new Duration(100),
                                new KeyValue(imgView.rotateProperty(), rot),
                                new KeyValue(imgView.translateXProperty(), (radius + 15) * Math.sin(rot/360*2*Math.PI)),
                                new KeyValue(imgView.translateYProperty(), (radius + 15) * -Math.cos(rot/360*2*Math.PI)),
                                new KeyValue(imgView.opacityProperty(), 1)
                            )
                        );
                        timeline.play();
                    }  
                });
                
                imgView.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent me)
                    {
                        if(wheelTimeline.getStatus() == Animation.Status.RUNNING)
                            return;
                        Timeline timeline = new Timeline();
                        timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                new KeyValue(imgView.rotateProperty(), rot),
                                new KeyValue(imgView.translateXProperty(), (radius + 10) * Math.sin(rot/360*2*Math.PI)),
                                new KeyValue(imgView.translateYProperty(), (radius + 10) * -Math.cos(rot/360*2*Math.PI)),
                                new KeyValue(imgView.opacityProperty(), 1)
                            ),
                            new KeyFrame(new Duration(100),
                                new KeyValue(imgView.rotateProperty(), rot),
                                new KeyValue(imgView.translateXProperty(), radius * Math.sin(rot/360*2*Math.PI)),
                                new KeyValue(imgView.translateYProperty(), radius * -Math.cos(rot/360*2*Math.PI)),
                                new KeyValue(imgView.opacityProperty(), 0.75)
                            )
                        );
                        timeline.play();
                    }  
                });
                imgView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent me)
                    {
                        if(wheelTimeline.getStatus() == Animation.Status.RUNNING)
                            return;
                        Timeline timeline = new Timeline();
                        timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                new KeyValue(imgView.opacityProperty(), 1.0)
                            ),
                            new KeyFrame(new Duration(200),
                                new KeyValue(imgView.opacityProperty(), 0.0)
                            )
                        );
                        timeline.play();
                        timeline.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent ae)
                            {
                                CardWheelPane.this.removeCard(cards.get(Integer.parseInt(imgView.getId())));
                            }
                        });
                    }  
                });
                
                this.getChildren().add(imgView);
                wheelTimeline.play();  
            }
    }
}
