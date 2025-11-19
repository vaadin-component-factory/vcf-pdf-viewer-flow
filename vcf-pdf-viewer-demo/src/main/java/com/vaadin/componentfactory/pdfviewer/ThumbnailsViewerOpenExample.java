package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "thumbnails-open", layout = MainLayout.class)
public class ThumbnailsViewerOpenExample extends Div {

    public ThumbnailsViewerOpenExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.openThumbnailsView();
        add(pdfViewer);
    }

}
