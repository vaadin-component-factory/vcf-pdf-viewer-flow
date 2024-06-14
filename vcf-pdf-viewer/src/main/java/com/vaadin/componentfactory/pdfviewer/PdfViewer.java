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

import com.vaadin.componentfactory.pdfviewer.event.ThumbnailClickedEvent;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.AbstractStreamResource;
import org.apache.commons.lang3.StringUtils;

@Tag("vcf-pdf-viewer")
@NpmPackage(value = "@vaadin-component-factory/vcf-pdf-viewer", version = "2.1.0")
@NpmPackage(value = "print-js", version = "1.6.0")
@JsModule("@vaadin-component-factory/vcf-pdf-viewer/vcf-pdf-viewer.js")
@JsModule("./src/pdf-print.js")
@CssImport(value = "./styles/toolbar-button.css", themeFor = "vaadin-button")
@CssImport("print-js/dist/print.css")
public class PdfViewer extends Div {

  private Button downloadButton;
	
  /* Indicates if download button is added or not */
  private boolean addDownloadButton = true;
  private Anchor downloadLink;

  /* Indicates if print button is added to toolbar or not */
  private boolean addPrintButton = false;
  private Button printButton;
  
  private String printButtonTooltipText;
  private String downloadButtonTooltipText;
  
  public PdfViewer() {}

  /**
   * @return url to source file
   */
  public String getSrc() {
    return getElement().getAttribute("src");
  }

  /**
   * Sets a pdf file to render.
   * 
   * @param src url to file
   */
  public void setSrc(String src) {
    this.getElement().setAttribute("src", src);
    updateDownloadSource();
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
    getElement().setAttribute("src", src);
    updateDownloadSource();
  }

  /**
   * @return current zoom level
   */
  public String getZoom() {
    return getElement().getProperty("zoom");
  }

  /**
   * Sets the level of zoom on the rendered pdf file.
   * <p>
   * Allowed values are - Number: for zoom percentage (e.g. 1.5 means 150% zoom). - 'auto': default
   * value. - 'page-fit': fit a full page into component.
   * 
   * @param zoom zoom level
   */
  public void setZoom(String zoom) {
    getElement().setProperty("zoom", zoom);
  }

  /**
   * Adds a listener for {@link ThumbnailClickedEvent} to the component.
   *
   * @param listener the listener to be added
   */
  public void addThumbnailClickedListener(ComponentEventListener<ThumbnailClickedEvent> listener) {
    addListener(ThumbnailClickedEvent.class, listener);
  }

  /**
   * Opens thumbnails view.
   */
  public void openThumbnailsView() {
    getElement().executeJs("this.__openSidebar()");
  }

  /**
   * Closes thumbnails view.
   */
  public void closeThumbnailsView() {
    getElement().executeJs("this.__closeSidebar()");
  }

  /**
   * @return current page on viewer
   */
  @Synchronize("currentPage-changed")
  public String getCurrentPage() {
    return getElement().getProperty("currentPage");
  }

  /**
   * Sets the current page of the rendered pdf file.
   * 
   * @param currentPage current page number
   */
  public void setPage(Integer currentPage) {
    getElement().executeJs("this.setCurrentPage($0)", currentPage);
  }

  /**
   * Indicates if toolbar should only show filename as title or default title.
   * By default is set to false, so default title will be display if not specified otherwise.
   * 
   * <p>Conditions:</p> 
   * <ul>
   * <li>Pdf file should have a filename.</li>
   * <li>This flag should be set on pdf viewer initialization time. It cannot be updated dynamically.</li>
   * </ul>
   * 
   * @param filenameOnly if true, toolbar only shows filename as title 
   */
  public void showFilenameOnly(boolean filenameOnly) {
    getElement().setProperty("toolbarOnlyFilename", filenameOnly);
  }

  /**
   * <p>Sets the auto zoom option label.</p>
   * <p>This property should be set on pdf viewer initialization time. It cannot be
   * updated dynamically.</p>
   * 
   * @param autoZoomOptionLabel auto zoom option label
   */
  public void setAutoZoomOptionLabel(String autoZoomOptionLabel) {
    getElement().setProperty("autoZoomOptionLabel", autoZoomOptionLabel);
  }

