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

public class DataBaseConnection {
    private String _username;
    private String _pass;
    private static String _url = "jdbc:postgresql://localhost:5432/speedtyping";
    private Connection _con = null;
    
    public DataBaseConnection(String username, String pass) throws Exception{
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
   
     
    private ResultSet executeQuery(String query){
        ResultSet rs = null;
        try {
            _con = DriverManager.getConnection(_url, _username, _pass);
                Statement st = _con.createStatement();
                rs = st.executeQuery(query);
                return rs;
        } catch (SQLException ex) {
            return rs;
        }finally{
            if(_con!=null)
                try {_con.close();} catch (SQLException ex) {}
        }
    }
}
