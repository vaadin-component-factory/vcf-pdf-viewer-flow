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

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.AbstractStreamResource;

@Tag("vcf-pdf-viewer")
//@NpmPackage(value = "pdfjs-dist", version = "^2.10.377")
@JsModule("./vcf-pdf-viewer.js")
public class PdfViewer extends Div {

  public PdfViewer() {

  }

  /**
   * @return url to source file
   */
  public String getSrc() {
    return getElement().getAttribute("src");
  }
  
  public void setSrc(String src) {
    this.getElement().setAttribute("src", src);
  }

  /**
   * Use this method to give in a pdf file as a StreamResource. This is handy when you for example
   * want to load a PDF from a database or have it freshly generated with a library
   *
   * How to use: {@code StreamResource resource = new StreamResource("mypdf.pdf", ()
   * -&gt; getPdfInputStream("mypdf.pdf");}
   *
   * @param src stream to file
   */
  public void setSrc(AbstractStreamResource src) {
    getElement().setAttribute("src", src);
  }

  public void setPage(Integer pageNumber) {
    this.getElement().executeJs("pageNumber", pageNumber);
  }

  /**
   * @return current zoom level
   */
  public String getZoom() {
    return getElement().getProperty("zoom");
  }

  /**
   * The level of zoom on the document. Allowed values are - Number, for zoom percentage. Eg. 1.5
   * means 150% zoom - 'auto', default value - 'page-fit', fit a full page into component
   *
   * @param zoom zoom level
   */
  public void setZoom(String zoom) {
    getElement().setProperty("zoom", zoom);
  }


}
