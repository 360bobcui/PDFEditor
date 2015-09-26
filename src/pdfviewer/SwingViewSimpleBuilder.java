package pdfviewer;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.icepdf.ri.common.MouseWheelListenerPageChanger;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.AbstractDocumentView;
import org.icepdf.ri.common.views.DocumentViewController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bob
 */
public class SwingViewSimpleBuilder extends SwingViewBuilder{

    SwingSimpleController swingControl;

    boolean isVoid;
    boolean isSelected;
    ButtonGroup statusButtonGroup;
    private JRadioButton voidButton;
    private JRadioButton selectButton;
    private JRadioButton skipButton;
    private JLabel seqText;  
    private Boolean[][] statusArray;
    private JButton submitButton;
    
    // Boolean[] voidList contain pages need to be voided.   Boolean[] selectList contain pages need to be selected for sequence purpose
    public SwingViewSimpleBuilder(SwingSimpleController c, ButtonGroup statusButtonGroup, 
            JLabel seqText, JButton submitButton, Boolean[][] statusArray) {
        super(c);
        swingControl = c;
        this.statusButtonGroup = statusButtonGroup;
        this.statusArray = statusArray;
        this.submitButton = submitButton;
        
        Enumeration buttons = statusButtonGroup.getElements(); 
        this.voidButton = (JRadioButton) buttons.nextElement();        
        this.skipButton = (JRadioButton) buttons.nextElement();
        this.selectButton = (JRadioButton) buttons.nextElement();
        this.seqText = seqText;     
        Icon ic = new ImageIcon("heart.gif");
        this.seqText.setIcon(ic);
        //voidButton.setEnabled(false);
        voidButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voidButtonActionPerformed(evt);
            }
        });
        //seqText = new JLabel();
        seqText.setText("----");
        //selectButton = new JRadioButton();

        selectButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        
        skipButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipButtonActionPerformed(evt);
            }
        });

    }
       
    private void voidButtonActionPerformed(ActionEvent evt) {
        //System.out.println("Void button clicked");
        int pageNum = swingControl.getCurrentPageNumber();
        statusButtonGroup.clearSelection();
        voidButton.setSelected(!statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum]);
        statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum] = !statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum];
        statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum] = false;
        statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum] = false;
        setUpSeq();
    }

    private void selectButtonActionPerformed(ActionEvent evt) {
        //System.out.println("select button clicked");
        int pageNum = swingControl.getCurrentPageNumber();
      
        statusButtonGroup.clearSelection();
        selectButton.setSelected(!statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum]);
        statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum] = !statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum];
        statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum] = false;
        statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum] = false;
        setUpSeq();  
    }
    
    private void skipButtonActionPerformed(ActionEvent evt) {
       // System.out.println("skip button clicked");
        int pageNum = swingControl.getCurrentPageNumber();
        statusButtonGroup.clearSelection();
        skipButton.setSelected(!statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum]);
        statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum] = !statusArray[GlobalVar.SKIP_BUTTON_INDEX][pageNum];
        statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum] = false;
        statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum] = false;
        setUpSeq();
    }

    
    //remove show hide utility pane button
    @Override
    public JToggleButton buildShowHideUtilityPaneButton(){ 
        System.out.println("utility toggle button is removed");
        return null;
    }
    
     //remove search button
    @Override
    public JButton buildSearchButton(){
        System.out.println("search button is removed");
        return null;
    }
       
    @Override
    public JToolBar buildToolToolBar(){
        System.out.println("Tool Toolbar is removed");
        return null;
    }
    
    @Override
    public JToolBar buildAnnotationlToolBar(){
        System.out.println("Annotationl Toolbar is removed");
        JToolBar annotationToolBar = new JToolBar();
        //annotationToolBar.add(statusButtonGroup);
        annotationToolBar.add(voidButton);        
        annotationToolBar.add(skipButton);
        annotationToolBar.add(selectButton);
        annotationToolBar.add(seqText);
        annotationToolBar.add(submitButton);
        return annotationToolBar;
    }   
    
    public void setUpSeq() {
        int pageNum = swingControl.getCurrentPageNumber();
        int selectCount = 0;
        for (int i = 0; i <= pageNum; i++) {
            if(statusArray[GlobalVar.SELECT_BUTTON_INDEX][i]) {
                selectCount++; 
            }
        }
        seqText.setText(GlobalVar.globalCounterGenerator(selectCount));  
    } 
    
}
