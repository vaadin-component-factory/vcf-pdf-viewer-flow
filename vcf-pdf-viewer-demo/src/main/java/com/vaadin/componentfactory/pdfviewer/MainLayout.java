package com.vaadin.componentfactory.pdfviewer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

  public MainLayout() {
    final DrawerToggle drawerToggle = new DrawerToggle();

    final RouterLink basicExample = new RouterLink("Basic example", BasicPdfViewerExample.class);
    final RouterLink zoomExample = new RouterLink("Zoom example", AdjustZoomPdfViewerExample.class);
    final RouterLink thumbnailsOpenExample =
        new RouterLink("Thumbnail viewer open example", ThumbnailsViewerOpenExample.class);
    final RouterLink thumbnailsListenerExample =
        new RouterLink("Thumbnail listener example", ThumbnailsListenerExample.class);
    final RouterLink selectPageExample =
        new RouterLink("Set page example", SelectPagePdfViewerExample.class);
    final RouterLink customAutoFitZoomLabelsExample = new RouterLink(
        "Custom label for auto and page fit options", CustomAutoFitZoomLabelsExample.class);
    final RouterLink withoutDownloadExample =
        new RouterLink("Without download option", WithoutDownloadExample.class);
    final RouterLink customTitleExample =
        new RouterLink("Custom title", CustomTitleExample.class);
    final RouterLink withPrintOptionExample =
        new RouterLink("With print option", WithPrintOptionExample.class);

    final VerticalLayout menuLayout = new VerticalLayout(basicExample, zoomExample,
        thumbnailsOpenExample, thumbnailsListenerExample, selectPageExample,
        customAutoFitZoomLabelsExample, withoutDownloadExample, customTitleExample, withPrintOptionExample);
    addToDrawer(menuLayout);
    addToNavbar(drawerToggle);
  }

  @Override
  public void setContent(Component content) {
    super.setContent(content);
    content.getElement().getStyle().set("height", "100%");
    content.getElement().getStyle().set("overflow", "hidden");
    content.getElement().getStyle().set("display", "flex");
    content.getElement().getStyle().set("flex-direction", "column");
  }
}
