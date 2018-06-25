package code2token;

/**
 * Created by huage on 2017/5/2.
 */
public class Token {
    public Byte tokenHash;
//    public int line;
//    public int indexOfChar;
//    public int endOfChar;
//
//
//    public Token(Byte tokenHash, int line, int indexOfChar, int endOfChar) {
//        this.tokenHash = tokenHash;
//        this.line = line;
//        this.indexOfChar = indexOfChar;
//        this.endOfChar = endOfChar;
//    }

    public Token(Byte tokenHash) {
    	this.tokenHash = tokenHash;
    }
    
    
    @Override
    public String toString() {
//        return "Token{" +
//                "tokenHash=" + tokenHash +
//                ", line=" + line +
//                ", indexOfChar=" + indexOfChar +
//                ", endOfChar=" + endOfChar +
//                '}';
    	return tokenHash.toString();
    }
}
