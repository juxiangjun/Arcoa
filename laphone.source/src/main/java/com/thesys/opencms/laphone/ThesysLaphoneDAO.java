/**
 * 
 */
package com.thesys.opencms.laphone;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;


/**
 * @author maggie
 *
 */
public abstract class ThesysLaphoneDAO {	

    /** The log object for this class. */
    protected static final Log LOG = CmsLog.getLog(ThesysLaphoneDAO.class);
 

    protected void init(){
    	
    }
    
    /**
     * Returns a connection to the db pool configured in parameter "db-pool" of module 
     * "com.alkacon.opencms.formgenerator".<p>
     * 
     * @return a connection to the db pool configured in parameter "db-pool" of module 
     *      "com.alkacon.opencms.formgenerator"
     *      
     * @throws SQLException if sth goes wrong 
     */
    public static Connection getConnection() throws SQLException {
//    	try{
//	    	String dbDriver = "net.sourceforge.jtds.jdbc.Driver";	
//	    	String dbUrl = "jdbc:jtds:sqlserver://192.168.7.5:1433/opencms;user=sa;password=thesys";
//	    	Class.forName(dbDriver);
//	    	return DriverManager.getConnection(dbUrl);
//    	}catch(ClassNotFoundException ex){}
//    	return null;
    	return OpenCms.getSqlManager().getConnection("laphone");
    	
    }
    



    
    public static java.sql.Timestamp convert(java.util.Date date){
    	java.sql.Timestamp result = null;
    	if(date!=null){
    		result = new java.sql.Timestamp(date.getTime());    		
    	}
    	return result;
    }
    
    public static  java.util.Date convert(java.sql.Timestamp date){
    	java.util.Date result = null;
    	if(date!=null){
    		result = new java.util.Date(date.getTime());    		
    	}
    	return result;
    }
    /**
     * This method closes the result sets and statement and connections.<p>
     * 
     * @param con The connection.
     * @param statement The statement.
     * @param res The result set.
     */
    public static void closeAll(Connection con, Statement statement, ResultSet res) {

        // result set
        if (res != null) {
            try {
                res.close();
            } catch (Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e.getLocalizedMessage());
                }
            }
        }
        // statement
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e.getLocalizedMessage());
                }
            }
        }
        // connection
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e.getLocalizedMessage());
                }
            }
        }
    }

   
    
    /**
     * for check table is exists
     * @return
     */
    protected abstract String getDBTableName();       
    
    
    
}
