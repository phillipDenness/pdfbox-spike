package uk.gov.hmcts.reform.npa;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Generate generate = new Generate();
        generate.createHighlightAndPopup();
    }
}
