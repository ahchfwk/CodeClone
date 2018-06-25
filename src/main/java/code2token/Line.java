package code2token;

/**
 * Created by huage on 2017/4/16.
 */
public class Line {
    public Long lineHash;
    public int lineNum;
    public int preLineNum;

    public Line(Long lineHash, int lineNum) {
        this.lineHash = lineHash;
        this.lineNum = lineNum;
    }

    public Line(Long lineHash, int lineNum, int preLineNum) {
        this.lineHash = lineHash;
        this.lineNum = lineNum;
        this.preLineNum = preLineNum;
    }

    @Override
    public String toString() {
        return "Line{" +
                "lineHash=" + lineHash +
                ", lineNum=" + lineNum +
                ", preLineNum=" + preLineNum +
                '}';
    }
}
