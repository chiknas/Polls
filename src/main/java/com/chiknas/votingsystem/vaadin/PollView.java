package com.chiknas.votingsystem.vaadin;

import com.chiknas.votingsystem.data.model.Answer;
import com.chiknas.votingsystem.data.model.Poll;
import com.chiknas.votingsystem.data.services.PollService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * com.chiknas.votingsystem.vaadin.MainView, created on 06/11/2019 09:46 <p>
 * @author NikolaosK
 */
@PageTitle("Polls")
@Route(value = "poll", layout = MainLayout.class)
public class PollView extends VerticalLayout implements HasUrlParameter<String> {

  private final PollService pollService;
  private final Label questionLabel = new Label();
  private final ListBox<Answer> answersList = new ListBox<>();
  private final Anchor getAllVotersButton = new Anchor();
  private final Button deleteButton = new Button("Delete Poll");
  private final Button chartButton = new Button("Chart");

  PollView(PollService pollService) {
    this.pollService = pollService;
    setAlignItems(Alignment.CENTER);
    setSizeFull();

    answersList.setRenderer(new ComponentRenderer<>(item -> {
      VerticalLayout verticalLayout = new VerticalLayout();
      verticalLayout.add(new Label(item.getTitle() + " (" + item.getSelectedByUsers().size() + " votes)"));
      return verticalLayout;
    }));

    add(questionLabel, answersList, new HorizontalLayout(getAllVotersButton, deleteButton, chartButton));
  }

  private Dialog chartDialog(Poll poll) {
    Chart chart = new Chart(ChartType.COLUMN);
    Configuration conf = chart.getConfiguration();
    poll.getAnswers().forEach(answer -> {
      ListSeries series = new ListSeries(answer.getTitle());
      series.addData(answer.getSelectedByUsers().size());
      conf.addSeries(series);
    });

    Dialog dialog = new Dialog(chart);
    dialog.setHeight("calc(50vh - (2*var(--lumo-space-m)))");
    dialog.setWidth("calc(50vw - (4*var(--lumo-space-m)))");
    return dialog;
  }

  /**
   * Confirmation dialog before submitting a vote.
   */
  private Dialog confirmationDialog(Answer answer) {
    Dialog confirmationDialog = new Dialog();
    Label label = new Label("Are you sure you want to submit " + answer.getTitle());
    TextField name = new TextField("Please add your name:");
    Button confirm = new Button("Confirm");
    Button cancel = new Button("Cancel");
    confirm.addClickListener(event -> {
      vote(answer, name.getValue());
      confirmationDialog.close();
    });
    cancel.addClickListener(event -> {
      if (confirmationDialog.isOpened()) {
        confirmationDialog.close();
      }
    });
    confirmationDialog.add(new VerticalLayout(label, name, new HorizontalLayout(confirm, cancel)));
    return confirmationDialog;
  }

  /**
   * URL parameter handler. Runs before the initialization of the class.
   */
  @Override
  public void setParameter(BeforeEvent event, String parameter) {
    Poll pollFromId = pollService.getPollFromId(Long.parseLong(parameter));
    if (pollFromId != null) {
      if (pollFromId.isAnonymous()) {
        getAllVotersButton.setVisible(false);
      }
      questionLabel.add(new H1(pollFromId.getAuthor() + " asks: " + pollFromId.getQuestion()));
      answersList.setItems(pollFromId.getAnswers());

      getAllVotersButton.setHref(new StreamResource("poll_voters.txt", () -> getVotersAnswersStream(pollFromId)));
      getAllVotersButton.getElement().setAttribute("download", true);
      getAllVotersButton.add(new Button("Download Voters"));
    } else {
      chartButton.setVisible(false);
      getAllVotersButton.setVisible(false);
      deleteButton.setVisible(false);
      getAllVotersButton.setVisible(false);
      questionLabel.setText("Question was not found!");
    }

    deleteButton.addClickListener(item -> {
      pollService.deleteById(Objects.requireNonNull(pollFromId).getId());
      UI.getCurrent().navigate(IndexView.class);
    });

    answersList.addValueChangeListener(item -> {
      if (!pollFromId.isAnonymous()) {
        confirmationDialog(item.getValue()).open();
      } else {
        vote(item.getValue());
      }
    });

    chartButton.addClickListener(item -> {
      chartDialog(pollFromId).open();
    });
  }

  private void vote(Answer answer) {
    vote(answer, null);
  }

  private void vote(Answer answer, String name) {
    if (answer.addUser(answer.getPoll().isAnonymous() ? RandomStringUtils.randomAlphanumeric(10) : name)) {
      pollService.save(answer.getPoll());
      buildNotification("Thank you for voting! Back to work now!").open();
      answersList.getDataProvider().refreshItem(answer);
    } else {
      buildNotification("Hey! You have already voted!").open();
    }
  }

  /**
   * Generates a notification for the specified text
   */
  private Notification buildNotification(String text) {
    Notification notification = new Notification(text);
    notification.setDuration(1500);
    notification.setPosition(Notification.Position.MIDDLE);
    return notification;
  }

  /**
   * Stream to produce a file with all voters of the specified poll.
   */
  private InputStream getVotersAnswersStream(Poll poll) {
    StringBuilder sb = new StringBuilder();
    sb.append("(").append(poll.getQuestion()).append(") ").append("voters");
    sb.append(System.getProperty("line.separator"));
    sb.append("winning answer so far is: ").append(pollService.getWinningAnswer(Objects.requireNonNull(poll).getId()));
    sb.append(System.getProperty("line.separator"));
    List<String> voters = pollService.getVoters(Objects.requireNonNull(poll).getId());
    voters.forEach(voter -> {
      sb.append(voter);
      sb.append(System.getProperty("line.separator"));
    });
    return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
  }
}
