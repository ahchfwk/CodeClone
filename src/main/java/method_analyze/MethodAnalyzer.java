package method_analyze;


import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public class MethodAnalyzer {
	
	private String filePath="";
	private String className="";
	private String signature="";
	private static FileSystem fs;
	private String methodBody = "";
	
	
	public MethodAnalyzer(String filePath, String className, String signature) {
		this.filePath = filePath;
		this.className = className;
		this.signature = signature;
		Configuration conf=new Configuration();
		try {
			fs=FileSystem.get(new URI("hdfs://ns1/"),conf,"hadoop");
		} catch (IOException | InterruptedException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	
	protected void closeFs() {
		try {
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String getMethodBody() {
		return methodBody;
	}
	
	class ClassLevelVisitor extends VoidVisitorAdapter<Void>{
		@Override
		public synchronized void visit(ClassOrInterfaceDeclaration node,Void arg){
			if(node.getNameAsString().equals(className)) {
				for(MethodDeclaration m:node.getMethods()) {
					if(m.getSignature().toString().equals(signature)) {
						try {
							methodBody = m.getBody().get().toString();
							break;
						} catch (Exception e) {
							methodBody = "";
						}
						
					}
				}

			}
			//super.visit(node, arg);
		}
			
	}
	
	
	public synchronized MethodAnalyzer start() {
		String absolutePath=GlobalUtil.hdfsReleasePath+filePath;
		InputStream fileStream=null;
		try {
			
			fileStream=fs.open(new Path(absolutePath));
			CompilationUnit cunit=JavaParser.parse(fileStream);	
		    cunit.accept(new ClassLevelVisitor(),null);
	    }catch(Exception e) {
	    	//e.printStackTrace();
	    	try {
	    		Configuration conf=new Configuration();
				fs=FileSystem.get(new URI("hdfs://ns1/"),conf,"hadoop");
				fileStream=fs.open(new Path(absolutePath));
				CompilationUnit cunit=JavaParser.parse(fileStream);	
			    cunit.accept(new ClassLevelVisitor(),null);
			} catch (IOException | InterruptedException | URISyntaxException e1) {
				e1.printStackTrace();
			}
	    }
		return this;	
	}
	
	
	public synchronized MethodAnalyzer start(String path, String classname, String signature) {
		filePath=path;
		className=classname;
		this.signature=signature;
		return start();
	}
	
	
	public static void main(String [] args) throws IOException {
		String code = new MethodAnalyzer("sourcepit/b2eclipse/master_20180129/b2eclipse-master/core/org.sourcepit.b2eclipse.mvn/src/org/sourcepit/b2eclipse/mvn/Activator.java", 
				"Activator","setMvnPath(String)").start().getMethodBody();
		System.out.println(code);
	}
}