  /**
   * <p>Sets the page fit zoom option label.</p>
   * <p>This property should be set on pdf viewer initialization time. It cannot be
   * updated dynamically.</p>
   * 
   * @param fitZoomOptionLabel page fit zoom option label
   */
  public void setPageFitZoomOptionLabel(String fitZoomOptionLabel) {
    getElement().setProperty("fitZoomOptionLabel", fitZoomOptionLabel);
  }
  
  /**
   * Returns whether download button is added to the toolbar or not.
   * 
   * @return true if download button is added
   */
  public boolean isAddDownloadButton() {
    return addDownloadButton;
  }

  /**
   * <p>Sets the flag to indicate if download button should be added or not. 
   * By default the flag is set to true, so download button is always added to the toolbar. </p>
   * <p>This flag should be set on pdf viewer initialization time. It cannot be updated dynamically.</p>
   * 
   * @param addDownloadButton true if download button should be added
   */
  public void setAddDownloadButton(boolean addDownloadButton) {
    this.addDownloadButton = addDownloadButton;
  }

  /**
   * @return custom title 
   */
  public String getCustomTitle() {
    return getElement().getProperty("customTitle");
  }

  /**
   * <p>Sets a custom title for the viewer. If this value is set, it will ignore the filename and the
   * metadata title of the pdf file.</p>
   * <p>This value should be set on pdf viewer initialization time. It cannot be updated dynamically.</p>
   * 
   * @param customTitle
   */
  public void setCustomTitle(String customTitle) {
    getElement().setProperty("customTitle", customTitle);
  }

   /**
   * Returns whether print button is added to the toolbar or not.
   * 
   * @return true if print button is added to toolbar
   */
  public boolean isAddPrintButton() {
    return addPrintButton;
  }

  /**
   * <p>Sets the flag to indicate if print button should be added to toolbar or not. 
   * By default the flag is set to false, so, by default print button is not added to the toolbar. </p>
   * <p>This flag should be set on pdf viewer initialization time. It cannot be updated dynamically.</p>
   * 
   * @param addPrintButton true if print button should be added to toolbar
   */
  public void setAddPrintButton(boolean addPrintButton) {
    this.addPrintButton = addPrintButton;
  }

  /**
   * <p>Sets the flag to indicate how to render interactive forms. 
   * Renders interactive form elements in the annotation layer (html) if true,
   * renders values of form elements directly onto the canvas if false.
   * By default the flag is set to true. </p>
   * <p>This flag should be set on pdf viewer initialization time. It cannot be updated dynamically.</p>  
   * 
   * @param renderInteractiveForms 
   */
  public void setRenderInteractiveForms(boolean renderInteractiveForms) {
    this.getElement().setProperty("renderInteractiveForms", renderInteractiveForms);
  }	
  
  /**
   * <p>Sets the flag to indicate if zoom options dropdown should be visible or not. 
   * By default the flag is set to false, so, the dropdown is always shown. </p>
   * <p>This flag should be set on pdf viewer initialization time. It cannot be updated dynamically.</p>
   * 
   * @param hideZoom true to hide the zoom dropdown
   */
  public void hideZoom(boolean hideZoom) {
	  this.getElement().setProperty("hideZoom", hideZoom);
  }
  
  /**
   * Sets a tooltip text for the previous page button in toolbar.
   * 
   * @param tooltipText text to show in tooltip
   */
  public void setPreviousPageTooltipText(String previousPageTooltipText) {
    this.getElement().setProperty("previousPageTooltip", previousPageTooltipText);
  }
  
  /**
   * Returns the tooltip text defined for the previous page button.
   * 
   * @return the previous page button tooltip text
   */
  public String getPreviousPageTooltipText() {
    return this.getElement().getProperty("previousPageTooltip");
  }
  
