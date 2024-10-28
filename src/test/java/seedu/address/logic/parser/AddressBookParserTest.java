package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.personcommands.AddPersonCommand;
import seedu.address.logic.commands.personcommands.DeletePersonCommand;
import seedu.address.logic.commands.personcommands.EditPersonCommand;
import seedu.address.logic.commands.personcommands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.logic.commands.personcommands.ExitCommand;
import seedu.address.logic.commands.personcommands.FindPersonCommand;
import seedu.address.logic.commands.personcommands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.types.common.NameContainsKeywordsPredicate;
import seedu.address.model.types.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        ArrayList<Command> commands = parser.parseCommand(PersonUtil.getAddCommand(person));
        AddPersonCommand command = (AddPersonCommand) commands.get(0);
        assertEquals(new AddPersonCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        ArrayList<Command> commands1 = parser.parseCommand(ClearCommand.COMMAND_WORD);
        ArrayList<Command> commands2 = parser.parseCommand(ClearCommand.COMMAND_WORD + " 3");
        assertTrue(commands1.get(0) instanceof ClearCommand);
        assertTrue(commands2.get(0) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        ArrayList<Command> commands = parser.parseCommand(
                DeletePersonCommand.COMMAND_WORD + " p " + INDEX_FIRST_PERSON.getOneBased());
        DeletePersonCommand command = (DeletePersonCommand) commands.get(0);
        assertEquals(new DeletePersonCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        ArrayList<Command> commands = parser.parseCommand(EditPersonCommand.COMMAND_WORD + " p "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        EditPersonCommand command = (EditPersonCommand) commands.get(0);
        assertEquals(new EditPersonCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        ArrayList<Command> commands1 = parser.parseCommand(ExitCommand.COMMAND_WORD);
        ArrayList<Command> commands2 = parser.parseCommand(ExitCommand.COMMAND_WORD + " 3");
        assertTrue(commands1.get(0) instanceof ExitCommand);
        assertTrue(commands2.get(0) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        ArrayList<Command> commands = parser.parseCommand(
                FindPersonCommand.COMMAND_WORD + " p " + keywords.stream().collect(Collectors.joining(" ")));
        FindPersonCommand command = (FindPersonCommand) commands.get(0);
        assertEquals(new FindPersonCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        ArrayList<Command> commands1 = parser.parseCommand(HelpCommand.COMMAND_WORD);
        ArrayList<Command> commands2 = parser.parseCommand(HelpCommand.COMMAND_WORD + " 3");
        assertTrue(commands1.get(0) instanceof HelpCommand);
        assertTrue(commands2.get(0) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        ArrayList<Command> commands1 = parser.parseCommand(ListCommand.COMMAND_WORD);
        ArrayList<Command> commands2 = parser.parseCommand(ListCommand.COMMAND_WORD + " 3");
        assertTrue(commands1.get(0) instanceof ListCommand);
        assertTrue(commands2.get(0) instanceof ListCommand);
    }


    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
