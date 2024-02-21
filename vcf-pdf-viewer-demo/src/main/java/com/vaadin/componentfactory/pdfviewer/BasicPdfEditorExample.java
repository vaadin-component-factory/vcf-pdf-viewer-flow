package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "basic-pdf-editor-example", layout = MainLayout.class)
public class BasicPdfEditorExample extends Div {

  public BasicPdfEditorExample() {

    PdfEditor pdfEditor = new PdfEditor();
    pdfEditor.setPadding(true);
    pdfEditor.setSizeFull();
    StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
    pdfEditor.setSrc(resource);
    pdfEditor.addSaveListener(pdfBytes -> {
      System.out.println("PDF saved. Size in bytes: " + pdfBytes.length);
    });
    add(pdfEditor);    
  }
  
}
