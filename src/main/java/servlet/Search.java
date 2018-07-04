package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Search_result.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class Search
 */
//@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	static SearchResult searchResult = new SearchResult();
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public void init () throws ServletException{
    	searchResult.prepareOptandId2m();
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
		
		String string = request.getParameter("code");
		System.out.println("the raw code is:"+string);
		String jsonresult = "";
		
		
		// 前台textarea传过来的文本，后台接受就无法保存换行符，string输出仍然有换行，但不是\n或\r\n
		HashMap<String,Map<String, Set<String[]>>> result = searchResult.search(string.replace(";", ";\n").replace("{", "{\n"));
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();
		jsonresult = gson.toJson(SearchResult.sort(result));
		//System.out.println("the search json result is:"+jsonresult);
		response.getWriter().write(jsonresult);
	}
	
	public void destroy() {
		//System.out.println("search destroy");
		searchResult.close();
	}
	

}
