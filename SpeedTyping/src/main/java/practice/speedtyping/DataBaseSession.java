package practice.speedtyping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseSession {
    private String _username;
    private String _pass;
    private static String _url = "jdbc:postgresql://localhost:5432/speedtyping";
    private Connection _con = null;
    
    public DataBaseSession(String username, String pass) throws Exception{
        _username = username;
        _pass = pass;
        checkUser();
    }
    
    static public void createUser(String username, String pass) throws Exception{
        String createQuery = "CREATE USER "+username+" WITH PASSWORD '"+pass+"';"
                + "GRANT CONNECT ON DATABASE speedtyping TO "+username+";"
                + "GRANT INSERT, SELECT ON ALL TABLES IN SCHEMA public TO "+username+";";
        Connection con = null;
        try{
            con = DriverManager.getConnection(_url, "postgres", "1234");
            Statement st = con.createStatement();
            st.execute(createQuery);
        } 
        catch (SQLException ex) {
            throw ex;
        }finally{
            if(con!=null)
                try {con.close();} catch (SQLException ex) {}
        }
    }
    
    private void checkUser() throws Exception{
        try{
            _con = DriverManager.getConnection(_url, _username, _pass);
            _con.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }finally{
            if(_con!=null)
                try {_con.close();} catch (SQLException ex) {}
        }
    }
    
    public List<String> getWords(int count,int lang) throws Exception{
        try {
            String query = "SELECT * FROM words WHERE lang_id ="+lang+" ORDER BY RANDOM() LIMIT "+count+";";
            ResultSet rs = executeQuery(query);
            List<String> list = new ArrayList<>();
            while(rs.next()){
                list.add(rs.getString(3));
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseSession.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    private ResultSet executeQuery(String query) throws Exception{
        try {
            _con = DriverManager.getConnection(_url, _username, _pass);
                Statement st = _con.createStatement();
                return st.executeQuery(query);
        } catch (SQLException ex) {
            throw ex;
        }finally{
            if(_con!=null)
                try {_con.close();} catch (SQLException ex) {}
        }
    }
}