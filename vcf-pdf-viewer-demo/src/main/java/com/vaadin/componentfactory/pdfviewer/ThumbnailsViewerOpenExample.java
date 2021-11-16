package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "thumbnails-open", layout = MainLayout.class)
public class ThumbnailsViewerOpenExample extends Div {
  
  public ThumbnailsViewerOpenExample() {
    
    PdfViewer pdfViewer = new PdfViewer();
    pdfViewer.setSizeFull();
    StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
    pdfViewer.setSrc(resource);
    pdfViewer.openThumbnailsView();
    add(pdfViewer);    
  }
  
}
