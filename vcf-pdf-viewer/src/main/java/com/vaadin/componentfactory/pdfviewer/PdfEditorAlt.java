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

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.server.StreamResource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Alternative (Alt) PDF-Editor that uses IFrame. <br>
 * Use this if there are issues with the regular PDF-Editor. <br>
 */
public class PdfEditorAlt extends VerticalLayout implements HasStyle {
    public HorizontalLayout hlTop = new HorizontalLayout();
    public Select<StreamResource> selectPdf = new Select<>();
    /**
     * Add a new blank page at the end.
     */
    public Button btnAddPage = new Button("+Page");
    public Button btnSave = new Button("Save");
    public H3 titlePdfName = new H3("...");
    public PdfEditorFrameAlt editorFrame = new PdfEditorFrameAlt();

    public PdfEditorAlt() {
        this(null);
    }

    public PdfEditorAlt(StreamResource pdf) {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        titlePdfName.getStyle().set("margin", "0px");
        hlTop.setWidthFull();
        hlTop.addAndExpand(selectPdf);
        hlTop.add(btnAddPage, btnSave);
        hlTop.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(hlTop);
        editorFrame.getStyle().set("width", "100%");
        addAndExpand(editorFrame);

        selectPdf.addValueChangeListener(e -> {
            editorFrame.setPdfSrc(e.getValue());
        });
        selectPdf.setTextRenderer(StreamResource::getName);
        selectPdf.setPlaceholder("Change file");
        if(pdf != null) {
            selectPdf.setItems(pdf);
            selectPdf.setValue(pdf);
        }

        btnAddPage.addClickListener(e -> {
            Dialog d = new Dialog();
            d.add("By adding a new blank page all the current edits will be saved and unmodifiable.");

            Button btnConfirm = new Button("Continue.");
            d.add(btnConfirm);
            btnConfirm.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            btnConfirm.addClickListener(e2 -> {
                d.close();
                editorFrame.addBlankPage();
            });
            d.open();
        });
    }

    public void setSrc(StreamResource pdf){
        List<StreamResource> items = new ArrayList<>();
        try{
            // Compatible with older vaadin versions
            selectPdf.getDataProvider().fetch(new Query<>()).forEach(items::add);
        } catch (Exception e) {}
        if(!items.contains(pdf)){
            items.add(pdf);
            // setItems(Collection<T>) does not exist in older vaadin versions?
            selectPdf.setItems(items.toArray(new StreamResource[0]));
        }
        selectPdf.setValue(pdf);
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
