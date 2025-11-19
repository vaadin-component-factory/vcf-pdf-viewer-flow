package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "zoom", layout = MainLayout.class)
public class AdjustZoomPdfViewerExample extends Div {

    public AdjustZoomPdfViewerExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();

        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.setZoom("page-fit");
        add(pdfViewer);
    }

}
