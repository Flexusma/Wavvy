package de.flexusma.wavybot.utils;

import net.dv8tion.jda.core.JDA;

import java.awt.dnd.DropTarget;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;

public class Database {
    static String url = BotToken.url;
    static String user = BotToken.user;
    static String password = BotToken.password;
    boolean isInit = false;
    static Connection con = null;


    public static Connection getCon(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean initDB() {
        System.out.println("Connecting to DB...");
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Database connected!");
            isInit = true;
            con = connection;

            return true;
        } catch (SQLException e) {
            System.out.println("Cannot connect the database! " + e);
            return false;
        }

    }


    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO preferences( id," +
            //"name," +
            " s_pref) VALUES (?, " +
            //"?," +
            " ?)";

    private static final String SQL_SERIALIZE_UOBJECT = "UPDATE preferences SET s_pref = ? WHERE id = ?";
    private static final String SQL_DESERIALIZE_OBJECT = "SELECT s_pref FROM preferences WHERE id = ?";

    public static void prefToDB(Connection connection,String id,
                                Object objectToSerialize) throws SQLException {

        PreparedStatement pstmt = connection
                .prepareStatement(SQL_SERIALIZE_OBJECT);

        // just setting the class name
        pstmt.setString(1, id);
        //pstmt.setString(2, objectToSerialize.getClass().getName());
        pstmt.setObject(2, objectToSerialize);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Java object serialized to database. Object: "
                + objectToSerialize);
    }

    public static void uprefToDB(Connection connection,String id,
                                Object objectToSerialize) throws SQLException {

        PreparedStatement pstmt = connection
                .prepareStatement(SQL_SERIALIZE_UOBJECT);

        // just setting the class name
        pstmt.setString(2, id);
        //pstmt.setString(2, objectToSerialize.getClass().getName());
        pstmt.setObject(1, objectToSerialize);
        pstmt.executeUpdate();
        pstmt.close();
        System.out.println("Java object Update-serialized to database. Object: "
                + objectToSerialize);
    }


    public static Preferences prefFromDB(Connection connection,
                                    String id) throws SQLException, IOException,
            ClassNotFoundException {
        PreparedStatement pstmt = connection
                .prepareStatement(SQL_DESERIALIZE_OBJECT);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        // Object object = rs.getObject(1);

        byte[] buf = rs.getBytes(1);
        ObjectInputStream objectIn = null;
        if (buf != null)
            objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

        Preferences deSerializedObject = (Preferences)objectIn.readObject();

        rs.close();
        pstmt.close();

        System.out.println("Java object de-serialized from database. Object: "
                + deSerializedObject + " Classname: "
                + deSerializedObject.getClass().getName());
        connection.close();
        return deSerializedObject;
    }


    public static Preferences initPref(JDA jda, String id){
        try {
            return Database.prefFromDB(Database.getCon(), id);
        }catch (Exception e){
            e.printStackTrace();
            try {
                prefToDB(Database.getCon(),id,new Preferences());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return new Preferences();
        }
    }

    public static void savePref(JDA jda, String id, Preferences pref){

        try {
            prefToDB(Database.getCon(),id,pref);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                uprefToDB(Database.getCon(),id,pref);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }


  /*  // serializing java object to mysql database
    long serialized_id = serializeJavaObjectToDB(connection, obj);

    // de-serializing java object from mysql database
    Vector objFromDatabase = (Vector) deSerializeJavaObjectFromDB(
            connection, serialized_id);

        connection.close();*/


}
