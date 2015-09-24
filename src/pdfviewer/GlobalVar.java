/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfviewer;

/**
 *
 * @author bob
 */
public class GlobalVar {
    public static int VOID_BUTTON_INDEX = 0;    
    public static int SKIP_BUTTON_INDEX = 1;    
    public static int SELECT_BUTTON_INDEX = 2;
    public static int NUM_BUTTON = 3;
    
    public static final int LEN_SEQ_NUM = 4;  // in the batch file
    public static int MAX_NUM_PAGES = 500;  //max number of pages a pdf file can have
    
    public static String globalCounterGenerator(int count) {
       int base = 1;
        for (int i = 0; i < LEN_SEQ_NUM; i++) {
            base *= 10;
        }
        int global = count + base;
        String ans = global + "";  
        return ans.substring(ans.length() - LEN_SEQ_NUM, ans.length());
        
    }
    
    
}
