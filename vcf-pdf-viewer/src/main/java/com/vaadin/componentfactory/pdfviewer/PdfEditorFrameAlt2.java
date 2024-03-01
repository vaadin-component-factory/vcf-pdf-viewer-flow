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

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

//@CssImport("./pdfjs/combined-viewer-prod.css")
//@JsModule("https://mozilla.github.io/pdf.js/build/pdf.mjs")
//@JsModule("https://mozilla.github.io/pdf.js/web/viewer.mjs")
// ISSUE WITH THIS APPROACH: Uncaught DOMException: Failed to set a named property 'function' on 'Window': Blocked a frame with origin "http://localhost:8080" from accessing a cross-origin frame.
//    at <anonymous>:1:30
// In short we can't execute JS inside the frame bc its another URL.
public class PdfEditorFrameAlt2 extends IFrame implements HasStyle {
    public CopyOnWriteArrayList<Consumer<String>> onSave = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Runnable> onPdfJsLoaded = new CopyOnWriteArrayList<>();
    public volatile boolean isPdfJsLoaded = false;
    public final long id = idCounter.getAndIncrement();
    public final String idString = "pdf-editor-"+id;
    public static AtomicLong idCounter = new AtomicLong();

    public PdfEditorFrameAlt2() {
        setId(idString);
        setSrc("https://mozilla.github.io/pdf.js/web/viewer.html");
        addAttachListener(e -> {
            if(!e.isInitialAttach()) return;
            isPdfJsLoaded = true;
            for (Runnable runnable : onPdfJsLoaded) {
                runnable.run();
            }
            executeSafeJSInside("console.log(`hello!`)");
        });
    }

    public void sendMessage(String type, String msg){
        executeSafeJS("$0.contentWindow.postMessage({'type': `"+type+"`, 'msg': `"+msg+"`}, '*');", this);
    }


    private AbstractStreamResource src;
    private AbstractStreamResource pdfSrc;

    public void setPdfSrc(StreamResource src){
        this.pdfSrc = src;
        getElement().setAttribute("pdfsrc", src);
        String url = getElement().getAttribute("pdfsrc");
        setPdfSrc(url);
    }

    public String getPdfSrcUrl() {
        return getElement().getAttribute("pdfsrc");
    }

    public AbstractStreamResource getPdfSrc() {
        return pdfSrc;
    }

    public void setPdfSrc(String url){
        sendMessage("change-pdf-request", url);
    }

    public void executeSafeJS(String js, Serializable... parameters){
        PdfEditorFrameAlt2 this_ = this;
        if(!isPdfJsLoaded) onPdfJsLoaded.add(new Runnable() {
            @Override
            public void run() {
                this_.getElement().executeJs(js, parameters); // Run later
                onPdfJsLoaded.remove(this);
            }
        });
        else this.getElement().executeJs(js, parameters); // Run now
    }

    public void executeSafeJSInside(String js, Serializable... parameters){
        executeSafeJS("function f() {\n "+js+" \n}\n document.getElementById(`"+idString+"`).contentWindow.eval(f.toString());\n",
                parameters);
    }

    public void setSrc(AbstractStreamResource src) {
        this.src = src;
        getElement().setAttribute("src", src);
    }

    public String getSrcUrl() {
        return getElement().getAttribute("src");
    }



    public AbstractStreamResource getSrcStreamResource(){
        return src;
    }

    @ClientCallable
    public void pdfEditorSaveResponse(String pdfBase64){
        for (Consumer<String> code : onSave) {
            code.accept(pdfBase64);
        }
    }

    public void save(Consumer<byte[]> onPDFReceived){
        onSave.add(new Consumer<String>() {
            @Override
            public void accept(String pdfBase64) {
                byte[] pdf = Base64.getDecoder().decode(pdfBase64.getBytes());
                onPDFReceived.accept(pdf);
                onSave.remove(this);
            }
        });
        sendMessage("save-request", "");
    }

    public void addBlankPage() {
        sendMessage("add-blank-page", "");
    }
}
