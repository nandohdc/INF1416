package model;

import repository.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private String loginName;
    private String password;
    private String salt;
    private boolean blocked = false;
    private String timeBlocked;
    private int attempt = 0;
    private int totalAccess = 0;
    private String certificate;
    private int totalQuery = 0;
    private GroupDAO group;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getTimeBlocked() {
        return timeBlocked;
    }

    public void setTimeBlocked(String timeBlocked) {
        this.timeBlocked = timeBlocked;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getTotalAccess() {
        return totalAccess;
    }

    public void setTotalAccess(int totalAccess) {
        this.totalAccess = totalAccess;
    }

    public int getTotalQuery() {
        return totalQuery;
    }

    public void setTotalQuery(int totalQuery) {
        this.totalQuery = totalQuery;
    }

    public GroupDAO getGroup() {
        return group;
    }

    public void setGroup(GroupDAO group) {
        this.group = group;
    }

    public UserDAO(String loginName, String password, String salt, boolean blocked, String timeBlocked, int attempt, int totalAccess, String certificate, int totalQuery, GroupDAO group) {
        this.loginName = loginName;
        this.password = password;
        this.salt = salt;
        this.blocked = blocked;
        this.timeBlocked = timeBlocked;
        this.attempt = attempt;
        this.totalAccess = totalAccess;
        this.certificate = certificate;
        this.totalQuery = totalQuery;
        this.group = group;
    }

    public boolean save(){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO db_user (loginname, password, salt, blocked, timeblocked, attempt, totalaccess, certificate, totalquery, groupid) VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, loginName);
            ps.setString(2, password);
            ps.setString(3, salt);
            if(blocked)
                ps.setInt(4, 1);
            else
                ps.setInt(4, 0);
            ps.setString(5, timeBlocked);
            ps.setInt(6, attempt);
            ps.setInt(7, totalAccess);
            ps.setString(8, certificate);
            ps.setInt(9, totalQuery);
            ps.setInt(10, group.getId());
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE db_user SET password = ?, salt = ?, blocked = ?, timeblocked = ?, attempt = ?, totalaccess = ?, totalquery = ?, certificate = ? WHERE loginname = ?");
            ps.setString(1, password);
            ps.setString(2, salt);
            if(blocked)
                ps.setInt(3, 1);
            else
                ps.setInt(3, 0);
            ps.setString(4, timeBlocked);
            ps.setInt(5, attempt);
            ps.setInt(6, totalAccess);
            ps.setInt(7, totalQuery);
            ps.setString(8, certificate);
            ps.setString(9, loginName);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UserDAO get(String loginName){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `db_user` WHERE loginname=?");
            ps.setString(1, loginName);
            rs = ps.executeQuery();
            if (rs.next()) {
                UserDAO userDAO = new UserDAO(rs.getString("loginname"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        rs.getInt("blocked") == 1,
                        rs.getString("timeblocked"),
                        rs.getInt("attempt"),
                        rs.getInt("totalaccess"),
                        rs.getString("certificate"),
                        rs.getInt("totalquery"),
                        GroupDAO.get(rs.getInt("groupid")));
                ps.close();
                conn.close();
                return userDAO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
