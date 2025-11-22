package gaddounamohamed.grp1.findmyfriend;

public class Config {

    public static String IP_Server = "10.0.2.2";

    // URL du script PHP
    public static String URL_GetAll_Locations = "http://" + IP_Server + "/servicephp/get_all.php";

    // URL pour ajouter une position (à compléter si tu as add.php)
    public static String URL_Add_Location = "http://" + IP_Server + "/servicephp/add_position.php";
}
