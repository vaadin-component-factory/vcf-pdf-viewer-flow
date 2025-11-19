package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "custom-auto-fit-labels", layout = MainLayout.class)
public class CustomAutoFitZoomLabelsExample extends Div {

    public CustomAutoFitZoomLabelsExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.setAutoZoomOptionLabel("automatic-zoom");
        pdfViewer.setPageFitZoomOptionLabel("page-fit");
        add(pdfViewer);
    }

}
