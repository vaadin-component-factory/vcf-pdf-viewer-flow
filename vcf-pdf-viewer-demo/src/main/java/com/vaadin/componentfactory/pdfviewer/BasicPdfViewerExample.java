package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class BasicPdfViewerExample extends Div {
  
  public BasicPdfViewerExample() {
    
    setWidthFull();

    PdfViewer pdfViewer = new PdfViewer();
    pdfViewer.setSrc("./pdf/example.pdf");
    add(pdfViewer);
    
  }

}
