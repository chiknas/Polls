package com.chiknas.votingsystem.vaadin;

import com.chiknas.votingsystem.data.model.Poll;
import com.chiknas.votingsystem.data.services.PollService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * com.chiknas.votingsystem.vaadin.IndexView, created on 06/11/2019 13:48 <p>
 * @author NikolaosK
 */
@PageTitle("Polls")
@Route(value = "", layout = MainLayout.class)
public class IndexView extends VerticalLayout {

  private H1 title = new H1("Polls List");
  private ListBox<Poll> pollsList = new ListBox<>();

  @Autowired IndexView(PollService pollService) {
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    add(title);

    pollsList.setItems(pollService.getAllPolls());
    pollsList.setRenderer(new ComponentRenderer<>(item -> {
      HorizontalLayout horizontalLayout = new HorizontalLayout();
      horizontalLayout.add(new Label("(" + item.getAuthor() + ") " + item.getQuestion()));
      return horizontalLayout;
    }));

    pollsList.addValueChangeListener(item -> {
      UI.getCurrent().navigate(PollView.class, item.getValue().getId().toString());
    });
    add(pollsList);
  }
}
