package MoepClient.netzwerk;

import Moep.Karte;

/**
 * Beschreibt das Packet, mit dem der Client dem Server die zu legende Karte übermittelt
 * Client -> Server
 * @author Christian Diller
 */
public class Packet13KarteLegen extends Packet
{

    private Karte karte;

    public Packet13KarteLegen(Karte _karte)
    {
        karte = _karte;
    }

    @Override
    public String gibData()
    {
        return "13" + seperator + karte.gibDaten();
    }

    @Override
    public void clientEventAufrufen(Verbindung verbindung)
    {
        //Kein ClientEvent
    }
}
