package uk.gov.hmcts.reform.npa;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.springframework.stereotype.Component;

import static uk.gov.hmcts.reform.npa.Utils.setAuthorAndDate;

@Component
public class AnnotateHighlightV2 {

    public PDAnnotationTextMarkup addHighlightAnnotation(PDAnnotation annotation) {

        if (annotation.getSubtype().equalsIgnoreCase("Highlight")) {
            PDAnnotationTextMarkup annotationHighlightMarkup = new PDAnnotationTextMarkup(annotation.getCOSObject());

            // Set the rectangle containing the markup
            PDRectangle position = new PDRectangle();
            position.setLowerLeftX((float) 170.423);
            position.setLowerLeftY((float) (497.219));
            position.setUpperRightX((float) (292.273));
            position.setUpperRightY((float) (510.461));
            annotationHighlightMarkup.setRectangle(position);

            annotationHighlightMarkup.setQuadPoints(Utils.getQuadsWithRectangle(position));
            annotationHighlightMarkup.setContents(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            setAuthorAndDate(annotationHighlightMarkup);

            return annotationHighlightMarkup;
        }
        throw new UnsupportedOperationException("This method does not support the annotation subtype.");
    }
}
