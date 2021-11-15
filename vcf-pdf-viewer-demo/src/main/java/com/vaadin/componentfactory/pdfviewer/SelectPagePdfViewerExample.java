package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "select-page", layout = MainLayout.class)
public class SelectPagePdfViewerExample extends Div {
  
  public SelectPagePdfViewerExample() {
    
    PdfViewer pdfViewer = new PdfViewer();
    StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
    pdfViewer.setSrc(resource);
    pdfViewer.setPage(4);
    add(pdfViewer);    
   
    HorizontalLayout layout = new HorizontalLayout();
    layout.setMargin(true);
    layout.add(new Button("Click to know current page", e -> {
      Notification.show("Current Page: " + pdfViewer.getCurrentPage());
    }));
    add(layout);
  }
  
}
