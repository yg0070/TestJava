package minaExamle.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class DBHelper {
	public DBHelper(){}
	private static final Log log = LogFactory.getLog(DBHelper.class);
    private static final String configFile = "dbcp.properties";
 
    private static DataSource dataSource;
 
    static {
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(DBHelper.class.getClassLoader()
                    .getResourceAsStream(configFile));
            dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
 
            Connection conn = getConn();
            DatabaseMetaData mdm = conn.getMetaData();
            log.info("Connected to " + mdm.getDatabaseProductName() + " "
                    + mdm.getDatabaseProductVersion());
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            log.error("初始化连接池失败：" + e);
        }
    }
 
  
 
    /**
     * 获取链接，用完后记得关闭
     * 
     * @see {@link DBManager#closeConn(Connection)}
     * @return
     */
    public static final Connection getConn() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("获取数据库连接失败：" + e);
        }
        return conn;
    }
 
    /**
     * 关闭连接
     * 
     * @param conn
     *            需要关闭的连接
     */
    public static void closeConn(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            log.error("关闭数据库连接失败：" + e);
        }
    }
	 
    
    
    public int save(String sql,String [] can) {
		long begin = System.currentTimeMillis();
		Connection conn = DBHelper.getConn();
		int num=0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=0;i<can.length;i++){
				ps.setString(i+1, can[i]);
			}
			num=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBHelper.closeConn(conn);
		return num;
	}
    public int select(String sql,String[]can){
    	long begin = System.currentTimeMillis();
		Connection conn = DBHelper.getConn();
		ResultSet rs=null;
		int num=0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=0;i<can.length;i++){
				ps.setString(i+1, can[i]);
			}
			rs=ps.executeQuery();
			if(rs.next()){
				num=rs.getInt(1);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBHelper.closeConn(conn);
		return num;
    }
}   