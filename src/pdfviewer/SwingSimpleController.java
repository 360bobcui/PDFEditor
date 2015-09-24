/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfviewer;

import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import org.icepdf.ri.common.SwingController;

/**
 * @author bob
 */
public class SwingSimpleController extends SwingController{
    
    private JRadioButton voidButton;
    private JRadioButton selectButton;
    private JRadioButton skipButton;
    private ButtonGroup statusButtonGroup;
    private Boolean[][] statusArray;
   // private int pageNum;    
    private JLabel seqText;
    
    public SwingSimpleController(ButtonGroup statusButtonGroup, JLabel seqText,
            Boolean[][] statusArray) {
        super();
        this.statusArray = statusArray;
        this.statusButtonGroup = statusButtonGroup;
        
        Enumeration buttons = statusButtonGroup.getElements(); 
        this.voidButton = (JRadioButton) buttons.nextElement();        
        this.skipButton = (JRadioButton) buttons.nextElement();
        this.selectButton = (JRadioButton) buttons.nextElement();
        this.seqText = seqText;
        
    }
    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int pageNum = this.getCurrentPageNumber();
        if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_PAGE_DOWN) {
            this.goToDeltaPage(1);          
        } else if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_PAGE_UP) {
            this.goToDeltaPage(-1);          
        } else if (keyCode == KeyEvent.VK_HOME) {
            this.goToDeltaPage(-pageNum);
        }
    }
    
    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {

//        System.out.println("Void button status " + statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum]);
//        System.out.println("Select button status " + statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum]);
//        System.out.println("Skip button status " + statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum]);
        int pageNum = this.getCurrentPageNumber();
        statusButtonGroup.clearSelection();
        voidButton.setSelected(statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum]);        
        selectButton.setSelected(statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum]); 
        skipButton.setSelected(statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum]);
        setUpSeq();
    }
    
    public void setUpSeq() {
        int pageNum = this.getCurrentPageNumber();
        int selectCount = 0;
        for (int i = 0; i <= pageNum; i++) {
            if(statusArray[GlobalVar.SELECT_BUTTON_INDEX][i]) {
                selectCount++; 
            }
        }
        seqText.setText(GlobalVar.globalCounterGenerator(selectCount));  
    } 
    
}
