package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
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

    // Alternative editor using iframe
    PdfEditorAlt pdfEditorAlt = new PdfEditorAlt(resource);
    add(pdfEditorAlt);
    pdfEditorAlt.setPadding(true);
    pdfEditorAlt.setWidthFull();
    pdfEditorAlt.setHeight("100vh");
    pdfEditorAlt.addSaveListener(pdfBytes -> {
      System.out.println("(ALTERNATIVE) PDF saved. Size in bytes: " + pdfBytes.length);
    });

    // Test
    PdfEditorFrameAlt2 iframe = new PdfEditorFrameAlt2();
    //add(iframe);
    iframe.setWidthFull();
    iframe.setHeight("100vh");
    iframe.setPdfSrc(resource);

  }
  
}
