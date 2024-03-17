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
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.communication.PushMode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

//@CssImport("./pdfjs/combined-viewer-prod.css")
//@JsModule("https://mozilla.github.io/pdf.js/build/pdf.mjs")
//@JsModule("https://mozilla.github.io/pdf.js/web/viewer.mjs")
// ISSUE WITH THIS APPROACH: Doesn't work because on customers website gets blocked by:
// :chrome-error://chromewebdata/:1 Refused to display 'http://localhost:9090/' in a frame because it set 'X-Frame-Options' to 'deny'.
// ---> Upgrading to latest Vaadin seems to fix this issue.
public class PdfEditorFrameAlt extends IFrame implements HasStyle {
    public CopyOnWriteArrayList<Consumer<String>> onSave = new CopyOnWriteArrayList<>();
    private static String editorHtml;

    static {
        try {
            editorHtml = Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer-orig.html"));
            editorHtml = editorHtml.replace("/* INSERT viewer-orig.css STYLE */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer-orig.css")));
            editorHtml = editorHtml.replace("/* INSERT pdf.mjs SCRIPT */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/pdf.mjs")));
            editorHtml = editorHtml.replace("/* INSERT viewer.mjs SCRIPT */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer.mjs")));
            editorHtml = editorHtml.replace("/* INSERT viewer-iframe-connector.js SCRIPT */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer-iframe-connector.js")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CopyOnWriteArrayList<Runnable> onPdfJsLoaded = new CopyOnWriteArrayList<>();
    public volatile boolean isPdfJsLoaded = false;

    public PdfEditorFrameAlt() {
        setSrc(new StreamResource("pdf-editor.html", () -> new ByteArrayInputStream(editorHtml.getBytes())));
        getElement().setAttribute("frameBorder", "0");
        addAttachListener(e -> {
            if(!e.isInitialAttach()) return;
            this.getElement().executeJs("" +
                    "let this_ = this\n" +
                    "window.document.addEventListener('pdf-editor-save-response', handleSaveResponse, false)\n" +
                    "function handleSaveResponse(e) {\n" +
                    "  this_.$server.pdfEditorSaveResponse(e.detail);\n" +
                    "}\n" +
                    "return new Promise(function (resolve) {\n" +
                    "    window.document.addEventListener('pdf-editor-finished', function handleFinished() {\n" +
                    "        resolve();\n" +
                    "    }, false);\n" +
                    "});\n" +
                    "", this).then(e2 -> {
                        isPdfJsLoaded = true;
                for (Runnable runnable : onPdfJsLoaded) {
                    runnable.run();
                }
            });
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
        PdfEditorFrameAlt this_ = this;
        if(!isPdfJsLoaded) onPdfJsLoaded.add(new Runnable() {
            @Override
            public void run() {
                this_.getElement().executeJs(js, parameters); // Run later
                onPdfJsLoaded.remove(this);
            }
        });
        else this.getElement().executeJs(js, parameters); // Run now
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
