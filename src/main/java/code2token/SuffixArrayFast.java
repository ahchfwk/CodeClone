package code2token;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huage on 2017/10/29.
 */
public class SuffixArrayFast extends SuffixArray {

    public static

    int[] sa2, rank, tmp, c;

    public SuffixArrayFast(String text) {
        super(toIntArray(text), DEFAULT_ALPHABET_SHIFT, DEFAULT_ALPHABET_SIZE);
    }

    public SuffixArrayFast(int[] text) {
        super(text, DEFAULT_ALPHABET_SHIFT, DEFAULT_ALPHABET_SIZE);
    }

    // TODO(williamfiset): Get rid of these constructors in favor of
    // automatically detecting the alphabet size shift required
    public SuffixArrayFast(String text, int shift) {
        super(toIntArray(text), shift, DEFAULT_ALPHABET_SHIFT);
    }
    public SuffixArrayFast(int[] text, int shift) {
        super(text, shift, DEFAULT_ALPHABET_SIZE);
    }

    // Designated constructor
    public SuffixArrayFast(int[] text, int shift, int alphabetSize) {
        super(text, shift, alphabetSize);
    }

    @Override protected void construct() {

        sa = new int[N];
        sa2 = new int[N];
        rank = new int[N];
        c = new int[Math.max(alphabetSize, N)];

        int i, p, r;
        for (i=0; i<N; ++i) c[rank[i] = T[i]]++;
        for (i=1; i<alphabetSize; ++i) c[i] += c[i-1];
        for (i=N-1; i>=0; --i) sa[--c[T[i]]] = i;
        for (p=1; p<N; p <<= 1) {
            for (r=0, i=N-p; i<N; ++i) sa2[r++] = i;
            for (i=0; i<N; ++i) if (sa[i] >= p) sa2[r++] = sa[i] - p;
            java.util.Arrays.fill(c, 0, alphabetSize, 0);
            for (i=0; i<N; ++i) c[rank[i]]++;
            for (i=1; i<alphabetSize; ++i) c[i] += c[i-1];
            for (i=N-1; i>=0; --i) sa[--c[rank[sa2[i]]]] = sa2[i];
            for (sa2[sa[0]] = r = 0, i=1; i<N; ++i) {
                if (!(rank[sa[i-1]] == rank[sa[i]] &&
                        sa[i-1]+p < N && sa[i]+p < N &&
                        rank[sa[i-1]+p] == rank[sa[i]+p])) r++;
                sa2[sa[i]] = r;
            } tmp = rank; rank = sa2; sa2 = tmp;
            if (r == N-1) break; alphabetSize = r + 1;
        }
    }

    /////////////////print for visualization

    public static void printArray(int[] array){
        StringBuilder sb = new StringBuilder();
        for (int i =0;i<array.length;i++){
            sb.append(array[i]+" ");
        }
        System.out.println(sb);
    }



    public  void printDuplicateSuffix(){
        List<Integer> array = duplicateResult;
        StringBuilder sb = new StringBuilder();
        for (int i =0;i<array.size();i++){
            int suffixLen = lcp[array.get(i)];
            char[] suffixCharArray = new char[suffixLen];

            for(int j=0,k=sa[array.get(i)];j<suffixLen;j++,k++){
                suffixCharArray[j] = (char) T[k];
            }
            String suffix = new String(suffixCharArray);


            sb.append(sa[array.get(i)]+" ,"+sa[array.get(i)-1]+" ,    "+suffix +"\n");
        }

        System.out.println(sb);
    }

    public  List<Integer> getDuplicateSuffix(){
        List<Integer> array = duplicateResult;
        List<Integer> result = new ArrayList<>();
        for (int i =0;i<array.size();i++){
            int suffixLen = lcp[array.get(i)];
            char[] suffixCharArray = new char[suffixLen];


            result.add(sa[array.get(i)]);
            result.add(sa[array.get(i) - 1] );
            result.add(lcp[array.get(i)]);
        }

        return result;
    }


    public static void main(String[] args) throws IOException {
//          SuffixArrayFast sa = new SuffixArrayFast("klhgaldhglahsdlhfdafas 8745238975  <>: dsfklhgaldhglahs   dlhfdafasdghklhgaldhglahsdlhfdafasd klhgaldhglahsdlhfdafas");
//        SuffixArrayFast sa = new SuffixArrayFast("aabaaaab");
//        SuffixArrayFast sa = new SuffixArrayFast("klhgaldhglahsdlhfdafasklhgaldhglahsdlhfdafklhgaldhglahsdlhfdaf");

        //get all

//        Filetraves filetraves =new Filetraves("C:\\Users\\hauge\\Desktop\\wingsoft");
//        List<File> lists = filetraves.directoryAllList();
//
//        Date time1 = new Date();
//        int LEN = lists.size();
//        String[] strs = new String[LEN];
//        for(int i =0;i<LEN;i++){
//            strs[i] = readToBuffer(lists.get(i).getAbsolutePath());
//        }
//
//        String finalString = String.join("", strs);
//        PrintWriter printWriter = new PrintWriter("allcode.txt");
//        printWriter.print(finalString);

        Date time1 = new Date();
        String finalString =  readToBuffer("C:\\Users\\hauge\\Desktop\\wingsoft\\core\\action\\CommonQueryAction.java");

        Date time2 = new Date();
        SuffixArrayFast sa = new SuffixArrayFast(finalString);
        Date time3 = new Date();
        System.out.println("pre-process time1: " + (time2.getTime() - time1.getTime()));
        System.out.println("suffix time2: " + (time3.getTime() - time2.getTime()));
        System.out.println("total time3: " + (time3.getTime() - time1.getTime()));
        System.out.println("size: "+finalString.length());
        System.out.println("---------------------");

//        printArray(sa.rank);
//        printArray(sa.lcp);
        // MyDduplicateRsult
        sa.printDuplicateSuffix();

    }




    public static String readToBuffer(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line=reader.readLine())!=null){
            line = line.trim();
            line = line.replaceAll("\\s*","");
            line = line.replaceAll("[^\\x00-\\x7F]", "");
            sb.append(line);
        }
        return sb.toString();
    }



}

