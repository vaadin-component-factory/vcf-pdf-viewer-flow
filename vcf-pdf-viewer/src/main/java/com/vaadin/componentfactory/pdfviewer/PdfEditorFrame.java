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
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.util.Streams;

import javax.annotation.security.RunAs;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
@CssImport("./pdfjs/viewer.css")
//@JsModule("https://mozilla.github.io/pdf.js/build/pdf.mjs")
//@JsModule("https://mozilla.github.io/pdf.js/web/viewer.mjs")
public class PdfEditorFrame extends Html implements HasStyle {
    public CopyOnWriteArrayList<Consumer<String>> onSave = new CopyOnWriteArrayList<>();
    private static String editorHtml;

    static {
        try {
            editorHtml = Streams.asString(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer.html"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CopyOnWriteArrayList<Runnable> onPdfJsLoaded = new CopyOnWriteArrayList<>();
    public volatile boolean isPdfJsLoaded = false;

    public PdfEditorFrame() {
        super(editorHtml);
        addAttachListener(e -> {
            if(!e.isInitialAttach()) return;
            this.getElement().executeJs("" +
                    "async function loadScripts() {\n" +
                    "  try {\n" +
                    "    const module = await import(\"https://mozilla.github.io/pdf.js/build/pdf.mjs\");\n" +
                    "    const module2 = await import(\"https://mozilla.github.io/pdf.js/web/viewer.mjs\");" +
                    "    console.log('Pdf.js scripts loaded successfully!');\n" +
                    "  } catch (error) {\n" +
                    "    console.error('Error loading script!', error);\n" +
                    "  }\n" +
                    "}" +
                    "\n" +
                    "return loadScripts();\n" +
                    "").then(e2 -> {
                        isPdfJsLoaded = true;
                for (Runnable runnable : onPdfJsLoaded) {
                    runnable.run();
                }
            });
        });
    }
    private AbstractStreamResource src;

    public void setSrc(String url){
        executeSafeJS("" +
                "console.log(`LOADING PDF FROM: " +url+"`)\n"+
                "window.pdfjsLib.getDocument(`"+url+"`).promise.then((pdf) =>{\n" +
                "    console.log(`PDF LOADED: `); console.log(pdf);" +
                "    window.PDFViewerApplication.load(pdf)\n" +
                "})");
    }

    public void executeSafeJS(String js, Serializable... parameters){
        PdfEditorFrame this_ = this;
        if(!isPdfJsLoaded) onPdfJsLoaded.add(new Runnable() {
            @Override
            public void run() {
                this_.getElement().executeJs(js, parameters); // Run later
                onPdfJsLoaded.remove(this);
            }
        });
        else this.getElement().executeJs(js, parameters); // Run now
    }

    /**
     * Sets a pdf file to render as a StreamResource.
     * <p>
     * Example: {@code StreamResource resource = new StreamResource("mypdf.pdf", ()
     * -&gt; getPdfInputStream("mypdf.pdf");}
     *
     * @param src stream to file
     */
    public void setSrc(AbstractStreamResource src) {
        this.src = src;
        getElement().setAttribute("src", src);
        String url = getElement().getAttribute("src");
        setSrc(url);
    }

    public String getSrcUrl() {
        return getElement().getAttribute("src");
    }

    public AbstractStreamResource getSrc(){
        return src;
    }

    @ClientCallable
    public void pdfEditorSaveResponse(String pdfBase64){
        for (Consumer<String> code : onSave) {
            code.accept(pdfBase64);
        }
    }

    public void save(Consumer<StreamResource> onPDFReceived){
        onSave.add(new Consumer<String>() {
            @Override
            public void accept(String pdfBase64) {
                byte[] pdf = Base64.decodeBase64(pdfBase64);
                onPDFReceived.accept(new StreamResource("in-editor.pdf", () ->
                        new ByteArrayInputStream(pdf)));
                onSave.remove(this);
            }
        });
        executeSafeJS("" +
                "let u8 = window.PDFViewerApplication.pdfDocument.getData();\n" +
                "let decoder = new TextDecoder('utf8');\n" +
                "let b64encoded = btoa(decoder.decode(u8));\n" +
                "this.$server.pdfEditorSaveResponse(b64encoded});\n");
    }

}
