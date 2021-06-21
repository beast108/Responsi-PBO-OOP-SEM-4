package responsi;

public class UserSession {
    static String id_user, nama;
    static int role;

    public static void setId_user(String vid_user){ id_user = vid_user; }
    public static void setNama(String vnama) { nama = vnama; }
    public static void setRole(int vrole){ role = vrole; }

    public static String getId_user(){ return id_user; }
    public static String getNama(){ return nama; }
    public static int getRole(){ return role; }

}
