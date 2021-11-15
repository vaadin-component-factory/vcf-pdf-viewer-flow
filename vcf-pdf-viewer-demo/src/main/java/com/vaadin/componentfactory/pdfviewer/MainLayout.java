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

    final VerticalLayout menuLayout = new VerticalLayout(basicExample, zoomExample);
    addToDrawer(menuLayout);
    addToNavbar(drawerToggle);
  }
}
