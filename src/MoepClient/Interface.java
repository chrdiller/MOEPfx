package MoepClient;

import Moep.Statusmeldung;
import MoepClient.netzwerk.ServerSuche;
import Moep.Karte;
import Moep.Ressourcen;
import MoepClient.GUI.GUI;
import MoepClient.netzwerk.Verbindung;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.Stage;
import javax.swing.event.PopupMenuListener;

/**
 * Die Interface-Klasse, der Knotenpunkt zwischen Netzwerk, GUI und Moep
 * @author Christian Diller & Philipp Herrle
 */
public class Interface
{

    private boolean dran; //Sperrt die Spieleraktionen, wenn false
    public boolean eingeloggt; //Unterdrückt verbindungVerlorenEvents, wenn false
    private GUI gui; //Die GUI, einmal erzeugt
    private Moep m; //Informationsverwaltung, bei jeder Spielsession neu erzeugt
    private Verbindung verbindung; //Die Verbindung zum Server, bei jeder Spielsession neu erzeugt
    private Map<String, String> server; //Speichert per Broadcast gefundene Server
    private Spielerverwaltung spieler; //Verwaltet die Spieler
    private ServerSuche serversuche; //Für den UDP-Bradcast zuständig

    public Interface(Stage primaryStage)
    {
        server = new HashMap<String, String>();
        server.put("Online spielen", "christian-diller.info"); //Aktiviert das Online-Feature

        //<editor-fold defaultstate="collapsed" desc="Mouse-Adapter">
        /*MouseAdapter[] adapter = new MouseAdapter[]{
            new MouseAdapter()
            { //KarteZiehen

                @Override
                public void mousePressed(MouseEvent me)
                {
                    zieheKarte();
                }
            },
            new MouseAdapter()
            { //KarteLegen

                @Override
                public void mousePressed(MouseEvent me)
                {
                    Hand hand = (Hand) me.getComponent();
                    int index = hand.gibIndexKarte(me.getPoint());

                    if (index != -1) {
                        karteLegen(index);
                    }
                }
            },
            new MouseAdapter()
            { //spielerDialog

                @Override
                public void mousePressed(final MouseEvent me)
                {
                    SpielerDialog spDialog = new SpielerDialog(spieler);
                    spDialog.addWindowListener(new WindowAdapter()
                    {

                        @Override
                        public void windowClosing(WindowEvent e)
                        {
                            spieler = ((SpielerDialog) e.getSource()).gibSpielerverwaltung();
                            gui.setEnabled(true);
                        }
                    });
                    gui.setEnabled(false);
                }
            },
            new MouseAdapter()
            { //Erstellen

                @Override
                public void mousePressed(MouseEvent me)
                {
                    m = new Moep();
                    InitPanel ip = (InitPanel) me.getComponent().getParent();
                    JButton erstellenBtn = ip.gibErstellenButton();
                    JButton beitretenBtn = ip.gibBeitretenButton();
                    if (!erstellenBtn.isEnabled()) {
                        return;
                    }
                    if ("Erstellen".equals(erstellenBtn.getText())) {
                        if (spieler.istGueltig() && (!"Servername".equals(ip.gibErstellenServername()) && !"".equals(ip.gibErstellenServername()))) {
                            serverErstellen(ip.gibErstellenServername());
                            beitretenBtn.setEnabled(false);
                            erstellenBtn.setText("Beenden");
                            eingeloggt = true;
                        } else {
                            Statusmeldung.fehlerAnzeigen("Bitte erst einen Servernamen eingeben und die Spieler konfigurieren");
                        }
                    } else {
                        verbindung.serverBeenden();
                        beitretenBtn.setEnabled(true);
                        erstellenBtn.setText("Erstellen");
                    }
                }
            },
            new MouseAdapter()
            { //Beitreten

                @Override
                public void mousePressed(MouseEvent me)
                {
                    m = new Moep();
                    InitPanel ip = (InitPanel) me.getComponent().getParent();
                    JButton erstellenBtn = ip.gibErstellenButton();
                    JButton beitretenBtn = ip.gibBeitretenButton();
                    if (!beitretenBtn.isEnabled()) {
                        return;
                    }
                    if ("Beitreten".equals(beitretenBtn.getText())) {
                        if (!"".equals(ip.gibName()) && !"Spielername".equals(ip.gibName())&& !"Auswählen...".equals(ip.gibServername())) {
                            verbindung = new Verbindung(server.get(ip.gibServername()), Interface.this);
                            if(verbindung.anmelden(ip.gibName())) {
                                if(ip.gibServername() != "Online spielen")
                                    Statusmeldung.infoAnzeigen("Willkommen auf dem MoepServer " + ip.gibServername());
                                else
                                    Statusmeldung.infoAnzeigen("Willkommen auf den Online-MoepServern");
                                erstellenBtn.setEnabled(false);
                                beitretenBtn.setText("Verlassen");
                            }
                            else
                                Statusmeldung.fehlerAnzeigen("Server nicht erreichbar");
                        } else {
                            Statusmeldung.fehlerAnzeigen("Bitte erst Spielernamen und Server angeben");
                        }
                    } else {
                        verbindung.schliessen();
                        logout();
                        Statusmeldung.infoAnzeigen("Logout erfolgreich");
                        erstellenBtn.setEnabled(true);
                        beitretenBtn.setText("Beitreten");
                    }

                }
            },
            new MouseAdapter()
            { //MoepButton

                @Override
                public void mousePressed(MouseEvent e)
                {
                    if (dran) {
                        verbindung.sendeMoepButton();
                        playSound("moep");
                    } else {
                        playSound("beep");
                    }
                }
            }
        };*/
        //</editor-fold>
        PopupMenuListener popupListener = new PopupMenuListener()
        {

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt)
            {
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt)
            {
                Interface.this.serverSuchen();
            }

            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt)
            {
            }
        };

        dran = false;
        eingeloggt = false;

        gui = new GUI(this, null, primaryStage);

        Statusmeldung.infoAnzeigen("Willkommen bei MOEP!");

        spieler = new Spielerverwaltung(new String[][]{{"", ""}, {"", ""}, {"", ""}, {"", ""}});

        serversuche = new ServerSuche(Interface.this);
        serverSuchen();
    }

    //<editor-fold defaultstate="collapsed" desc="Karten: Ziehen, Legen, Empfangen">
    private void zieheKarte()
    {
        if (!dran) {
            playSound("beep");
        } else {
            playSound("click");
            verbindung.sendeKarteZiehen();
            gui.handAktualisieren(m.gibHand());
        }
    }

    private void karteLegen(int index)
    {
        if (!dran) {
            playSound("beep");
            return;
        }
        m.zuLegen(index);
        if (!verbindung.sendeKarteLegen(m.gibKarteAt(index))) {
            Statusmeldung.fehlerAnzeigen("Karte konnte nicht gesendet werden");
        } else {
            playSound("click");
        }
    }

    public void karteEmpfangen(Karte karte)
    {
        m.ziehen(karte);
        gui.handAktualisieren(m.gibHand());
    }

    public void ablageAkt(Karte k)
    {
        gui.ablageAktualisieren(k);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serversteuerung: zugLegal, dranSetzen, status, kick">
    public void zugLegal(boolean legal, int illegalArt)
    {
        if (legal) {
            m.legen();
            gui.handAktualisieren(m.gibHand());
        } else {
            playSound("beep");
            if (illegalArt == 1) {
                m.legen();
            }
            gui.handAktualisieren(m.gibHand());
        }

    }

    public void dranSetzen(boolean dranH)
    {
        dran = dranH;
    }

    public void status(String statusH)
    {
        m.addNachricht(statusH);
        gui.setStatus(m.gibStatus());
    }

    public void kick(String Grund)
    {
        if (eingeloggt) {
            Statusmeldung.warnungAnzeigen("Vom Server gekickt: " + Grund);
            logout();
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FarbeWuenschen">
    public void farbeWuenschenAnfrage()
    {
        /*FarbeWuenschenDialog farbeWuenschenDialog = new FarbeWuenschenDialog(this);
        gui.setEnabled(false);*/
    }

    public void sendeFarbeWuenschenAntwort(int farbe)
    {
        /*verbindung.sendeFarbeWuenschenAntwort(farbe);
        gui.setEnabled(true);*/
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Mitspieler-Aktionen">
    public void mitspielerLogin(String name, int position)
    {
        m.mitspielerLogin(name, position);
        gui.setSpielStatus(m.gibSpielerliste());
        gui.spielerZahlAendern(1);
    }

    public void mitspielerLogout(String name)
    {
        m.mitspielerLogout(name);
        gui.setSpielStatus(m.gibSpielerliste());
        gui.spielerZahlAendern(-1);
    }

    public void mitspielerAmZug(String spielername)
    {
        m.mitspielerAmZug(spielername);
        gui.setSpielStatus(m.gibSpielerliste());
    }

    public void spielerKartenzahlUpdate(String spielername, int kartenzahl)
    {
        m.mitspielerKartenzahlUpdate(spielername, kartenzahl);
        gui.setSpielStatus(m.gibSpielerliste());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="spielEnde, logout, beenden">
    public void spielEnde(boolean gewonnen)
    {
        m.setzeHand(new ArrayList<Karte>());
        gui.handAktualisieren(m.gibHand());

        gui.ablageReset();

        gui.setSpielStatus(m.gibSpielerliste());
        gui.setStatus(m.gibStatus());

        if (gewonnen) {
            playSound("applause");
        } else {
            playSound("ooh");
        }
    }

    private void logout()
    {
        eingeloggt = false;
        dran = false;
        verbindung.schliessen();

        gui.spielerZahlAendern(0);
        m.statusLeeren();
        spielEnde(false);
    }

    public void beenden()
    {
        System.exit(0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Server-Suche">
    public void serverGefunden(String serverName, String serverAdresse)
    {
        if (server.get(serverName) == null) {
            server.put(serverName, serverAdresse);
            gui.serverGefunden(serverName);
        } else {
            gui.serverGefunden(null);
        }
    }

    private void serverSuchen()
    {
        serversuche.suchen();
        Statusmeldung.infoAnzeigen("Serverliste wurde aktualisiert");
    }
    //</editor-fold>

    public void verbindungVerloren()
    {
        if (eingeloggt) {
            Statusmeldung.fehlerAnzeigen("Verbindung verloren");
            logout();
        }
    }

    private void playSound(String soundName)
    {
        Ressourcen.gibClip(soundName).play();
    }

    public void serverErstellen(String servername)
    {
        if (serversuche.istEinzigerServer()) {
            if (spieler.istGueltig()) {
                verbindung = new Verbindung(spieler, servername, Interface.this);
                verbindung.anmelden(spieler.gibEigenenNamen());
            }
        } else {
            Statusmeldung.fehlerAnzeigen("Auf diesem PC läuft bereits ein Server");
        }
    }
}
