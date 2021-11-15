package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "", layout = MainLayout.class)
public class BasicPdfViewerExample extends Div {
  
  public BasicPdfViewerExample() {
    
    PdfViewer pdfViewer = new PdfViewer();
    StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
    pdfViewer.setSrc(resource);
    add(pdfViewer);    
  }
  
}
