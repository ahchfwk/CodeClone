package servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Search_result.SearchResult;

/**
 * Servlet implementation class Detail
 */
public class Detail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	static SearchResult searchResult = new SearchResult();
    public Detail() {
        super();
        // TODO Auto-generated constructor stub
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
		String[] codeidlist = request.getParameterValues("codeidlist");
		System.out.println("code id to be search is:"+Arrays.toString(codeidlist));
		String jsonresult = "";
		
		List<Integer> idList = new LinkedList<>();
		for(String id:codeidlist) {
			idList.add(Integer.parseInt(id));
		}
		List<String> result = searchResult.detail(idList);
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();
		jsonresult = gson.toJson(result);
		System.out.println("detail json result is:"+jsonresult);
		response.getWriter().write(jsonresult);
	}
	
	
	public void destroy() {
		searchResult.close();
		System.out.println("detail destroy");
	}

}


