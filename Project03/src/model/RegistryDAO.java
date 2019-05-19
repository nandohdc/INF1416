package model;

import repository.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegistryDAO {

    private int messageId;
    private UserDAO userId;
    private String file;
    private String created;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public UserDAO getUserId() {
        return userId;
    }

    public void setUserId(UserDAO userId) {
        this.userId = userId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public RegistryDAO(int messageId, UserDAO userId, String file, String created) {
        this.messageId = messageId;
        this.userId = userId;
        this.file = file;
        this.created = created;
    }

    public boolean save(){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO db_registry (messageid, userid, file) VALUES(?,?,?)");
            ps.setInt(1, messageId);
            if(userId != null)
                ps.setString(2, userId.getLoginName());
            else
                ps.setString(2, null);
            ps.setString(3, file);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<RegistryDAO> getAll(){
        ArrayList<RegistryDAO> registryDAOS = new ArrayList<>();
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `db_registry`");
            rs = ps.executeQuery();
            while (rs.next()) {
                RegistryDAO registryDAO = new RegistryDAO(rs.getInt("messageid"),
                        UserDAO.get(rs.getString("userid")),
                        rs.getString("file"),
                        rs.getString("created"));

                registryDAOS.add(registryDAO);

            }
            ps.close();
            conn.close();
            return registryDAOS;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
