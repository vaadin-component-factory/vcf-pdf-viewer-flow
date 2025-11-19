package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "without-download", layout = MainLayout.class)
public class WithoutDownloadExample extends Div {

    public WithoutDownloadExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.setAddDownloadButton(false);
        add(pdfViewer);
    }

}
