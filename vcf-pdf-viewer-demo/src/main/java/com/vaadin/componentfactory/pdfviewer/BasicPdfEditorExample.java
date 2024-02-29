package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "basic-pdf-editor-example", layout = MainLayout.class)
public class BasicPdfEditorExample extends Div {

  public BasicPdfEditorExample() {
    getStyle().set("overflow", "auto");

    StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
    PdfEditor pdfEditor = new PdfEditor(resource);
    pdfEditor.setPadding(true);
    pdfEditor.setWidthFull();
    pdfEditor.setHeight("100vh");
    pdfEditor.addSaveListener(pdfBytes -> {
      System.out.println("PDF saved. Size in bytes: " + pdfBytes.length);
    });
    add(pdfEditor);

    // Test
    PdfEditorFrameOld old = new PdfEditorFrameOld();
    //add(old);
    old.setWidthFull();
    old.setHeight("100vh");
    old.setPdfSrc(resource);

    // Test
    PdfEditorFrame2 iframe = new PdfEditorFrame2();
    //add(iframe);
    iframe.setWidthFull();
    iframe.setHeight("100vh");
    iframe.setPdfSrc(resource);

  }
  
}
