package cloneGroup;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class CloneGroup {
	
	public static void main(String args[]) {
		CloneGroup cGroup = new CloneGroup();
		Map<String, Map<String, Set<Integer>>> result = cGroup.getCloneGroup("ActionBarSherlock");
		System.out.println(result);
		for(String i:result.keySet()) {
			System.out.println(i);
			for(Set<Integer> j:result.get(i).values()) {
				System.out.print(j);
			}
		}
		cGroup.closeConn();
	}
	
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://10.141.221.73:3306/codehub";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";
    private Connection conn = null;
    private Statement stmt = null;
    public static final String sql = "SELECT repository_id FROM codehub.repository_java where local_addr like 'repositories/%/";
    public static final String sql2 = "SELECT id from releases where repository_id=";
    public  static final String sql3 = "SELECT A.block_id,A.group_id, B.name FROM codehub.cc_cloneGroup as A,"
    		+ "releases as B where A.release_id = B.id and B.id=";
    
    public CloneGroup() {
    	try{        	
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
        
            // 打开链接
            System.out.println("clonegroup连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            // 执行查询
            System.out.println("clonegroup实例化Statement对象...");
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    
    
    private List<String> getRelId (String reponame) {
    	List<String> idList = new ArrayList<String>();
    	try {
			ResultSet rs = stmt.executeQuery(sql+reponame+"'");
			if(rs.next()) {
				String repository_id = rs.getString("repository_id");
				rs = stmt.executeQuery(sql2+repository_id);
				while(rs.next()) {
					String id = rs.getString("id");
					idList.add(id);
				}
				return idList;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
    
    
    public Map<String, Map<String, Set<Integer>>> getCloneGroup(String reponame) {
    	Map<String, Map<String, Set<Integer>>> group = new TreeMap<String, Map<String, Set<Integer>>>();
    	List<String> idlist = getRelId(reponame);
    	if(idlist != null) {
    		ResultSet rs = null;
    		for(String id:idlist) {
    			try {
					rs = stmt.executeQuery(sql3+id);
					while (rs.next()) {
						String block_id = rs.getString("block_id");
						String group_id = rs.getString("group_id");
						String relname = rs.getString("name");
						//if there's no relname, set it by id
						if(relname == null) {
							relname = id;
						}
						// if this release has existed
						if(group.containsKey(relname)) {
							// if this clonegroup has existed
							if(group.get(relname).containsKey(group_id)) {
								group.get(relname).get(group_id).add(Integer.parseInt(block_id));
							// if not	
							}else {
								Set<Integer> temset = new HashSet<Integer>();
								temset.add(Integer.parseInt(block_id));
								group.get(relname).put(group_id, temset);
							}
						
						//if not
						}else {
							HashMap<String, Set<Integer>> teMap = new HashMap<>();
							Set<Integer> temset = new HashSet<Integer>();
							temset.add(Integer.parseInt(block_id));
							teMap.put(group_id, temset);
							group.put(relname, teMap);
							
						}
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
		return group;
			
	}
    
    
    public void closeConn() {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("clonegroup Goodbye!");
	}
    
    
}
