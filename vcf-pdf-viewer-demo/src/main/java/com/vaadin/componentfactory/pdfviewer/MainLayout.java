package com.vaadin.componentfactory.pdfviewer;

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

    final VerticalLayout menuLayout =
        new VerticalLayout(basicExample, zoomExample, thumbnailsOpenExample, thumbnailsListenerExample);
    addToDrawer(menuLayout);
    addToNavbar(drawerToggle);
  }
}
