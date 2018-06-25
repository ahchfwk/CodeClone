package nilsimsa;

import method_analyze.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import code2token.*;

public class HashGenerator {
	
	public static void main(String[] args) {
		int count = Integer.parseInt(args[0]);
		while(count < Integer.parseInt(args[1])) {
			new HashGenerator().generateBat(count, count+1000);
			count+=1000;
		}
	}
	
	
	public String code2simhash(String code) {
		Nilsimsa nilsimsa = new Nilsimsa(code);
		return nilsimsa.toHexDigest();
	}
	
	
	public String token2simhash(String code) {
		Word word = new Word();
		try {
			String token = word.segment(code, true).getTokenStr();
			Nilsimsa nilsimsa = new Nilsimsa(token);
			return nilsimsa.toHexDigest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public String code2sha1(String code) {
		return DigestUtils.shaHex(code);
	}
	
	
	public String token2sha1(String code) {
		Word word = new Word();
		try {
			String token = word.segment(code, true).getTokenStr();
			return DigestUtils.shaHex(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private Id2methodBody id2methodBody = null;
	
	public HashGenerator() {
		id2methodBody = new Id2methodBody();
	}
	
	
	public String Generated(int id) {
        String code = id2methodBody.getMethodById(id);
        System.out.println(code);
        System.out.println(id);
        String line = id+","+code2sha1(code)+","+code2simhash(code)+","+token2sha1(code)+","+token2simhash(code)+"\n";
        return line;
	}
	
	public void close() {
		id2methodBody.closeConn();
	}
	
	public void generateBat(int start, int end) {
		try {
			File file = new File("hash.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fWriter = new FileWriter(file,true);
			BufferedWriter bufferedWriter = new BufferedWriter(fWriter);
			for(int i = start; i<end; i++) {
				String line = Generated(i);
				bufferedWriter.write(line);
			}
			bufferedWriter.flush();
			bufferedWriter.close();
			fWriter.close();
			close();
		} catch (Exception e) {
			System.out.print(e.getStackTrace());
		}
	}
	
}
