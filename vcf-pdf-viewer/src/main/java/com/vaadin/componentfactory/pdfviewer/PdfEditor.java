/*-
 * #%L
 * Pdf Viewer
 * %%
 * Copyright (C) 2021 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.server.StreamResource;

import java.util.function.Consumer;

public class PdfEditor extends VerticalLayout implements HasStyle {
    public HorizontalLayout hlTop = new HorizontalLayout();
    public Select<StreamResource> selectPdf = new Select<>();
    public Button btnSave = new Button("Save");
    public H3 titlePdfName = new H3("...");
    public PdfEditorFrame editorFrame = new PdfEditorFrame();

    public PdfEditor() {
        this(null);
    }

    public PdfEditor(StreamResource pdf) {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        titlePdfName.getStyle().set("margin", "0px");
        hlTop.setWidthFull();
        hlTop.addAndExpand(selectPdf);
        hlTop.add(btnSave);
        hlTop.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(hlTop);
        editorFrame.getStyle().set("width", "100%");
        addAndExpand(editorFrame);

        if(pdf != null) setSrc(pdf);
        selectPdf.addValueChangeListener(e -> {
            setSrc(e.getValue());
        });
        selectPdf.setPlaceholder("Change file");
    }

    public void setSrc(StreamResource pdf){
        editorFrame.setPdfSrc(pdf);
    }

    public String getSrc(){
        return editorFrame.getPdfSrcUrl();
    }

    /**
     * Gets executed when user clicks the save button and provides the edited pdf in binary format.
     */
    public void addSaveListener(Consumer<byte[]> listener){
        btnSave.addClickListener(e -> {
            editorFrame.save(listener);
        });
    }
}
