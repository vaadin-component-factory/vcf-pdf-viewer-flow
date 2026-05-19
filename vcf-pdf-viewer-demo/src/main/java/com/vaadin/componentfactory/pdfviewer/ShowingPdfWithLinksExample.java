package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "showing-pdf-with-links", layout = MainLayout.class)
public class ShowingPdfWithLinksExample extends Div {

  public ShowingPdfWithLinksExample() {

    PdfViewer pdfViewer = new PdfViewer();
    pdfViewer.setSizeFull();
    pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/links-example.pdf", "links-example.pdf"));
    add(pdfViewer);
  }

}
