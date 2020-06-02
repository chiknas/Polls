package com.chiknas.votingsystem.vaadin;

import com.chiknas.votingsystem.data.model.Poll;
import com.chiknas.votingsystem.data.services.PollService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;


/**
 * com.chiknas.votingsystem.vaadin.CreatePollView, created on 06/11/2019 11:20 <p>
 * @author NikolaosK
 */
@PageTitle("Polls")
@Route(value = "create", layout = MainLayout.class)
public class CreatePollView extends VerticalLayout {

  private final PollService pollService;
  private final VerticalLayout main = new VerticalLayout();
  private final List<TextField> inputList = new ArrayList<>();
  private final TextField author = new TextField("Your name(author):");
  private final TextField question = new TextField("Poll Question:");
  private final Button submit = new Button("Submit");
  private final Button addNewAnswer = new Button("Add Extra Answer");
  private final Checkbox isAnonymous = new Checkbox("Anonymous");

  CreatePollView(PollService pollService) {
    this.pollService = pollService;
    setAlignItems(Alignment.CENTER);
    setSizeFull();
    main.setAlignItems(Alignment.CENTER);
    main.setPadding(false);
    main.setWidth("50%");

    //add a question textfield
    question.setWidth("50%");
    add(author, question);

    //add answer textfields
    main.add(getAnswerRow());

    add(main, getButtonRow());
  }

  private HorizontalLayout getButtonRow() {
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    submit.addClickListener(event -> submitPoll());
    addNewAnswer.addClickListener(event -> main.add(getAnswerRow()));
    horizontalLayout.add(submit, addNewAnswer, isAnonymous);
    return horizontalLayout;
  }

  private HorizontalLayout getAnswerRow() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();
    layout.add(getAnswerTextField());
    layout.add(getAnswerTextField());
    layout.add(getAnswerTextField());
    layout.add(getAnswerTextField());
    return layout;
  }

  private TextField getAnswerTextField() {
    TextField textField = new TextField("Answer:");
    textField.setWidth("50%");
    inputList.add(textField);
    return textField;
  }

  private void submitPoll() {
    Poll poll = new Poll();
    poll.setQuestion(question.getValue());
    poll.setIsAnonymous(isAnonymous.getValue());
    poll.setAuthor(author.getValue());
    inputList.forEach(item -> {
      String value = item.getValue();
      if (!value.isEmpty()) {
        poll.addAnswer(value);
      }
    });
    Poll newPoll = pollService.save(poll);
    UI.getCurrent().navigate(PollView.class, newPoll.getId().toString());
  }
}
