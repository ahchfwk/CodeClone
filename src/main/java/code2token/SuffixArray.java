package code2token;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by huage on 2017/10/29.
 */

public abstract class SuffixArray {

    protected static final int DEFAULT_ALPHABET_SHIFT = 0;
    protected static final int DEFAULT_ALPHABET_SIZE = 256;

    protected static final int MIN_TOKEN_LEN = 50;

    // Length of the suffix array
    public final int N;

    protected int shift = DEFAULT_ALPHABET_SHIFT;

    protected int alphabetSize = DEFAULT_ALPHABET_SIZE;

    // T is the text
    public int[] T;

    // The sorted suffix array values.
    public int[] sa;

    // Longest Common Prefix array
    public int [] lcp;

    //result
    public List<Integer> duplicateResult;

    // Designated constructor
    public SuffixArray(int[] text, int shift, int alphabetSize) {

        if (text == null || alphabetSize <= 0)
            throw new IllegalArgumentException();

        this.T = text;
        this.N = text.length;

        this.shift = shift;
        this.alphabetSize = alphabetSize;

        // Build suffix array
        construct();

        // Build LCP array
        duplicateResult = new ArrayList<>();
        kasai();

    }

    protected static int[] toIntArray(String s) {
        if (s == null) return null;
        int[] text = new int[s.length()];
        for(int i = 0; i < s.length(); i++)
            text[i] = s.charAt(i);
        return text;
    }

    // The suffix array construction algorithm is left undefined
    // as there are multiple ways to do this.
    protected abstract void construct();

    // Use Kasai algorithm to build LCP array
    // http://www.mi.fu-berlin.de/wiki/pub/ABI/RnaSeqP4/suffix-array.pdf
    private void kasai() {
        int[] cache = new int[3];

        lcp = new int[N];
        int [] inv = new int[N];
        for (int i = 0; i < N; i++) inv[sa[i]] = i;
        for (int i = 0, len = 0; i < N; i++) {
            if (inv[i] > 0) {
                int k = sa[inv[i]-1];
                while((i+len < N) && (k+len < N) && T[i+len] == T[k+len]) len++;
                lcp[inv[i]] = len;
                if (len >= MIN_TOKEN_LEN && abs(sa[inv[i]] - sa[inv[i]-1])>=len){

                    if (sa[inv[i]]!=++cache[0] || sa[inv[i]-1]!=++cache[1] || len != --cache[2] ) {
                        duplicateResult.add(inv[i]);
                        cache[0] = sa[inv[i]];
                        cache[1] = sa[inv[i] - 1];
                        cache[2] = len;
                    }


                }
                if (len > 0) len--;
            }
        }
    }

    @Override public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("-----i-----SA-----LCP---Suffix\n");

        for(int i = 0; i < N; i++) {
            int suffixLen = N - sa[i];
            char[] suffixArray = new char[suffixLen];
            for (int j = sa[i], k = 0; j < N; j++, k++)
                suffixArray[k] = (char)(T[j] - shift);
            String suffix = new String(suffixArray);
            String formattedStr = String.format("% 7d % 7d % 7d %s\n", i, sa[i], lcp[i], suffix);
            sb.append(formattedStr);
        }

        return sb.toString();

    }

}