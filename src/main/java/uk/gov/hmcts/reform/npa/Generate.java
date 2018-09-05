package uk.gov.hmcts.reform.npa;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.npa.Utils.writeAnnotationsToPdf;

public class Generate {

    private PDPageTree pages;
    private List newAnnotations = new ArrayList();
    private AnnotateCommentBox annotateCommentBox = new AnnotateCommentBox();
    private AnnotateHighlightV2 annotateHighlightV2 = new AnnotateHighlightV2();

    public void createHighlightAndPopup() throws IOException {
        ClassLoader classLoader = Application.class.getClassLoader();
        File file = new File(classLoader.getResource("pdf/highlightAndPopup.pdf").getFile());

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
                newAnnotations.add(annotateHighlightV2.addHighlightAnnotation(pdAnnotation));
            } else if(pdAnnotation.getSubtype().equalsIgnoreCase("Text")) {
                newAnnotations.addAll(annotateCommentBox.addCommentIconAnnotation(pdAnnotation));
            }
        }

        page.setAnnotations(newAnnotations);
        writeAnnotationsToPdf(document);
    }
}
