package code2token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huage on 2017/3/28.
 */
public class Word {
    public static HashMap<Long, Long> equivHashMap = new HashMap<>();
    public static List<Long> neglectList = new ArrayList<>();
    public static int wordNum;
    public List<Token> lineTokenNumList;
    public Lexer lexer;

    public  List<Token> segment(String inputFileName, String outputFileName) throws IOException {

        segment(inputFileName);
        save(outputFileName);
        return lineTokenNumList;
    }
    
    public Word segment(String code, boolean iscode) throws IOException{
    	int flag =0;  
        lineTokenNumList = new ArrayList<Token>();
        lexer = new Lexer(code, true);
        int lastword = -1;
        Byte word = -1;
        while (lexer.getReaderIsEnd() == false){
          do {
              lastword = word;
              word = lexer.scan();
              if (word>=0) {
                  //lineTokenNumList.add(new Token(word, lexer.line, lexer.wordIndexofChar,lexer.indexOfChar-1));
              	lineTokenNumList.add(new Token(word));
              }
          } while (word == -1&&!lexer.getReaderIsEnd());
          wordNum++;

          switch (flag){

          }
      }
      //System.out.println(lexer.line);
      return this;
    }
    
    public Word segment(String inputFileName) throws IOException {
        int flag =0;  
//        List<List<Line>> totalList = new ArrayList<List<Line>>();
//        List<String> list = new ArrayList<String>();
//        List<Line> tmpList = new ArrayList<Line>();
        lineTokenNumList = new ArrayList<Token>();
//        Stack<Integer> stack = new Stack<Integer>();
//        Stack<Integer> forStack = new Stack<Integer>();
        lexer = new Lexer(inputFileName);
        int lastword = -1;
        Byte word = -1;
//        int storageLine = 0;
//        int preLineNum;
        while (lexer.getReaderIsEnd() == false){

            do {
                lastword = word;
                word = lexer.scan();
                if (word>=0) {
                    //lineTokenNumList.add(new Token(word, lexer.line, lexer.wordIndexofChar,lexer.indexOfChar-1));
                	lineTokenNumList.add(new Token(word));
                }
            } while (word == -1&&!lexer.getReaderIsEnd());
            wordNum++;

            switch (flag){

            }
        }
        System.out.println(lexer.line);
        return this;
    }


    public void save(String outputFileName){

            new Thread(() -> {
                try {
                    lexer.saveToken(lineTokenNumList,outputFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
    }


    public String getTokenStr() {
    	return lineTokenNumList.toString().replace(", ","").replace("[", "").replace("]", "");
    }


    public String getLineTokenStringList(){
        byte[] temp = new byte[lineTokenNumList.size()];
        for (int i=0;i<lineTokenNumList.size();i++){
            temp[i] = lineTokenNumList.get(i).tokenHash;
        }
        return new String(temp);
    }


}
