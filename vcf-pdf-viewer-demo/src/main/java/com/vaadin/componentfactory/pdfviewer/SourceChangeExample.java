package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route(value = "source-change", layout = MainLayout.class)
public class SourceChangeExample extends Div {

    public SourceChangeExample() {

        PdfViewer pdfViewer = new PdfViewer();
        pdfViewer.setSizeFull();

        ComboBox<String> filesComboBox = new ComboBox<>("Select PDF");
        filesComboBox.setWidth("300px");
        filesComboBox.setPlaceholder("Select a file");
        filesComboBox.setItems("bitcoin.pdf", "example.pdf", "example-invoice.pdf");
        filesComboBox.addValueChangeListener(e -> {
            String filename = e.getValue();
            var filePath = "/pdf/" + filename;
            var downloadHandler = DownloadHandler.forClassResource(getClass(), filePath);
            pdfViewer.setSrc(downloadHandler);
        });

        add(filesComboBox, pdfViewer);
    }

}
