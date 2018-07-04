package nilsimsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import code2token.Word;
import method_analyze.Id2methodBody;

public class Optimize {
	private int segmentLength = 0;
	private List<String> hashlinelist = null;
	private List<Integer> idList = null;
	private List<String> codehashlist = null;
	private List<String> tokenhashlist = null;
	private HashMap<String, Set<String>> codehashDict = null;
	private HashMap<String, Set<String>> tokenhashDict = null;
	private HashMap<String, Set<Integer>> codehash2IdDict = null;
	private HashMap<String, Set<Integer>> tokenhash2IdDict = null;

	
	public static void main(String[] args) {
//		Optimize optimize = new Optimize();
//		optimize.prepare("codehubhash.txt");
//		optimize.searchHash("89300d01d003155a841a1960180bc4643056a4e0e1320068230c542448b26229");
//		Set<Integer> result = optimize.searchTokenHash("01177edee4a58ee17053304994d3d8c939cb848321fffba0582cd3c4f0502bb4");
//		
//		long s = System.nanoTime();
//		for(int i=0;i<100000;i++) {
//			result = optimize.searchTokenHash("01177edee4a58ee17053304994d3d8c939cb848321fffba0582cd3c4f0502bb4");
//		}
//		long e = System.nanoTime();
//		System.out.println(e-s);
		
		Optimize optimize = new Optimize();
		optimize.prepare("hash.txt");
		
		String code = "{\r\n" + 
				"    return ifNameIs(\"ville\").andArgIs(0, \"cede\").itMatches();\r\n" + 
				"}";
		Set<String> briefResult = optimize.findBriefInfo(code, true);
		for(String r:briefResult) {
			System.out.println(r);
		}
		
		Id2methodBody id2methodBody = new Id2methodBody();
		for(String r:briefResult) {
			int id = Integer.parseInt(r.substring(r.lastIndexOf("#")+1));
			System.out.println(id2methodBody.getMethodById(id));
		}
		id2methodBody.closeConn();
	}
	
	
	public void readFileByLine(int bufSize, FileChannel fcin, ByteBuffer rBuffer){
        String enterStr = "\n"; 
        try{ 
	        byte[] bs = new byte[bufSize]; 
	 
	        int size = 0; 
	        StringBuffer strBuf = new StringBuffer(""); 
	        //while((size = fcin.read(buffer)) != -1){ 
	        while(fcin.read(rBuffer) != -1){
				  int rSize = rBuffer.position(); 
				  rBuffer.rewind(); 
				  rBuffer.get(bs); 
				  rBuffer.clear(); 
				  String tempString = new String(bs, 0, rSize); 
				 
	              int fromIndex = 0; 
	              int endIndex = 0; 
	              while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1){ 
		               String line = tempString.substring(fromIndex, endIndex); 
		               line = new String(strBuf.toString() + line); 
		               //System.out.print(line); 
		               hashlinelist.add(line.trim());
					   
					   strBuf.delete(0, strBuf.length()); 
					   fromIndex = endIndex + 1; 
				  } 
				  if(rSize > tempString.length()){ 
				  strBuf.append(tempString.substring(fromIndex, tempString.length())); 
				  }else{ 
				  strBuf.append(tempString.substring(fromIndex, rSize)); 
				  } 
	        }
        } catch (IOException e) { 
        // TODO Auto-generated catch block 
        e.printStackTrace(); 
        } 
    } 
	
	
	public Optimize() {
		hashlinelist = new ArrayList<String>();
		idList = new ArrayList<Integer>();
		codehashlist = new ArrayList<String>();
		tokenhashlist = new ArrayList<String>();
		codehashDict = new HashMap<String, Set<String>>();
		tokenhashDict = new HashMap<String, Set<String>>();
		codehash2IdDict = new HashMap<String,Set<Integer>>();
		tokenhash2IdDict = new HashMap<String,Set<Integer>>();

	}
	
	
	
	public void dynamicLoad(String hash) {
		hashlinelist.clear();
		idList.clear();
		codehashlist.clear();
		tokenhashlist.clear();
		codehashDict.clear();
		tokenhashDict.clear();
		codehash2IdDict.clear();
		tokenhash2IdDict.clear();

		int countFor1 = 0;
		for(int i=0;i<hash.length();i++){
			if (hash.charAt(i)=='1' || hash.charAt(i)=='2' || hash.charAt(i)=='4' || hash.charAt(i)=='8') {
				countFor1 += 1;
			}
			else if (hash.charAt(i)=='3' || hash.charAt(i)=='5' || hash.charAt(i)=='6' || hash.charAt(i)=='9' || hash.charAt(i)=='a' || hash.charAt(i)=='c'){
				countFor1 += 2;
			}
			else if (hash.charAt(i)=='7' || hash.charAt(i)=='b' || hash.charAt(i)=='d' || hash.charAt(i)=='e'){
				countFor1 += 3;
			}
			else if (hash.charAt(i)=='f') {
				countFor1 += 4;
			}
		}
		
		System.out.println("hash is:"+hash+"////"+"countFor1 is:"+countFor1);
		String hashfile = "/home/fdse/esdata/fwk/BigCode/hash_separate_by_1_redundance/hash"+(countFor1-3)+"-"+(countFor1+3)+".txt";
		//String hashfile = "C:\\Users\\fwk\\Desktop\\java\\hash\\hash_separate_by_1_redundance\\hash"+(countFor1-3)+"-"+(countFor1+3)+".txt";
		prepare(hashfile);
	}
	
	
	public void prepare(String hashfile) {
		System.out.println("loading data..."+hashfile);
		try {
			// read hash file
//			FileReader fReader = new FileReader(hashfile);
//			BufferedReader bReader = new BufferedReader(fReader);
//			String line = "";
//			while((line = bReader.readLine()) != null) {
//				hashlinelist.add(line); 
//			}
//			bReader.close();
			
			//read hash file by nio
			//System.out.println("start");
			int bufSize = 500;
			File fin = new File(hashfile);
			FileChannel fcin = new RandomAccessFile(fin, "r").getChannel();
			ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);
			readFileByLine(bufSize, fcin, rBuffer);
			fcin.close();
			//System.out.println(hashlinelist.subList(0, 10));
			//System.out.println("end");
			
			//从每行提取id和codehash和tokenhash
			for(String s:hashlinelist) {
				int firstCommaIdx = s.indexOf(",");
				idList.add(Integer.parseInt(s.substring(0, firstCommaIdx)));
//				codehashlist.add(s.substring(firstCommaIdx+42, firstCommaIdx+106));
				tokenhashlist.add(s.substring(firstCommaIdx+148));
			}
			
			//计数变量
			int count = 0;

			//将codehash和对应的id存入hashmap
//			for(String codehash:codehashlist) {
//				if(codehash2IdDict.containsKey(codehash)) {
//					codehash2IdDict.get(codehash).add(idList.get(count));
//				}
//				else {
//					Set<Integer> id = new HashSet<Integer>();
//					id.add(idList.get(count));
//					codehash2IdDict.put(codehash, id);
//				}
//				count++;
//			}
			
			count = 0;
			//将tokenhash和对应的id存入hashmap
			for(String tokenhash:tokenhashlist) {
				if(tokenhash2IdDict.containsKey(tokenhash)) {
					tokenhash2IdDict.get(tokenhash).add(idList.get(count));
				}
				else {
					Set<Integer> id = new HashSet<Integer>();
					id.add(idList.get(count));
					tokenhash2IdDict.put(tokenhash, id);
				}
				count++;
			}
			store(8);
			System.out.println("loading completed");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 可以自定义切分的粒度，但是切分是2的幂
	private void store(int segmentCount) {
		segmentLength = 64/segmentCount;
		for(int i = 0;i<segmentCount;i++) {
			// 将simhash切分并存储于hashmap
//			for(String codehash:codehashlist) {
//				String key = codehash.substring(i*segmentLength,(i+1)*segmentLength);
//				if(codehashDict.containsKey(key)){
//					codehashDict.get(key).add(codehash);
//				}
//				else {
//					Set<String> value = new HashSet<String>();
//					value.add(codehash);
//					codehashDict.put(key, value);
//				}
//			}
			for(String tokenhash:tokenhashlist) {
				String key = tokenhash.substring(i*segmentLength,(i+1)*segmentLength);
				if(tokenhashDict.containsKey(key)){
					tokenhashDict.get(key).add(tokenhash);
				}
				else {
					Set<String> value = new HashSet<String>();
					value.add(tokenhash);
					tokenhashDict.put(key, value);
				}
			}
			
		}
	}
	
	
	public Set<Integer> searchHash(String hash){
		Set<Integer> idSet = new HashSet<Integer>();
		for(int i = 0;i<64/segmentLength;i++) {
			if(codehashDict.containsKey(hash.substring(i*segmentLength,(i+1)*segmentLength))) {
				Iterator<String> iterator = codehashDict.get(hash.substring(i*segmentLength,(i+1)*segmentLength)).iterator();
				while(iterator.hasNext()) {
					String candidateHash = iterator.next();
					if(Nilsimsa.compare(hash, candidateHash)<8) {			
						idSet.addAll(codehash2IdDict.get(candidateHash));
					}
				}
			}
		}
		//System.out.println(idSet.toString());
		return idSet;
	}
	
	public Set<Integer> searchTokenHash(String hash){
		Set<Integer> idSet = new HashSet<Integer>();
		for(int i = 0;i<64/segmentLength;i++) {
			if(tokenhashDict.containsKey(hash.substring(i*segmentLength,(i+1)*segmentLength))) {
				Iterator<String> iterator = tokenhashDict.get(hash.substring(i*segmentLength,(i+1)*segmentLength)).iterator();
				while(iterator.hasNext()) {
					String candidateHash = iterator.next();
					
					if(Nilsimsa.compare(hash, candidateHash)<8) {			
						idSet.addAll(tokenhash2IdDict.get(candidateHash));
					}
				}
			}
		}
		//System.out.println(idSet.toString());
		return idSet;
	}
	
	public Set<String> findCode(String code, boolean useToken){
		Set<String> findResult = new HashSet<String>();
		Id2methodBody id2methodBody = new Id2methodBody();
		if(useToken) {
			try {
				String token = new Word().segment(code,true).getTokenStr();
				String hash = new Nilsimsa(token).toHexDigest();
				Set<Integer> result = searchTokenHash(hash);
				for(int i:result) {
					findResult.add(id2methodBody.getMethodById(i));
				}
				id2methodBody.closeConn();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			String hash = new Nilsimsa(code).toHexDigest();
			Set<Integer> result = searchHash(hash);
			for(int i:result) {
				findResult.add(id2methodBody.getMethodById(i));
			}
			id2methodBody.closeConn();
		}
		return findResult;
	}
	
	public Set<String> findBriefInfo (String code, boolean useToken) {
		Set<String> findResult = new HashSet<String>();
		Id2methodBody id2methodBody = new Id2methodBody();
		if(useToken) {
			try {
				
				String token = new Word().segment(code,true).getTokenStr();
				//System.out.println("token is:"+token);
				String hash = new Nilsimsa(token).toHexDigest();
				//System.out.println("hash is:"+hash);
				//动态加载来了
				dynamicLoad(hash);
				if (Nilsimsa.compare("0000000000000000000000000000000000000000000000000000000000000000", hash)<3 || 
						Nilsimsa.compare("0000080200048010080008011493020110400000100100000000000020002038", hash)<3){
					id2methodBody.closeConn();
					return findResult;
				}
				Set<Integer> result = searchTokenHash(hash);
				for(int i:result) {
					findResult.add(id2methodBody.getBriefinfoById(i));
				}
				id2methodBody.closeConn();
			} catch (Exception e) {
				e.printStackTrace();
				id2methodBody.closeConn();
			}
		}else {
			String hash = new Nilsimsa(code).toHexDigest();
			Set<Integer> result = searchHash(hash);
			for(int i:result) {
				findResult.add(id2methodBody.getBriefinfoById(i));
			}
			id2methodBody.closeConn();
		}
		return findResult;
	}
	
}
