package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "rendering-interactive-forms", layout = MainLayout.class)
public class RenderingInteractiveFormsExample extends Div {
  
  public RenderingInteractiveFormsExample() {
    
    PdfViewer pdfViewer = new PdfViewer();
    pdfViewer.setSizeFull();
    StreamResource resource = new StreamResource("Pdf Form Example", () -> getClass().getResourceAsStream("/pdf/pdf-form-example.pdf"));
    pdfViewer.setSrc(resource);
    pdfViewer.setRenderInteractiveForms(false);
    add(pdfViewer);    
  }
  
}
