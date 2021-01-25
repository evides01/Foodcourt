package th.com.samsen.tunyaporn.foodcourt.model;

public class Configuration {

    public static final String DATABASE_NAME = "dbpointtickets.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "tm_conf";

    private String ip;
    private String username;
    private String password;
    private String dbname;
    private String duration;
    private String printSize;

    public Configuration() {
    }

    public Configuration(String ip, String username, String password, String dbname, String duration, String printSize) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.dbname = dbname;
        this.duration = duration;
        this.printSize = printSize;
    }

    public class Column {
        public static final String ip = "IP";
        public static final String username = "Username";
        public static final String password = "Password";
        public static final String dbname = "DBName";
        public static final String duration = "Duration";
        public static final String printSize = "PrintSize";
    }



    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
}
