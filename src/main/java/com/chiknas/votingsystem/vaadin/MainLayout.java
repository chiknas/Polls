package com.chiknas.votingsystem.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * com.chiknas.votingsystem.vaadin.MainLayout, created on 06/11/2019 09:44 <p>
 * @author NikolaosK
 */
@PageTitle("Polls")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@StyleSheet("frontend://css/MainLayout.css")
public class MainLayout extends VerticalLayout implements RouterLayout, PageConfigurator {

  MainLayout() {
    setSizeFull();
    setPadding(false);
    setMargin(false);
    setAlignItems(Alignment.CENTER);

    Image logo = new Image("img/logo.png", "Polls");
    logo.setHeightFull();
    Button homeButton = new Button("Home");
    homeButton.addClickListener(event -> UI.getCurrent().navigate(IndexView.class));
    HorizontalLayout topBar = new HorizontalLayout();
    topBar.setClassName("top-bar");
    topBar.add(logo, homeButton);
    topBar.setDefaultVerticalComponentAlignment(Alignment.CENTER);

    Button newPollButton = new Button("Create Poll");
    newPollButton.addClickListener(event -> UI.getCurrent().navigate(CreatePollView.class));
    topBar.add(newPollButton);
    topBar.setMargin(false);

    add(topBar);

  }

  @Override
  public void configurePage(InitialPageSettings settings) {
    settings.addFavIcon("icon", "img/logo.png", "192x192");
  }
}
