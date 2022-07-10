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
    private final String _role = "typing_user";
    private final String _rolePass = "1234";
    private String _username;
    private String _pass;
    private final String _url = "jdbc:postgresql://localhost:5432/speedtyping";
    private Connection _con = null;

    private Connection getConnection(String role, String pass) throws SQLException{
        Connection con = DriverManager.getConnection(_url, role, pass);
        return con;
    }
    
    public void createRole() throws Exception {
        String createQuery = "CREATE USER " + _role + " WITH PASSWORD '" + _rolePass + "';"
                + "GRANT CONNECT ON DATABASE speedtyping TO " + _role + ";"
                + "GRANT INSERT, SELECT ON ALL TABLES IN SCHEMA public TO " + _role + ";"
                + "GRANT USAGE, SELECT ON SEQUENCE results_id_seq TO " + _role + ";"
                + "GRANT USAGE, SELECT ON SEQUENCE program_users_id_seq TO " + _role + ";";
        Connection con = null;
        try {
            con = getConnection("postgres", "1234");
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
    
    public void addUser(String username, String pass) throws Exception {
        try{
            String query = "INSERT INTO program_users(username, password) VALUES ('"+username+"', '"+pass+"');";
            execInsertQuery(query);
        }catch(SQLException ex){
            try{
                createRole();
                addUser(username, pass);
            }catch(SQLException ex1){throw ex;}
        }
    }
    
    public boolean checkUser(String username, String pass) throws Exception {
        boolean result = false;
        try{
            String query = "SELECT * FROM program_users WHERE username = '"+username+"' AND password = '"+pass+"';";
            ResultSet rs = execSelectQuery(query);
            if(!rs.next())
                result = false;
            else{
                _username = username;
                _pass = pass;
                result = true;
            }
        }catch(SQLException ex){
            try{
                createRole();
                result = true;
            }catch(SQLException ex1){throw ex;}
        }finally{
            return result;
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
            _con = getConnection(_role, _rolePass);
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
            _con = getConnection(_role, _rolePass);
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
        String query = "INSERT INTO results(user_id,test_date,speed,error_ratio) "
                + "VALUES ((SELECT id FROM program_users WHERE username = '"+_username+"'), "
                + "'" + date + "', " + (int) results[3] + ", " + results[4] + ");";
        execInsertQuery(query);
    }
    
    public List<String> getLastResult() throws Exception {
       String query = "SELECT c.username, a.test_date, a.speed, a.error_ratio "
               + "FROM results a INNER JOIN (SELECT MAX(a.test_date) mxdate FROM results a) b "
               + "ON a.test_date = b.mxdate "
               + "JOIN program_users c ON a.user_id = c.id WHERE c.username= '"+_username+"';";
        ResultSet rs = execSelectQuery(query);
        List<String> list = new ArrayList<>();
        if (rs.next()) {
            list.add(rs.getString(1));
            list.add(rs.getString(2));
            list.add(rs.getString(3));
            list.add(rs.getString(4));
        }
        return list;
    }
    
    public List<Object> getResults(int type) throws Exception {
        String query;
        switch(type){
            case 0:
                query = "SELECT  a.speed, a.test_date FROM results a "
                        + "JOIN program_users b ON a.user_id = b.id "
                        + "WHERE b.username = '"+_username+"' ORDER BY a.test_date";
                break;
            case 1:
                query = "SELECT  a.error_ratio, a.test_date FROM results a "
                        + "JOIN program_users b ON a.user_id = b.id "
                        + "WHERE b.username = '"+_username+"' ORDER BY a.test_date";
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
