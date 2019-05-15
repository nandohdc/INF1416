package model;

import repository.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private String loginName;
    private String name;
    private String password;
    private boolean blocked = false;
    private String timeBlocked;
    private int attempt = 0;
    private int totalAccess = 0;
    private String certificateFactory;
    private int totalQuery = 0;
    private String publicKey;
    private GroupDAO group;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCertificateFactory() {
        return certificateFactory;
    }

    public void setCertificateFactory(String certificateFactory) {
        this.certificateFactory = certificateFactory;
    }

    public int getTotalQuery() {
        return totalQuery;
    }

    public void setTotalQuery(int totalQuery) {
        this.totalQuery = totalQuery;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public GroupDAO getGroup() {
        return group;
    }

    public void setGroup(GroupDAO group) {
        this.group = group;
    }

    public UserDAO(String loginName, String name, String password, boolean blocked, String timeBlocked, int attempt, int totalAccess, String certificateFactory, int totalQuery, String publicKey, GroupDAO group) {
        this.loginName = loginName;
        this.name = name;
        this.password = password;
        this.blocked = blocked;
        this.timeBlocked = timeBlocked;
        this.attempt = attempt;
        this.totalAccess = totalAccess;
        this.certificateFactory = certificateFactory;
        this.totalQuery = totalQuery;
        this.publicKey = publicKey;
        this.group = group;
    }

    public boolean save(){
        Connection conn = MySQLConnection.getMySQLConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO user (loginname, name, password, blocked, timeblocked, attempt, totalaccess, certificatefactory, totalquery, publickey, groupid) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, loginName);
            ps.setString(2, name);
            ps.setString(3, password);
            if(blocked)
                ps.setInt(4, 1);
            else
                ps.setInt(4, 0);
            ps.setString(5, timeBlocked);
            ps.setInt(6, attempt);
            ps.setInt(7, totalAccess);
            ps.setString(8, certificateFactory);
            ps.setInt(9, totalQuery);
            ps.setString(10, publicKey);
            ps.setInt(11, group.getGid());
            ps.executeUpdate();
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
            ps = conn.prepareStatement("SELECT * FROM `user` WHERE loginname=?");
            ps.setString(1, loginName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new UserDAO(rs.getString("loginname"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("blocked") == 1,
                        rs.getString("timeblocked"),
                        rs.getInt("attempt"),
                        rs.getInt("totalaccess"),
                        rs.getString("certificatefactory"),
                        rs.getInt("totalquery"),
                        rs.getString("publickey"),
                        GroupDAO.get(rs.getInt("groupid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
