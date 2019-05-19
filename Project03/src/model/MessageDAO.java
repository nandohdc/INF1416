package model;

import repository.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDAO {

    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageDAO(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static MessageDAO get(int id){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `db_message` WHERE id=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                MessageDAO messageDAO = new MessageDAO(rs.getInt("id"),
                        rs.getString("text"));
                ps.close();
                conn.close();
                return messageDAO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
