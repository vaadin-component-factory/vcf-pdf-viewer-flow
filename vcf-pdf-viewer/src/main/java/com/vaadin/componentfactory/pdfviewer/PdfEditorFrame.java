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
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
//@CssImport("./pdfjs/combined-viewer-prod.css")
//@JsModule("https://mozilla.github.io/pdf.js/build/pdf.mjs")
//@JsModule("https://mozilla.github.io/pdf.js/web/viewer.mjs")
//@CssImport("./pdfjs/viewer-mod.css")
public class PdfEditorFrame extends Html implements HasStyle {
    public CopyOnWriteArrayList<Consumer<String>> onSave = new CopyOnWriteArrayList<>();
    private static String editorHtml;

    static {
        try {
            editorHtml = Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer-mod.html"));
            editorHtml = editorHtml.replace("/* INSERT viewer-mod.css STYLE */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer-mod.css")));
            editorHtml = editorHtml.replace("/* INSERT pdf.mjs SCRIPT */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/pdf.mjs")));
            editorHtml = editorHtml.replace("/* INSERT viewer.mjs SCRIPT */",
                    Utils.toUTF8String(Utils.getResource("/META-INF/resources/frontend/pdfjs/viewer.mjs")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CopyOnWriteArrayList<Runnable> onPdfJsLoaded = new CopyOnWriteArrayList<>();
    public volatile boolean isPdfJsLoaded = false;

    public PdfEditorFrame() {
        super(editorHtml);
        setSrc(new StreamResource("pdf-editor.html", () -> new ByteArrayInputStream(editorHtml.getBytes())));
        addAttachListener(e -> {
            if(!e.isInitialAttach()) return;
            this.getElement().executeJs("" +
                    "console.log(document.getElementsByClassName(`pdf-editor-html`)[0])\n" +
                    "let this_ = this;\n" +
                    "async function loadScripts() {\n" +
                    "  try {\n" +
                    "    window.onmessage = function(e) {\n" +
                    "      let type = e.data.type;\n" +
                    "      let msg = e.data.msg;\n" +
                    "      if(type == 'save-response') this_.$server.pdfEditorSaveResponse(msg);\n" +
                    "    }\n" +
                    "    const module = await import(\"https://mozilla.github.io/pdf.js/build/pdf.mjs\");\n" +
                    "    const module2 = await import(\"https://mozilla.github.io/pdf.js/web/viewer.mjs\");\n" +
                    "    const module3 = await import(\"https://unpkg.com/pdf-lib/dist/pdf-lib.min.js\");\n" +
                    "    const promiseA = new Promise((resolve, reject) => {\n" +
                    "      $0.onload = function() { resolve(0); };\n" +
                    "    });" +
                    "    //await promiseA;\n" +
                    "    console.log('Pdf.js scripts loaded successfully!');\n" +
                    "  } catch (error) {\n" +
                    "    console.error('Error loading script!', error);\n" +
                    "  }\n" +
                    "}" +
                    "\n" +
                    "return loadScripts();\n" +
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
        executeSafeJS("let url = `"+url+"`\n" +
                "              console.log(\"LOADING PDF FROM: \" + url)\n" +
                "              window.pdfjsLib.getDocument(url).promise.then((pdf) =>{\n" +
                "                console.log(`PDF LOADED: `); console.log(pdf);\n" +
                "                window.PDFViewerApplication.load(pdf)\n" +
                "              })");
    }

    public void executeAsyncSafeJS(String js, Serializable... parameters){
        executeSafeJS("let async_this = this; async function executeAsyncSafeJS(){\n"+js+"\n}\n return executeAsyncSafeJS();\n", parameters);
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
        executeAsyncSafeJS("" +
                js_save()+
                "let u8 = await save();\n" +
                "                let binaryString = new Uint8Array(u8).reduce((data, byte) => data + String.fromCharCode(byte), '');\n" +
                "                let b64encoded = btoa(binaryString);\n" +
                "                async_this.$server.pdfEditorSaveResponse(b64encoded);\n", this);
    }

    private String js_save(){
        return "  async function save(options = {}) {\n" +
                "    if (PDFViewerApplication._saveInProgress) {\n" +
                "      return;\n" +
                "    }\n" +
                "    PDFViewerApplication._saveInProgress = true;\n" +
                "    await PDFViewerApplication.pdfScriptingManager.dispatchWillSave();\n" +
                "\n" +
                "    const url = PDFViewerApplication._downloadUrl,\n" +
                "      filename = PDFViewerApplication._docFilename;\n" +
                "    try {\n" +
                "      PDFViewerApplication._ensureDownloadComplete();\n" +
                "\n" +
                "      return await PDFViewerApplication.pdfDocument.saveDocument();\n" +
                "    } catch (reason) {\n" +
                "      // When the PDF document isn't ready, or the PDF file is still\n" +
                "      // downloading, simply fallback to a \"regular\" download.\n" +
                "      console.error(`Error when saving the document: ${reason.message}`);\n" +
                "    } finally {\n" +
                "      await PDFViewerApplication.pdfScriptingManager.dispatchDidSave();\n" +
                "      PDFViewerApplication._saveInProgress = false;\n" +
                "    }\n" +
                "\n" +
                "    if (PDFViewerApplication._hasAnnotationEditors) {\n" +
                "      PDFViewerApplication.externalServices.reportTelemetry({\n" +
                "        type: \"editing\",\n" +
                "        data: { type: \"save\" },\n" +
                "      });\n" +
                "    }\n" +
                "  }\n";
    }

    public void addBlankPage(){
        // Not supported by pdf.js natively: https://github.com/mozilla/pdf.js/issues/17500
        // Thus this workaround with the help of pdf-lib is required.
        executeAsyncSafeJS("const { PDFDocument } = PDFLib;\n" +
                js_save()+
                "let u8 = await save();\n" +
                " const pdfDoc = await PDFDocument.load(u8, { \n" +
                "    updateMetadata: false \n" +
                "  })\n" +
                "      const lastPage = pdfDoc.getPage(pdfDoc.getPageCount() - 1) || pdfDoc.addPage();\n" +
                "      const newPage = pdfDoc.addPage();\n" +
                "      newPage.setSize(lastPage.getWidth(), lastPage.getHeight());" +
                "// Perform operations on the page, if needed\n" +
                "\n" +
                "const pdfBytes = await pdfDoc.save();\n" +
                "// Load back into viewer\n" +
                "let binaryString = new Uint8Array(pdfBytes).reduce((data, byte) => data + String.fromCharCode(byte), '');\n" +
                "let b64encoded = btoa(binaryString);\n" +
                "const pdfDataUri = 'data:application/pdf;base64,' + b64encoded;\n" +
                "window.pdfjsLib.getDocument(pdfDataUri).promise.then(pdfDoc => {\n" +
                "  // Now you can work with the PDF document\n" +
                "  const numPages = pdfDoc.numPages;\n" +
                "  console.log(`New number of pages: ${numPages}`);\n" +
                "  window.PDFViewerApplication.load(pdfDoc)" +
                "});");
    }

}
