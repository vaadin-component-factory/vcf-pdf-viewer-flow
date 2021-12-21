/*-
 * #%L
 * Timeline
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

@Tag("vcf-pdf-viewer")
@NpmPackage(value = "@vaadin-component-factory/vcf-pdf-viewer", version = "^1.0.0")
@JsModule("@vaadin-component-factory/vcf-pdf-viewer/vcf-pdf-viewer.js")
@CssImport(value = "./styles/download-button.css", themeFor = "vaadin-button")
public class PdfViewer extends Div {

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

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    addDownloadButton();
  }

  /**
   * Adds button to download pdf file currently on display.
   */
  private void addDownloadButton() {
    String src = this.getSrc(); 
    Anchor link = new Anchor(src, "");   
    Button downloadButton = new Button();
    downloadButton.getElement().appendChild(new Icon(VaadinIcon.DOWNLOAD).getElement());
    downloadButton.setThemeName("download-button");
    link.add(downloadButton);
    link.getElement().setAttribute("download", true);
    getElement().appendChild(link.getElement());
  }
}
