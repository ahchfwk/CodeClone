package method_analyze;

import java.sql.*;

public class Id2methodBody {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://10.141.221.73:3306/codehub";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";
    private MethodAnalyzer mAnalyzer = null;
    private Connection conn = null;
    private Statement stmt = null;
    public static final String sql = "SELECT c.path, c.classname, m.methodsignature, r.created_at "
    		+ "from java_rl_class as c, java_rl_class_method as m, releases as r "
    		+ "where c.classid = m.classid and c.rlid=r.id and m.methodid =";
    
    public static void main(String[] args) {
//        Id2methodBody id2methodBody = new Id2methodBody();
//        id2methodBody.getMethodById(1);
//        id2methodBody.getMethodById(2);
//        id2methodBody.getMethodById(3);
//        id2methodBody.closeConn();
    }

    public Id2methodBody() {
        try{
        	mAnalyzer = new MethodAnalyzer("", "", "");
        	
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
        
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            // 执行查询
            System.out.println(" 实例化Statement对象...");
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public String getMethodById(int id) {
		String code = "";
        try {
			ResultSet rs = stmt.executeQuery(sql+id);
			if(rs.next()) {
				String path = rs.getString("path");
	            String classname = rs.getString("classname");
	            String methodsignature = rs.getString("methodsignature");
	            rs.close();
	            code = mAnalyzer.start(path, classname, methodsignature).getMethodBody();
	            //mAnalyzer.closeFs();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				// 长时间不用，会自己关闭
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql+id);
				if(rs.next()) {
					String path = rs.getString("path");
		            String classname = rs.getString("classname");
		            String methodsignature = rs.getString("methodsignature");
		            rs.close();
		            code = mAnalyzer.start(path, classname, methodsignature).getMethodBody();
		            //mAnalyzer.closeFs();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
		}catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
            return "exception";
        }
        return code;
	}
    
    public String getBriefinfoById(int id) {
		String brief = "";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql+id);
			if(rs.next()) {
				String[] paths = rs.getString("path").split("/");
	            String classname = rs.getString("classname");
	            String methodsignature = rs.getString("methodsignature");
	            String created = rs.getString("created_at");
	            rs.close();
	            String repo = paths[1];
	            String release = paths[2]+"/"+created;
	            String filename = paths[paths.length-1];
	            brief = repo+"#"+release+"#"+filename+"#"+classname+"#"+methodsignature+"#"+id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return brief;
	}
    
	public void closeConn() {
		try {
			stmt.close();
			conn.close();
			mAnalyzer.closeFs();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Goodbye!");
	}
    
}
