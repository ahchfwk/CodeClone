package servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cloneGroup.*;

/**
 * Servlet implementation class CloneGroup
 */
public class MultiClone extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MultiClone() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		response.setContentType("text/html;charset=utf-8");  
		/** 设置响应头允许ajax跨域访问 **/  
        response.setHeader("Access-Control-Allow-Origin", "*");  
        /* 星号表示所有的异域请求都可以接受， */  
        
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		request.setCharacterEncoding("utf-8");
		String reponame = request.getParameter("reponame");
		System.out.println("repo to be search is:"+reponame);
		String jsonresult = "";
		CloneGroup cloneGroup = new CloneGroup();
		Map<String, Map<String, Set<Integer>>> result = cloneGroup.getCloneGroup(reponame);
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();
		jsonresult = gson.toJson(result);
		System.out.println("detail json result is:"+jsonresult);
		response.getWriter().write(jsonresult);
	}

}
