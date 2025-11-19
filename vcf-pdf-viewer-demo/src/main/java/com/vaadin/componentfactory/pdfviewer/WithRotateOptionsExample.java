package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "with-rotate-options", layout = MainLayout.class)
public class WithRotateOptionsExample extends Div {

    public WithRotateOptionsExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.setAddRotateClockwiseButton(true);
        pdfViewer.setAddRotateCounterClockwiseButton(true);
        add(pdfViewer);
    }

}
