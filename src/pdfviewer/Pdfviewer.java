/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfviewer;

//import javax.swing.JFrame;
//import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.icepdf.ri.common.SwingController; 
import org.icepdf.ri.common.SwingViewBuilder;
 import javax.swing.*; 
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import static pdfviewer.GlobalVar.globalCounterGenerator;
/**
 *
 * @author bob
 */
public class Pdfviewer {

    /**
     * @param args the command line arguments
     */
    private static JRadioButton voidButton;
    private static JRadioButton selectButton;
    private static JRadioButton skipButton;
    private static ButtonGroup statusButtonGroup;
    private static JLabel seqText;
    private static Boolean[][] statusArray;
    
    private static JButton submitPDFButton;
    
    
    public static void main(String[] args) {
        GregorianCalendar today = new GregorianCalendar(); 
        String date = today.getTime().toString();
        //File file = new File("test.pdf");
        //COSDocument cos = new COSDocument(file);
        
        String pdfFileName = "test.pdf";
        if (args.length > 1) {
            pdfFileName = args[0];
            date = args[1];
        } else if (args.length > 0) {
            pdfFileName = args[0];
        }
        
        initComponent(pdfFileName, date);
        
       // statusButtonGroup.getElements().nextElement();
//        
//        Boolean[] voidList = new Boolean[MAX_NUM_PAGES];
//        Boolean[] selectList = new Boolean[MAX_NUM_PAGES];
//        Boolean[] skipList = new Boolean[MAX_NUM_PAGES];
        
        
        
//        for (int i = 0; i < MAX_NUM_PAGES; i++) {
//            voidList[i] = false;
//            selectList[i] = false;
//            skipList[i] = false;
//        }
        
        SwingSimpleController controller = new SwingSimpleController(statusButtonGroup, seqText,statusArray); 
        controller.openDocument(pdfFileName); // show the component 
        int pageNum = controller.getDocument().getNumberOfPages();

        System.out.println(controller.getDocument().getNumberOfPages());  
        
        SwingViewSimpleBuilder factory = new SwingViewSimpleBuilder(controller, statusButtonGroup,
                seqText, submitPDFButton,statusArray); 
        
//        factory.buildDocumentMenu();
//        factory.buildViewMenu();
 
        JPanel viewerComponentPanel = factory.buildViewerPanel(); 
        // add interactive mouse link annotation support via callback 
//        controller.getDocumentViewController().setAnnotationCallback(
//                new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController())); 
        
        JFrame applicationFrame = new JFrame(); 
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        applicationFrame.getContentPane().add(viewerComponentPanel); // Now that the GUI is all in place, we can try opening a PDF 
//        controller.openDocument(filePath); // show the component 
//        System.out.println(controller.getDocument().getNumberOfPages());
        applicationFrame.pack(); 
        
        
        applicationFrame.setVisible(true); 
    }    
    
