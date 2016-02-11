package db;
import java.sql.*;

public class UsersService {

    static {
        try {
            Class.forName("com.oracle.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "chat", "1234");
    }

    private void closeConnection(Connection connection) {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException ex) {
        }
    }

    public boolean isBanned(String name) {
        String sql = "select * from users where name ='" + name + "'";
        String ban = null;
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ban = String.valueOf(resultSet.getObject("ban"));
                if (ban.equals("ban"))
                    return true;
            }
        } catch (SQLException e) {
            System.out.print(e);
        } finally {
            closeConnection(conn);
        }
        return false;
    }

    public boolean isUserExist(String name) {
        String sql = "select * from users where name =?";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.getRow() == 0 && !resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isCorrectMsg(String msg)
    {
        String delims = "[ .,?!]+";
        String[] tokens = msg.split(delims);
        for(String s : tokens)
        {
            System.out.println(s);
            if(s.equals("con"))
                return false;
        }
        return true;
    }
    public void insert(String name) {
        String sql = "insert into users (id,name,ban) values" +
                "(USR_ID.nextval," + "'" + name + "','')";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public void update(String name) {
        String sql2 = "update users set ban='ban'" +
                "where name=" + "'" + name + "'";
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(sql2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public void delete() {

    }
}
