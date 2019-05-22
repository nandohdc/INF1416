package model;

import repository.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO {

    private int id;
    private String name;

    public GroupDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GroupDAO get(int id){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `db_group` WHERE id=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                GroupDAO groupDAO = new GroupDAO(rs.getInt("id"),
                        rs.getString("name"));
                ps.close();
                conn.close();
                return groupDAO;

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

}
