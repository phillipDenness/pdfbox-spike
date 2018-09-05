package uk.gov.hmcts.reform.npa;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationPopup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static uk.gov.hmcts.reform.npa.Utils.colourBlue;
import static uk.gov.hmcts.reform.npa.Utils.setAuthorAndDate;


public class AnnotateCommentBox {
    static final float INCH = 72;
    private PDPageTree pages;
    private List newAnnotations = new ArrayList();

    private float pw;
    private float ph;

    public void generateAnnotation() throws IOException {
        File file = new File("stickyNotePdf_2.pdf");

        PDDocument document = PDDocument.load(file);

        pages = document.getDocumentCatalog().getPages();
        PDPage page = pages.get(0);

        pw = page.getMediaBox().getUpperRightX();
        ph = page.getMediaBox().getUpperRightY();
        List<PDAnnotation> annotations = page.getAnnotations();


        for (PDAnnotation pdAnnotation: annotations) {
            COSDictionary dictionary = pdAnnotation.getCOSObject();
            System.out.println("<<<<<<<<<<<<<<<<<NEW ANNO>>>>>>>>>>>>>\n" +
                dictionary +
                "\n" + pdAnnotation.getSubtype() +
                "\n" + dictionary.getString("Contents") +
                "\n" + dictionary.getItem(COSName.RECT)+
                "\n" + dictionary.getItem(COSName.QUADPOINTS));

            addCommentIconAnnotation(pdAnnotation);
        }
//////////////////////////////////////////////////////////////////////////////////////

        page.setAnnotations(newAnnotations);
        document.save("my_doc.pdf");
        document.close();
    }

    public static List<PDAnnotation> addCommentIconAnnotation(PDAnnotation annotation) {

        float pw = annotation.getPage().getMediaBox().getUpperRightX();
        float ph = annotation.getPage().getMediaBox().getUpperRightY();

        COSDictionary dictionary = annotation.getCOSObject();

        if(annotation.getSubtype().equalsIgnoreCase("Text")) {

            PDAnnotationTextMarkup annotationTextMarkup = new PDAnnotationTextMarkup(dictionary);
            annotationTextMarkup.setContents("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"");
            setAuthorAndDate(annotationTextMarkup);

//            annotationTextMarkup.setColor(colourBlue);
//            annotationTextMarkup.setConstantOpacity((float) 0.2);

            // Set the rectangle containing the markup
            PDRectangle position = new PDRectangle();
            position.setLowerLeftX((float) 160.172);
            position.setLowerLeftY((float) (217.321));
            position.setUpperRightX((float) (160.172 + 18));
            position.setUpperRightY((float) ((217.321 + 18)));
            annotationTextMarkup.setRectangle(position);

            annotationTextMarkup.setQuadPoints(Utils.getQuadsWithRectangle(position));

            PDAnnotationPopup subPopup = annotationTextMarkup.getPopup();
//            PDRectangle popupPosition = new PDRectangle();
//            popupPosition.setLowerLeftX((float) 612.0);
//            popupPosition.setLowerLeftY((float) (555.5464));
//            popupPosition.setUpperRightX((float) (812.0));
//            popupPosition.setUpperRightY((float) (425.5464));
//            subPopup.setRectangle(popupPosition);

            System.out.println(annotationTextMarkup.getAppearance());
            PDAppearanceDictionary appearanceDictionary = annotationTextMarkup.getAppearance();
            System.out.println(appearanceDictionary.getRolloverAppearance().getAppearanceStream().getBBox());

            return Arrays.asList(subPopup, annotationTextMarkup);
        }
        throw new UnsupportedOperationException("This method does not support the annotation subtype.");
    }
}
