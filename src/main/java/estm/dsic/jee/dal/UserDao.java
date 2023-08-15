package estm.dsic.jee.dal;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import estm.dsic.jee.controllers.Login;
import jakarta.annotation.Resource;
public class UserDao {
    @Resource(name = "jdbc/mydb")
    private DataSource mydb;
    private Connection cnx;
    private Statement stm;
    private ResultSet rs;
   
    
    public boolean auth(String email,String passwd,String type){

            try {
                cnx=mydb.getConnection();
                stm=cnx.createStatement();
                rs=stm.executeQuery("SELECT * FROM Users WHERE user_email='"+email+"' AND user_password='"+passwd+"' AND user_type ='"+type+"';");
                if(rs.next()) {
                    return true;
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
    }

    public Login getUserByEmail(String email) {
        Login user = null;
        PreparedStatement stm = null;
        try {
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("SELECT * FROM Users WHERE email = ?");
            stm.setString(1, email);
            rs = stm.executeQuery();
            if (rs.next()) {
                user = new Login();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPasswd(rs.getString("passwd"));
                user.setUserType(rs.getString("user_type"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    
}
