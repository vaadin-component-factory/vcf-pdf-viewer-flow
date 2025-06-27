package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "", layout = MainLayout.class)
public class BasicPdfViewerExample extends Div {
  
  public BasicPdfViewerExample() {
    
    PdfViewer pdfViewer = new PdfViewer();
    pdfViewer.setSizeFull();
    pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf", "example.pdf"));
    add(pdfViewer);    
  }
  
}