  /**
   * Sets tooltip text for the next page button in toolbar.
   * 
   * @param tooltipText text to show in tooltip
   */
  public void setNextPageTooltipText(String nextPageTooltipText) {
    this.getElement().setProperty("nextPageTooltip", nextPageTooltipText);
  }
  
  /**
   * Returns the tooltip text defined for the next page button.
   * 
   * @return the next page button tooltip text
   */
  public String getNextPageTooltipText() {
    return this.getElement().getProperty("nextPageTooltip");
  }
  
  /**
   * Sets tooltip text for the sidebar toogle button in toolbar.
   * 
   * @param tooltipText text to show in tooltip
   */
  public void setSidebarToggleTooltipText(String sidebarToggleTooltipText) {
    this.getElement().setProperty("sidebarToggleTooltip", sidebarToggleTooltipText);
  }
  
  /**
   * Returns the tooltip text defined for the sidebar toggle button.
   * 
   * @return the sidebar toggle button tooltip text
   */
  public String getSidebarToggleTooltipText() {
    return this.getElement().getProperty("sidebarToggleTooltip");
  } 
  
  /**
   * Sets tooltip text for the print button in toolbar.
   * 
   * @param printButtonTooltipText the printButtonTooltipText to set
   */ 
  public void setPrintButtonTooltipText(String printButtonTooltipText) {
    this.printButtonTooltipText = printButtonTooltipText;
  }
  
  /**
   * Returns the tooltip text defined for the print button.
   * 
   * @return the print button tooltip text
   */
  public String getPrintButtonTooltipText() {
    return printButtonTooltipText;
  }  

  /**
   * Sets tooltip text for the download button in toolbar.
   * 
   * @param downloadButtonTooltipText the downloadButtonTooltipText to set
   */
  public void setDownloadButtonTooltipText(String downloadButtonTooltipText) {
    this.downloadButtonTooltipText = downloadButtonTooltipText;
  }

  /**
   * Returns the tooltip text defined for the download button.
   * 
   * @return the download button tooltip text
   */
  public String getDownloadButtonTooltipText() {
    return downloadButtonTooltipText;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    if(addDownloadButton) {
      addDownloadButton();
    }
    if(addPrintButton){
      addPrintButton();
    }
  }
  
  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
    if(addDownloadButton) {
      this.getElement().removeChild(downloadLink.getElement());
    }
    if(addPrintButton){
      this.getElement().removeChild(printButton.getElement());
    }
  }

  /**
   * Adds button to download pdf file currently on display.
   */
  private void addDownloadButton() {
    String src = this.getSrc();
    if(StringUtils.isNotBlank(src)) {
    	downloadLink = new Anchor(src, "");
    } else {
    	downloadLink = new Anchor();
    	downloadLink.setText("");
    }    
    downloadLink.setTabIndex(-1);
    downloadButton = new Button();
    downloadButton.getElement().appendChild(new Icon(VaadinIcon.DOWNLOAD).getElement());
    downloadButton.getElement().setAttribute("aria-label", "Download file");
    downloadButton.setThemeName("download-button");
    downloadButton.setEnabled(StringUtils.isNotBlank(src));
    downloadButton.setTooltipText(this.getDownloadButtonTooltipText());
    downloadLink.add(downloadButton);
    downloadLink.getElement().setAttribute("download", true);
    getElement().appendChild(downloadLink.getElement());
  }

  /**
   * Update the href for the download link based on the current pdf src.
   */
  private void updateDownloadSource() {
    if (downloadLink != null) {
      String src = this.getSrc();
      downloadLink.setHref(src);
      downloadButton.setEnabled(StringUtils.isNotBlank(src));
    }
  }

  /**
   * Adds button to print the pdf file that's on display in the viewer.
   */
  private void addPrintButton() {
    printButton = new Button(new Icon(VaadinIcon.PRINT));
    printButton.getElement().setAttribute("aria-label", "Print file");
    printButton.setThemeName("print-button");
    getElement().appendChild(printButton.getElement());
    printButton.addClickListener(e -> this.getElement().executeJs("printPdf.printPdf($0)", this.getSrc()));
    printButton.setTooltipText(this.getPrintButtonTooltipText());
  }

}
