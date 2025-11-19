package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "with-print", layout = MainLayout.class)
public class WithPrintOptionExample extends Div {

    public WithPrintOptionExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.setAddPrintButton(true);
        add(pdfViewer);
    }

}
