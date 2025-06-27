# Pdf Viewer component for Vaadin Flow

This is the server-side component of [&lt;vcf-pdf-viewer&gt; Web Component](https://github.com/vaadin-component-factory/vcf-pdf-viewer). The component uses [PDF.js](https://github.com/mozilla/pdf.js) library to display pdf files.

This component is part of Vaadin Component Factory.

## Description 

Pdf Viewer component provides support to the following features:

- Display a pdf file.
- Display a thumbnail's viewer.
- Set zoom.
- Navigate to a certain page.
- Open or close thumbnail's viewer.
- Add a listener on change page when clicking on thumbnail.
- Download the pdf file.

From version 2.5.x:

- Printing the pdf file (implemented with [Print.js](https://www.npmjs.com/package/print-js)).

## Development instructions

- Build the project and install the add-on locally:
```
mvn clean install
```
- For starting the demo server go to pdf-viewer-demo and run:
```
mvn jetty:run
```
This deploys demo at http://localhost:8080

## How to use it - Example

```java
PdfViewer pdfViewer = new PdfViewer();
StreamResource resource = new StreamResource("example.pdf", () -> getClass().getResourceAsStream("/pdf/example.pdf"));
pdfViewer.setSrc(resource);
pdfViewer.openThumbnailsView();
add(pdfViewer);    
```

#### Since version 5.0.0 - DownloadHandler support
```java
PdfViewer pdfViewer = new PdfViewer();
pdfViewer.setSrc(DownloadHandler.forClassResource(getClass(), "/pdf/example.pdf"));
add(pdfViewer);       
```

## Missing features or bugs

You can report any issue or missing feature on [GitHub](https://github.com/vaadin-component-factory/vcf-pdf-viewer/issues).

## License

Apache License 2.0.
