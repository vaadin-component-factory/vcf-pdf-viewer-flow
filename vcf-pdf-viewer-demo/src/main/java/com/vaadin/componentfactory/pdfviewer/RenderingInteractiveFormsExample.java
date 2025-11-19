package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "rendering-interactive-forms", layout = MainLayout.class)
public class RenderingInteractiveFormsExample extends Div {

    public RenderingInteractiveFormsExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();
        pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/pdf-form-example.pdf"));
        pdfViewer.setRenderInteractiveForms(false);
        add(pdfViewer);
    }

}
