package practice.speedtyping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataBaseSession {

    private String _username;
    private String _pass;
    private static String _url = "jdbc:postgresql://localhost:5432/speedtyping";
    private Connection _con = null;

    public DataBaseSession(String username, String pass) throws Exception {
        _username = username;
        _pass = pass;
        checkUser();
    }

    static public void createUser(String username, String pass) throws Exception {
        String createQuery = "CREATE USER " + username + " WITH PASSWORD '" + pass + "';"
                + "GRANT CONNECT ON DATABASE speedtyping TO " + username + ";"
                + "GRANT INSERT, SELECT ON ALL TABLES IN SCHEMA public TO " + username + ";"
                + "GRANT USAGE, SELECT ON SEQUENCE results_id_seq TO " + username + ";";
        Connection con = null;
        try {
            con = DriverManager.getConnection(_url, "postgres", "1234");
            Statement st = con.createStatement();
            st.execute(createQuery);
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (con != null)
                try {
                con.close();
            } catch (SQLException ex) {
            }
        }
    }

    private void checkUser() throws Exception {
        try {
            _con = DriverManager.getConnection(_url, _username, _pass);
            _con.close();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (_con != null)
                try {
                _con.close();
            } catch (SQLException ex) {
            }
        }
    }

    public List<String> getWords(int count, int lang) throws Exception {
        String query = "SELECT * FROM words WHERE lang_id =" + lang + " ORDER BY RANDOM() LIMIT " + count + ";";
        ResultSet rs = execSelectQuery(query);
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getString(3));
        }
        return list;
    }

    private ResultSet execSelectQuery(String query) throws Exception {
        try {
            _con = DriverManager.getConnection(_url, _username, _pass);
            Statement st = _con.createStatement();
            return st.executeQuery(query);
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (_con != null)
                try {
                _con.close();
            } catch (SQLException ex) {
            }
        }
    }

    private void execInsertQuery(String query) throws Exception {
        try {
            _con = DriverManager.getConnection(_url, _username, _pass);
            PreparedStatement pst = _con.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (_con != null)
                try {
                _con.close();
            } catch (SQLException ex) {
            }
        }
    }

    public void insertResult(Object[] results) throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(calendar.getTime());
        String query = "INSERT INTO results(username,test_date,speed,error_ratio) VALUES('" + _username + "', '" + date + "', " + (int) results[3] + ", " + results[4] + ");";
        execInsertQuery(query);
    }

    public List<String> loadLastResult() throws Exception {
        String query = "SELECT a.* FROM results a INNER JOIN "
                + "(SELECT username, MAX(test_date) mxdate FROM results "
                + "WHERE username = '"+_username+"' GROUP BY username ) "
                + "b ON a.username = b.username AND a.test_date = b.mxdate;";
        ResultSet rs = execSelectQuery(query);
        List<String> list = new ArrayList<>();
        if (rs.next()) {
            list.add(rs.getString(2));
            list.add(rs.getString(3));
            list.add(rs.getString(4));
            list.add(rs.getString(5));
        }
        return list;
    }
    
    public List<Object> loadResults(int type) throws Exception {
        String query;
        switch(type){
            case 0:
                query = "SELECT  speed, test_date FROM results WHERE username = '"+_username+"' ORDER BY test_date";
                break;
            case 1:
                query = "SELECT  error_ratio, test_date FROM results WHERE username = '"+_username+"' ORDER BY test_date";
                break;
            default:
                throw new Exception();
        }
        ResultSet rs = execSelectQuery(query);
        List<Object> list = new ArrayList<>();
        while (rs.next()) {
            list.add(rs.getFloat(1));
            list.add(rs.getString(2));
        }
        return list;
    }
}
