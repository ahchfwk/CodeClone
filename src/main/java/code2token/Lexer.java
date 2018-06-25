package code2token;


import java.io.*;
import java.util.List;


/**
 * Created by huage on 2017/3/28.
 */
public class Lexer {

    public int test = 0;
    public int line = 1;
    public int indexOfChar = 0;
    public int wordIndexofChar = 1;

    private char peek = ' ';
    private char lastpeek = ' ';
    private  BufferedReader reader = null;
    private Boolean isEnd = false;

    private Boolean varibleOneOrSecond =true;
    private String lastVariable ="";
    private int lastline = 0;


    public Boolean getReaderIsEnd(){
        return this.isEnd;
    }

    public void saveToken(List<Token> list, String outputFileName) throws IOException{

        FileWriter writer = new FileWriter(outputFileName);
        writer.write("token info");

        writer.write("\n");

        if (list.size()>0)
        {
            for (Token x :list) {
                writer.write(x.toString()+"\t\t" +"\n");
            }

        }
        writer.flush();
        writer.close();
    }

    public Lexer(String inputFileName) throws IOException{
        reader = new BufferedReader(new FileReader(inputFileName));
    }
    public Lexer(String input, String xml)throws IOException{
        InputStream myIn=new ByteArrayInputStream(input.getBytes());
        reader = new BufferedReader(new InputStreamReader(myIn)    );
    }

    public Lexer(String code, boolean iscode) throws IOException{
    	InputStream myInputStream = new ByteArrayInputStream(code.getBytes());
    	reader = new BufferedReader(new InputStreamReader(myInputStream));
    }

    public void getChar() throws IOException{
        lastpeek = peek;
        peek = (char)reader.read();
        if((int)peek == 0xffff){

            this.isEnd = true;
        }
        indexOfChar++;
    }

    public Boolean getChar(char x) throws IOException {
        lastpeek = peek;
        getChar();
        if (peek!=x){
            return false;
        }
        peek = ' ';
        indexOfChar++;
        return true;
    }

