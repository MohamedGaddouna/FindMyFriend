package gaddounamohamed.grp1.findmyfriend;

public class Position {

    int idposition ;
    String longitude,latitude,numero,pseudo;

    public Position(String latitude, String longitude, String numero, String pseudo) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.numero = numero;
        this.pseudo = pseudo;
    }

    public Position(int idposition, String latitude, String longitude, String numero, String pseudo) {
        this.idposition = idposition;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numero = numero;
        this.pseudo = pseudo;
    }
}
