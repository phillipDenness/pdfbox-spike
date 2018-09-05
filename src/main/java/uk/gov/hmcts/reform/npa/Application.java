package uk.gov.hmcts.reform.npa;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationMarkup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationPopup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationText;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static uk.gov.hmcts.reform.npa.AnnotateCommentBox.addCommentIconAnnotation;
import static uk.gov.hmcts.reform.npa.AnnotateHighlightV2.addHighlightAnnotation;
import static uk.gov.hmcts.reform.npa.Utils.writeAnnotationsToPdf;

public class Application {
    static final float INCH = 72;
    private static PDPageTree pages;
    private static List newAnnotations = new ArrayList();

    public static void main(String[] args) throws IOException {

        File file = new File("highlightAndPopup.pdf");

        PDDocument document = PDDocument.load(file);

        pages = document.getDocumentCatalog().getPages();
        PDPage page = pages.get(0);
        List<PDAnnotation> annotations = page.getAnnotations();

        for (PDAnnotation pdAnnotation: annotations) {
            COSDictionary dictionary = pdAnnotation.getCOSObject();

            System.out.println("<<<<<<<<<<<<<<<<<NEW ANNO>>>>>>>>>>>>>\n" +
                dictionary +
                "\n" + pdAnnotation.getSubtype() +
                "\n" + dictionary.getString("Contents") +
                "\n" + dictionary.getItem(COSName.RECT)+
                "\n" + dictionary.getItem(COSName.QUADPOINTS));

            if (pdAnnotation.getSubtype().equalsIgnoreCase("Highlight")) {
                newAnnotations.add(addHighlightAnnotation(pdAnnotation));
            } else if(pdAnnotation.getSubtype().equalsIgnoreCase("Text")) {
                newAnnotations.addAll(addCommentIconAnnotation(pdAnnotation));
            }
        }
//////////////////////////////////////////////////////////////////////////////////////

        page.setAnnotations(newAnnotations);
        writeAnnotationsToPdf(document);
    }
}