    public byte scan() throws IOException {
        for (; ; getChar()) {
            if (getReaderIsEnd())
            {
                break;
            }
            else if (peek == ' ' || peek == '\t' || peek == '\r')   //||peek == '\r'
                continue;
            else if (peek == '\n') {
                line = line + 1;
                indexOfChar = 0;
                continue;
            }
            else if(peek == '@'){
                reader.readLine();
                line = line + 1;
                indexOfChar = 0;
                continue;
            }
            else if (peek == '/') {
                getChar();
                if (peek=='*') {
                    for (; ; getChar()) {
                        if (peek == '\n')
                            line = line + 1;
                        if (lastpeek=='*'&& peek == '/' ) {
                            //     getChar();
                            break;
                        }
                        if (getReaderIsEnd()){
                            break;
                        }
                        else
                            continue;
                    }

                }
                else if (peek=='/'){
                    reader.readLine();
                    indexOfChar = 0;
                    line = line + 1;
                }
                else if (peek=='\n'){
                    line = line +1;                //TODO important
                }
            } else
                break;
        }

        wordIndexofChar = indexOfChar;

        switch (peek) {
            case  '\\':
                getChar();
                getChar();
                return -1;
            case '[':
                getChar();
                return KeyWordAndType.LEFTMIDBRACKET;
            case ']':
                getChar();
                return KeyWordAndType.RIGHTMIDBRACKET;
            case '(':
                getChar();
                return  KeyWordAndType.LEFTSMALLBRACKET;
            case ')':
                getChar();
                return  KeyWordAndType.RIGHTSMALLBRACKET;
            case '.':
                getChar();
                return  KeyWordAndType.DOT;

            case ',':
                getChar();
                return  KeyWordAndType.COMMA;
            case ':':
                getChar();
                return  KeyWordAndType.COLON;
            case ';':
                getChar();                    //鏄笉鏄渶瑕佽繑鍥炲垎鍙疯繕鏈夊緟鑰冭瘉
                return KeyWordAndType.SEMICOLON;
            //
            case '{':
                getChar();
                return KeyWordAndType.LEFTLARGEBRACKET;
            case '}':
                getChar();
                return KeyWordAndType.RIGHTLARGEBRACKET;
            case '+':
                getChar();
                return  KeyWordAndType.PLUS;
            case '-':
                getChar();
                return KeyWordAndType.MIN;
            case '*':
                getChar();
                return KeyWordAndType.MUL;
            case '/':
                getChar();
                return  KeyWordAndType.DIV;
            case '%':
                getChar();
                return KeyWordAndType.PERCENT;

            case '=':
                if (getChar('=')) {

                    return KeyWordAndType.EQ;
                } else {
                    return KeyWordAndType.ASSIGNMENT;
                }
            case '>':
                if (getChar('=')) {

                    return KeyWordAndType.GTE;
                } else {
                    return KeyWordAndType.GT;
                }
            case '<':
                if (getChar('=')) {
                    ;
                    return KeyWordAndType.LT;
                } else {

                    return KeyWordAndType.LTE;
                }
            case '!':
                if (getChar('=')) {

                    return KeyWordAndType.UEQ;
                } else {

                    return KeyWordAndType.NOT;
                }
            case '&':
                if (getChar('&')){
                    return KeyWordAndType.AND;
                }
                else {
                    return KeyWordAndType.AND;
                }
            case '|':
                if (getChar('|')){
                    return KeyWordAndType.OR;
                }
                else {
                    return KeyWordAndType.OR;
                }


        }

        if (peek == '\"') {
            StringBuffer sb = new StringBuffer();

            sb.append(peek);
            do {

                if (peek=='\\')
                {
                    getChar();
                    getChar();
                }
                else {
                    getChar();
                }
                sb.append(peek);
            }while (peek!='\"'&&!getReaderIsEnd());
            getChar();
            //鍒ゆ柇涓�琛岄噷闈㈢殑澶氫釜鍙橀噺銆�
            return KeyWordAndType.DIGIT;

        }

        if (peek == '\'') {
            StringBuffer sb = new StringBuffer();
            sb.append(peek);
            do {
                if (peek=='\\')
                {
                    getChar();
                    getChar();
                }
                else {
                    getChar();
                }
                sb.append(peek);
            }while (peek!='\''&&!getReaderIsEnd());
            getChar();
            return KeyWordAndType.DIGIT;
        }

        if (Character.isDigit(peek)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(peek, 10);
                getChar();
            } while (Character.isDigit(peek));


            return KeyWordAndType.DIGIT;          //鍊� 浣嶇Щ锛�
        }


        if (Character.isLetter(peek)) {
            StringBuffer sb = new StringBuffer();

            /* 棣栧厛寰楀埌鏁翠釜鐨勪竴涓垎鍓� */
            do {
                sb.append(peek);
                getChar();
            } while (Character.isLetterOrDigit(peek) || peek == '_' || peek == '-');
            String s = sb.toString();
            //鐩存帴璺宠 鍔犲揩閫熷害銆�
            if (s.equals("import")||s.equals("package")){
                reader.readLine();
                indexOfChar = 0;
                line = line + 1;
                return -1;
            }

            else {
                if (s.endsWith(KeyWordAndType.exceptionKeyword)) {
                    return -1;
                }
                for(int i =0;i<KeyWordAndType.neglectfulKeyword.length;i++){
                    if(s.equals(KeyWordAndType.neglectfulKeyword[i]))
                    {
                        return -1;
                    }
                }
                Byte isKeyWord = KeyWordAndType.map.get(s);
                if(isKeyWord!=null){
                    return isKeyWord;
                }

                if (lastline!=line) {
                    lastVariable = sb.toString();
                    lastline = line;
                    return KeyWordAndType.DIGIT;   //鏈夋椂闂村姞鍔犲ソ鍒ゆ柇
                } else if (lastVariable.equals(sb.toString())) {

                    return KeyWordAndType.DIGIT;
                } else {
                    lastVariable = sb.toString();
                    return KeyWordAndType.SecondDIGIT;
                }

            }


        }

        if ((int) peek != 0xffff) {
            char x = peek;
            getChar();
            //TODO   % 绛夊瓧绗�

//            System.out.println(x);
            return -1;   // 绌烘牸鍒ゆ柇

        }

        return -2; //            null   鏀规垚-2

    }

}
