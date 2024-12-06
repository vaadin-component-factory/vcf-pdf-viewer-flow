package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "with-rotate-options", layout = MainLayout.class)
public class WithRotateOptionsExample extends Div {
  
  public WithRotateOptionsExample() {
    
    PdfViewer pdfViewer = new PdfViewer();
    pdfViewer.setSizeFull();
    StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
    pdfViewer.setSrc(resource);
    pdfViewer.setAddRotateClockwiseButton(true);
    pdfViewer.setAddRotateCounterClockwiseButton(true);    
    add(pdfViewer);    
  }
  
}
