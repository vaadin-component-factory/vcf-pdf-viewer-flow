    (async () => {
        await import("https://unpkg.com/pdf-lib/dist/pdf-lib.min.js");

        async function save(options = {}) {
            if (PDFViewerApplication._saveInProgress) {
                return;
            }
            PDFViewerApplication._saveInProgress = true;
            await PDFViewerApplication.pdfScriptingManager.dispatchWillSave();

            const url = PDFViewerApplication._downloadUrl,
                filename = PDFViewerApplication._docFilename;
            try {
                PDFViewerApplication._ensureDownloadComplete();

                return await PDFViewerApplication.pdfDocument.saveDocument();
            } catch (reason) {
                // When the PDF document isn't ready, or the PDF file is still
                // downloading, simply fallback to a "regular" download.
                console.error(`Error when saving the document: ${reason.message}`);
            } finally {
                await PDFViewerApplication.pdfScriptingManager.dispatchDidSave();
                PDFViewerApplication._saveInProgress = false;
            }

            if (PDFViewerApplication._hasAnnotationEditors) {
                PDFViewerApplication.externalServices.reportTelemetry({
                    type: "editing",
                    data: {
                        type: "save"
                    },
                });
            }
        }



        window.onmessage = async function(e) {
            let type = e.data.type
            let msg = e.data.msg
            if (type == 'save-request') {
                let u8 = await save();
                let binaryString = new Uint8Array(u8).reduce((data, byte) => data + String.fromCharCode(byte), '');
                let b64encoded = btoa(binaryString);
                var event = new CustomEvent('pdf-editor-save-response', {detail: b64encoded})
                window.parent.document.dispatchEvent(event)
            }
            if (type == 'change-pdf-request') {
                // Because we are using an iframe, pdf.js somehow duplicates the url if it's not absolute, thus do the following
                if (!msg.startsWith('http')) {
                    if (!msg.startsWith('/')) msg = "/" + msg
                    msg = window.location.origin + msg
                }
                let url = msg
                console.log("LOADING PDF FROM: " + url)
                window.pdfjsLib.getDocument(url).promise.then((pdf) => {
                    console.log(`PDF LOADED: `);
                    console.log(pdf);
                    window.PDFViewerApplication.load(pdf)
                })
            }
            if (type == 'add-blank-page') {
                const {
                    PDFDocument
                } = window.PDFLib;
                let u8 = await save();
                const pdfDoc = await PDFDocument.load(u8, {
                    updateMetadata: false,
                });

                const lastPage = pdfDoc.getPage(pdfDoc.getPageCount() - 1) || pdfDoc.addPage();
                const newPage = pdfDoc.addPage();
                newPage.setSize(lastPage.getWidth(), lastPage.getHeight());
                // Perform operations on the page, if needed

                const pdfBytes = await pdfDoc.save();
                let binaryString = new Uint8Array(pdfBytes).reduce((data, byte) => data + String.fromCharCode(byte), '');
                let b64encoded = btoa(binaryString);
                const pdfDataUri = 'data:application/pdf;base64,' + b64encoded;

                window.pdfjsLib.getDocument(pdfDataUri).promise.then(pdfDoc => {
                    // Now you can work with the PDF document
                    const numPages = pdfDoc.numPages;
                    console.log(`New number of pages: ${numPages}`);
                    window.PDFViewerApplication.load(pdfDoc);
                });
            }
        }


    var event = new CustomEvent('pdf-editor-finished', {detail: ""})
    window.parent.document.dispatchEvent(event)

    })();