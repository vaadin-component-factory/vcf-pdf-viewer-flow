package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "thumbnails-listener", layout = MainLayout.class)
public class ThumbnailsListenerExample extends Div {

    public ThumbnailsListenerExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
        pdfViewer.addThumbnailClickedListener(e -> {
            Notification.show("Selected page on thumbnail click: " + e.getSelectedPage());
        });
        add(pdfViewer);
    }

}
