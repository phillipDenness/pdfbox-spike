package uk.gov.hmcts.reform.npa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationMarkup;

import java.io.IOException;
import java.util.Calendar;

public class Utils {

    static PDColor colourRed = new PDColor(new float[]{1,0,0}, PDDeviceRGB.INSTANCE);
    static PDColor colourBlue = new PDColor(new float[]{0,0,1}, PDDeviceRGB.INSTANCE);
    static PDColor colourBlack = new PDColor(new float[]{0,0,0}, PDDeviceRGB.INSTANCE);


    public static float[] getQuadsWithRectangle(PDRectangle position) {

        // work out the points forming the four corners of the annotations
        // set out in anti clockwise form (Completely wraps the text)
        // OK, the below doesn't match that description.
        // It's what acrobat 7 does and displays properly!
        float[] quads = new float[8];

        quads[0] = position.getLowerLeftX();  // x1
        quads[1] = position.getUpperRightY() - 2; // y1
        quads[2] = position.getUpperRightX(); // x2
        quads[3] = quads[1]; // y2
        quads[4] = quads[0];  // x3
        quads[5] = position.getLowerLeftY() - 2; // y3
        quads[6] = quads[2]; // x4
        quads[7] = quads[5]; // y5

        return quads;
    }

    public static void writeAnnotationsToPdf(PDDocument document) {
        try {
            document.save("my_doc.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAuthorAndDate(PDAnnotationMarkup annotationMarkup) {
        annotationMarkup.setTitlePopup("Phillip Denness");
        annotationMarkup.setCreationDate(Calendar.getInstance());

    }
 }
