package code2token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huage on 2017/3/28.
 */
public class KeyWordAndType {

    // 共计36�?
    
        private KeyWordAndType(){

        }
        /** 运算�?*/
        public static final byte PLUS = 3;//�?
        public static final byte MIN = 3;//�?
        public static final byte MUL = 5;//�?
        public static final byte DIV = 6;//�?
        public static final byte PERCENT = 4;
        public static final byte AND = 7;//�?
        public static final byte OR = 8;//�?
        public static final byte NOT = 9;//�?
        public static final byte EQ = 11;//等于
        public static final byte UEQ  = 14; //不等�?

        public static final byte LT = 12;//小于
        public static final byte LTE = 12;//小等�?
        public static final byte GT = 13;//大于
        public static final byte GTE = 13;//大于
    // 左括号， 右括号， . , [ ]
        public static final byte LEFTSMALLBRACKET = 1;
        public static final byte RIGHTSMALLBRACKET = 14;
        public static final byte DOT =     10;
        public static final byte LEFTMIDBRACKET = 2;
        public static final byte RIGHTMIDBRACKET = 15;

        public static final byte ASSIGNMENT = 30;

        public static final byte COMMA = 34;
        public static final byte COLON = 35;

        public static final byte LEFTLARGEBRACKET = 32;
        public static final byte RIGHTLARGEBRACKET = 33;

       /** 界符{ ',', ';', '{', '}', '(', ')', '[', ']', '_', ':', '.', '"' };*/

       //复用
       public static final byte SecondDIGIT = 16;
      public static final byte ThirdDIGIT = 21;


        /**标识�?*/
        public static final byte ID = 0;
        /** 常数*/
        public static final byte DIGIT = 0;



        public static final byte BREAK = 16;
        public static final byte BYTE = 17;
        public static final byte CASE = 18;
        //这个可以去掉,先这么写�? try 和catch 应该去掉。�??   stack大于1的时�? { } 应该录进�?
        public static final byte CATCH = 19;
        public static final byte CHAR = 20;
        public static final byte STRING = 20;
        public static final byte CONTINUE = 21;
        public static final byte ELSE = 22;
        public static final byte FLOAT = 23;
        public static final byte DOUBLE = 23;
        public static final byte FOR = 24;
        public static final byte Byte = 25;
        public static final byte LONG = 25;
        public static final byte SHORT = 25;
        public static final byte NEW = 26;
        public static final byte IF = 27;
        public static final byte RETURN = 28;
        public static final byte SWITCH = 29;
        public static final byte TRY = 30;
        public static final byte WHILE = 31;
        public static final byte THIS = 18;

        public static final Map<String,Byte> map = new HashMap<String, Byte>(){{
            put("break", BREAK);
            put("byte", BYTE);
            put("case", CASE);
            put("catch", CATCH);
            put("char",CHAR);
            put("String",STRING);
            put("continue",CONTINUE);
            put("else",ELSE);
            put("float",FLOAT);
            put("double",DOUBLE);
            put("for",FOR);
            put("byte",BYTE);
            put("long",LONG);
            put("short",SHORT);
            put("new",NEW);
            put("if",IF);
            put("return", RETURN);
            put("switch", SWITCH);
            put("try", TRY);
            put("while", WHILE);

            //方便鉴别
            put("System", SYSTEM);
            put("out", OUT);
            put("this", THIS);
        }};



    public static final String[] neglectfulKeyword = {
    "synchronized", "transient",  "volatile", "private", "protected",
    "public", "static", "final", "throws","interface", "assert","void"};

    public static final String exceptionKeyword = "Exception";


    //不常�?
//    public static final byte SYNCHRONIZED ;
//    public static final byte TRANSIENT ;
//    public static final byte VOLATILE ;

    // �?要忽略的
    //    public static final byte PRIVATE = 29;
    //    public static final byte PROTECTED = 30;
    //    public static final byte PUBLIC = 31;
    //   public static final byte STATIC = 34;
    //     public static final byte FINAL = 15;
        // SYNCHRONIZED
    // VOLATILE



    //   THROWS
    // EXCEPTION

    //  ASSERT
    //
    //
    //
    // public static final byte FINALLY = 16;
    //         public static final byte VOID = 43;
  //     public static final byte INSTANCEOF = 22;
    //  public static final byte ENUM = 48;
    //      public static final byte THROW = 39;
    //     public static final byte INTERFACE = 24;

    //       public static final byte DEFAULT = 10;
   // public static final byte DO = 11;
    //  public static final byte BOOLEAN = 2;

//      public static final byte SUPER = 35;
    //       public static final byte CLASS = 8;


    //System  out
    public static final byte SYSTEM = -4;
    public static final byte OUT = -5;
    public static final byte SEMICOLON = -6;
}
