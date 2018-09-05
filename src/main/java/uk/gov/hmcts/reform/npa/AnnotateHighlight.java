package uk.gov.hmcts.reform.npa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class AnnotateHighlight {

    static final float INCH = 72;
    private static PDPageTree pages;

    public void generateAnnotation() throws IOException {
        File file = new File("./courtPdf.pdf");

        PDDocument document = PDDocument.load(file);

        pages = document.getDocumentCatalog().getPages();
        PDPage page = pages.get(0);

        float pw = page.getMediaBox().getUpperRightX();
        float ph = page.getMediaBox().getUpperRightY();
        List<PDAnnotation> annotations = page.getAnnotations();

        PDColor yellow = new PDColor(new float[]{1, 1, 204 / 255F}, PDDeviceRGB.INSTANCE);

        PDBorderStyleDictionary borderThick = new PDBorderStyleDictionary();
        borderThick.setWidth((INCH / 12));  // 12th inch
        PDBorderStyleDictionary borderThin = new PDBorderStyleDictionary();
        borderThin.setWidth((INCH / 72)); // 1 point
        PDBorderStyleDictionary borderULine = new PDBorderStyleDictionary();
        borderULine.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        borderULine.setWidth((INCH / 72)); // 1 point

        PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
        txtMark.setColor(yellow);
        txtMark.setConstantOpacity((float) 0.2);   // Make the highlight 20% transparent

        // Set the rectangle containing the markup
        PDRectangle position = new PDRectangle();
        position.setLowerLeftX((float) 182.73182345512217);
        position.setLowerLeftY((float) (ph - 236.45362567184563));
        position.setUpperRightX((float) (182.73182345512217 + 125.51378522600446));
        position.setUpperRightY((float) (ph - (236.45362567184563 + 12.380947026991306)));
        txtMark.setRectangle(position);

        annotations.add(txtMark);

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

        txtMark.setQuadPoints(quads);
        txtMark.setContents("Highlighted since it's important");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String meta = "Date: " + sdf.format(Date.from(Instant.now())) + "\nAuthor: Jd Richard Clarke";
        txtMark.setTitlePopup(meta);
        annotations.add(txtMark);

        document.save("./my_doc.pdf");

        document.close();
    }
}
