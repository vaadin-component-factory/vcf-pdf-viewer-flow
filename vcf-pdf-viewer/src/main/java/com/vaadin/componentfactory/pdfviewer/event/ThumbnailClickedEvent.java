package com.vaadin.componentfactory.pdfviewer.event;

import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

@DomEvent("thumbnail-clicked")
public class ThumbnailClickedEvent extends ComponentEvent<PdfViewer> {

  private final Integer selectedPage;

  public ThumbnailClickedEvent(PdfViewer source, boolean fromClient,
      @EventData(value = "event.detail.pageNumber") Integer pageNumber) {
    super(source, fromClient);
    this.selectedPage = pageNumber;
  }

  public Integer getSelectedPage() {
    return selectedPage;
  }

}