    private static void initComponent(final String pdfFileName, final String date) {
        voidButton = new JRadioButton();
        selectButton = new JRadioButton();
        skipButton = new JRadioButton();
        
        voidButton.setText("Void");        
        skipButton.setText("Skip");
        selectButton.setText("Select");
        
        statusButtonGroup = new ButtonGroup();
        statusButtonGroup.add(voidButton);        
        statusButtonGroup.add(skipButton);
        statusButtonGroup.add(selectButton);
        
        seqText = new JLabel();
        seqText.setText("----");
        statusArray = new Boolean[GlobalVar.NUM_BUTTON][GlobalVar.MAX_NUM_PAGES];
        
        for(int i = 0; i < GlobalVar.NUM_BUTTON; i++){
            for (int j = 0; j < GlobalVar.MAX_NUM_PAGES; j++) {
                statusArray[i][j] = false;
            }
        }
        
        submitPDFButton = new JButton();
        submitPDFButton.setText("Submit PDF");
        
        submitPDFButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    System.out.println("PDF is pressed");
                    generatePDFFile(date, pdfFileName, statusArray);
                    JOptionPane.showMessageDialog(null, "The PDF is created successfully!");   
                } catch (IOException ex) {
                    Logger.getLogger(Pdfviewer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (COSVisitorException ex) {
                    Logger.getLogger(Pdfviewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public static void generatePDFFile(String date, String pdfFileName, Boolean[][] statusArray) throws IOException, COSVisitorException {
        PDDocument pdf = PDDocument.load(pdfFileName);
        //String[] names = pdfFileName.split("\\.");
        String targetFile = "";
        if (pdfFileName.contains(".pdf")) {  //doesn't contain surfix
            targetFile = pdfFileName.replace(".pdf", "_forAudit.pdf");
        } else {
            targetFile = pdfFileName + "_forAudit.pdf";
        }
        
        // == prepare for void mark
        String imageName = "void.jpg";
        BufferedImage buffered = ImageIO.read(new File(imageName));
        PDJpeg voidMark = new PDJpeg(pdf, buffered);
        // == end of preparing for void mark
        
        List pages = pdf.getDocumentCatalog().getAllPages();
        Iterator<PDPage> iter = pages.iterator();
        int pageNum = 0; // 0 based
        int sequenceNum = 1; // start from 0001
        while(iter.hasNext()) {
            PDPage page = iter.next();
            PDPageContentStream stream = new PDPageContentStream(pdf, page, true, false);            
            stream.beginText();
            stream.setFont(PDType1Font.HELVETICA, 20);
            stream.moveTextPositionByAmount(100, 300);            
            stream.drawString(date); //date stamp 
            stream.endText();
             // == void stamp
            if (statusArray[GlobalVar.VOID_BUTTON_INDEX][pageNum]) {
                stream.drawImage(voidMark, 100, 200);
            }
            // == end of void stamp
            
             // == seq stamp
            if (statusArray[GlobalVar.SELECT_BUTTON_INDEX][pageNum]) {
                stream.beginText();
                stream.setFont(PDType1Font.HELVETICA, 24);
                stream.moveTextPositionByAmount(600, 400);
                stream.setTextRotation(3.14/2, 600, 400); // rotate text 90 degree at x = 600, y = 400

                stream.drawString(globalCounterGenerator(sequenceNum));
                sequenceNum++;
                stream.endText();
            }
            // == end of seq stamp
           
            stream.close();
             pageNum++;
        }
        pdf.save(targetFile);
        pdf.close();
        
    
    
    }
    
        // given date and 
    public static String dateStampPDFFile(String date, String pdfFileName) throws IOException, COSVisitorException {
        PDDocument pdf = PDDocument.load(pdfFileName);
        //String[] names = pdfFileName.split("\\.");
        String targetFile = "";
        if (pdfFileName.contains(".pdf")) {  //doesn't contain surfix
            targetFile = pdfFileName.replace(".pdf", "_DS.pdf");
        } else {
            targetFile = pdfFileName + "_DS.pdf";
        }
//        String imageName = "void.jpg";
//        String fileName = "res.pdf"     
        
        List pages = pdf.getDocumentCatalog().getAllPages();
        Iterator<PDPage> iter = pages.iterator();
        int pageNum = 0;
        while(iter.hasNext()) {
            PDPage page = iter.next();
            PDPageContentStream stream = new PDPageContentStream(pdf, page, true, false);
            
            // == date stamp
            stream.beginText();
            stream.setFont(PDType1Font.HELVETICA, 24);
            stream.moveTextPositionByAmount(100, 300);                   
            stream.drawString(date);            
            stream.endText();
            // == end of date stamp
            
           
            stream.close();
        }
        pdf.save(targetFile);
        pdf.close();
        return targetFile;
    }
    
    // given the page number(1 based), and void stamp on the given pdf file
    public static String voidStamp(List<Integer> pageNums, String pdfFileName) throws IOException, COSVisitorException {
        PDDocument pdf = PDDocument.load(pdfFileName);
        String targetFile = "";
        if (pdfFileName.contains(".pdf")) {  //doesn't contain surfix
            targetFile = pdfFileName.replace(".pdf", "_VS.pdf");
        } else {
            targetFile = pdfFileName + "_VS.pdf";
        }
        
        String imageName = "void.jpg";

        
        BufferedImage buffered = ImageIO.read(new File(imageName));
        PDJpeg voidMark = new PDJpeg(pdf, buffered);
//        String imageName = "void.jpg";
//        String fileName = "res.pdf"     
        
        List pages = pdf.getDocumentCatalog().getAllPages();
        Iterator<PDPage> iter = pages.iterator();
        int pageCount = 1; // page number is 1 based
        while(iter.hasNext()) {
            PDPage page = iter.next();
            if (pageNums.contains(pageCount)){
                PDPageContentStream stream = new PDPageContentStream(pdf, page, true, false);
                stream.drawImage(voidMark, 100, 200);
                stream.close();
            }
            pageCount++;
        }
        pdf.save(targetFile);
        pdf.close();
        return targetFile;
    
    }
    
    // given the page number that skips incrementing the seq number, the page number list should contain void page list
    // write the sequence on the right middle of the file    
    public static String sequenceStampPDFFile(List<Integer> pageNums, String pdfFileName) throws IOException, COSVisitorException {
        PDDocument pdf = PDDocument.load(pdfFileName);
        String targetFile = "";
        if (pdfFileName.contains(".pdf")) {  //doesn't contain surfix
            targetFile = pdfFileName.replace(".pdf", "_SS.pdf");
        } else {
            targetFile = pdfFileName + "_SS.pdf";
        }
//        String imageName = "void.jpg";
//        String fileName = "res.pdf"     
        
        List pages = pdf.getDocumentCatalog().getAllPages();
        Iterator<PDPage> iter = pages.iterator();
        int sequenceNum = 1;
        int pageCount = 1;
        while(iter.hasNext()) {
            PDPage page = iter.next();
            PDPageContentStream stream = new PDPageContentStream(pdf, page, true, false);
            if(!pageNums.contains(pageCount)){
                stream.beginText();
                stream.setFont(PDType1Font.HELVETICA, 24);
                stream.moveTextPositionByAmount(600, 400);
                stream.setTextRotation(3.14/2, 600, 400); // rotate text 90 degree at x = 600, y = 400

                stream.drawString(globalCounterGenerator(sequenceNum));
                sequenceNum++;
                stream.endText();
            }
            stream.close();
            pageCount++;
            
        }
        pdf.save(targetFile);
        pdf.close();
        return targetFile;
    }
}
