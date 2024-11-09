---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

<!-- Acknowledgements -->
## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

<!-- Setting up, getting started -->
## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* User is a event organiser / planner
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

Eventory is an application that allows you to plan events easily, whether you’re planning by yourself or collaborating with others. With Eventory, you’ll keep crucial details always at your fingertips and ensure that everyone is on the same page.

Here are some of the features of Eventory:

* **Add**, **Edit**, and **Delete** contacts and events
* **Find** contacts and events by name and tag
* **Link** contacts to events

By enhancing management and data processing, Eventory reduces stress and helps you execute events smoothly and efficiently.

### User stories
Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`
#### Important Note:
* Not all features in the user stories have been implemented
* For features that have been implemented, they may not work exactly as described in user stories
* Features are not implemented strictly based on priority

| Priority | As a …​                                          | I want to …​                                               | So that I can…​                                                            |
|----------|--------------------------------------------------|------------------------------------------------------------|----------------------------------------------------------------------------|
| `* * *`  | new user                                         | see a list of commands                                     | quickly use it as reference                                                |
| `* * *`  | meticulous planner                               | add, edit, and delete contacts                             | maintain only a list of essential contacts                                 |
| `* * *`  | efficiency-focused user                          | search by name                                             | save time in looking for specific contacts                                 |
| `* * *`  | frequent user                                    | save and load all my data                                  | Use the application across multiple sessions                               |
| `* *`    | easily overwhelmed planner                       | see priorities of work to be done                          | better manage my time                                                      |
| `* *`    | team member                                      | share contacts with others                                 | work with others more effectively                                          |
| `* *`    | frequent user                                    | import and export contacts                                 | migrate between working platforms                                          |
| `* *`    | detail-oriented planner                          | add custom notes to each contact                           | keep track of specific details                                             |
| `*`      | tech-savvy user                                  | use keyboard shortcuts                                     | achieve my goals more efficiently                                          |
| `*`      | impatient person                                 | easily use commands                                        | do my work quickly and without frustration                                 |
| `*`      | user with many clients                           | share my schedule                                          | share my availability with clients                                         |
| `*`      | bilingual user                                   | translate notes                                            | work in different languages with different clients                         |
| `*`      | event planner                                    | see who is in charge of a venue                            | quickly contact them for bookings                                          |
| `*`      | planning supervisor                              | set permissions for team members                           | so that we can collaborate at any level                                    |
| `*`      | planner of multiple events                       | separate contacts based on event                           | contact relevant people more quickly                                       |
| `*`      | event planner                                    | see contact's occupations                                  | know who may be relevant to my event                                       |
| `*`      | safety-conscious event organiser                 | store emergency contact details for team members           | quickly reach them in case of an emergency                                 |
| `*`      | large event organiser                            | send bulk messages to multiple contacts at once            | save time when sending updates or reminders                                |
| `*`      | organiser                                        | schedule messages in advance                               | send reminders to vendors                                                  |
| `*`      | busy planner                                     | receive delivery confirmations and read receipts           | be sure my contacts have received important information                    |
| `*`      | detailed planner                                 | assign tasks to individual contacts                        | know who is responsible for each task                                      |
| `*`      | event organiser                                  | track the history of events a contact has been involved in | have a reference of their past contributions                               |
| `*`      | organiser with many contacts                     | sort contact by their latest interaction or by tags        | quickly access the most relevant contacts                                  |
| `* `     | frequent event organiser                         | view available venues for hosting my event                 | save time searching for suitable venues                                    |
| `*`      | event planner                                    | view contacts on a map                                     | plan location based events                                                 |
| `*`      | large scale event planner                        | create relationship mappings between contacts              | understand and leverage connections within my network                      |
| `*`      | planner who does not check the application often | customise alerts and notifications                         | differentiate between notifications easily                                 |
| `*`      | long time event organiser                        | archive inactive contacts without deleting them            | maintain a record of past interactions while keeping my active list clean. |

### Use Cases

(For all use cases below, the **System** is the `Eventory` and the **Actor** is the `user`, unless specified otherwise)

---

#### **Use Case 1: Add a person or event**

**Main Success Scenario (MSS)**

1. User requests to add a person or event.
2. Eventory adds the person or event.

    *Use case ends.*

**Extensions**

- **2a.** The given format is invalid.
    - **2a1.** Eventory shows an error message.
      *Use case resumes at step 1.*

  *Use case ends.*

---

#### **Use Case 2: Edit a person or event**

**Main Success Scenario (MSS)**

1. User requests to list persons and events.
2. Eventory shows a list of persons and events.
3. User requests to edit a person or event.
4. Eventory edits the person or event.

   *Use case ends.*

**Extensions**

- **2a.** The list is empty.

  *Use case ends.*

- **3a.** The given index is invalid.
    - **3a1.** Eventory shows an error message.
      *Use case resumes at step 2.*

  *Use case ends.*

---

#### **Use Case 3: Delete a person or event**

**Main Success Scenario (MSS)**

1. User requests to list persons and events.
2. Eventory shows a list of persons and events.
3. User requests to delete a specific person or event in the list.
4. Eventory deletes the person or event.

   *Use case ends.*

**Extensions**

- **2a.** The list is empty.
  *Use case ends.*

- **3a.** The given index is invalid.
    - **3a1.** Eventory shows an error message.
      *Use case resumes at step 2.*

  *Use case ends.*

---

#### **Use Case 4: Find a person or event by name**

**Main Success Scenario (MSS)**

1. User requests to find a person or event by name.
2. Eventory returns a list of relevant people and events.

   *Use case ends.*

**Extensions**

- **2a.** The list is empty.

  *Use case ends.*

---

#### **Use Case 5: Find a person or event by tag**

**Main Success Scenario (MSS)**

1. User requests to find a person or event by tag.
2. Eventory returns a list of relevant people and events.

   *Use case ends.*

**Extensions**

- **2a.** The list is empty.

  *Use case ends.*

---

#### **Use Case 6: View events happening in the schedule**

**Main Success Scenario (MSS)**

1. User requests to see their schedule.
2. Eventory returns a list of events happening in a time period.

   *Use case ends.*

**Extensions**

- **2a.** The list is empty.

  *Use case ends.*

---

#### **Use Case 7: Link person to event**

**Main Success Scenario (MSS)**

1. User requests to link person to event.
2. Eventory does the linking.

   *Use case ends.*

**Extensions**

- **2a.** The given index is invalid.
    - **2a1.** Eventory shows an error message.
      *Use case resumes at step 1.*

- **2b.** The event does not exist.
    - **2b1.** Eventory shows an error message.
      *Use case resumes at step 1.*

  *Use case ends.*

---

#### **Use Case 8: Unlink person to event**

**Main Success Scenario (MSS)**

1. User requests to unlink person from event.
2. Eventory does the unlinking.

   *Use case ends.*

**Extensions**

- **2a.** The given index is invalid.
    - **2a1.** Eventory shows an error message.
      *Use case resumes at step 1.*

- **2b.** The event does not exist.
    - **2b1.** Eventory shows an error message.
      *Use case resumes at step 1.*

  *Use case ends.*

---

#### **Use Case 9: Clear Eventory**

**Main Success Scenario (MSS)**

1. User requests to clear Eventory contacts and events.
2. Eventory removes all contacts and events in memory.

    *Use case ends.*

---

#### **Use Case 10: List contacts and events**

**Main Success Scenario (MSS)**

1. User requests for list of contacts and events in Eventory.
2. Eventory shows all contacts and events.

    *Use case ends.*

**Extensions**

- **2a.** The list is empty.

    *Use case ends.*

---

#### **Use Case 11: Request for help**

**Main Success Scenario (MSS)**

1. User requests help in Eventory.
2. Eventory displays all commands.

   *Use case ends.*

---

#### **Use Case 12: Exit programme**

**Main Success Scenario (MSS)**

1. User exits the programme.
2. Eventory closes.

   *Use case ends.*

---

### Non-Functional Requirements

**1. Domain Rules**
- 1a. `Essential` The number of contacts in a single event should not exceed 1000, ensuring efficient management of contacts for each event.

**2. Technical Requirements**
- 2a. `Essential` Must be compatible with Java 17 or higher, ensuring that the application runs on modern environments.
- 2b. `Typical` The system should work on both 32-bit and 64-bit environments, making it accessible to a broader range of users.
- 2c. `Novel` The system must support running offline and sync data when the internet is available, allowing event planners to manage contacts without internet access.

**3. Performance Requirements**
- 3a. `Essential` The system should respond to user input within 2 seconds under normal load conditions, providing a fast and efficient user experience for frequent tasks.
- 3b. `Typical` Event-based contact searches should return results within 3 seconds for up to 1000 contacts, facilitating quick access to relevant information.
- 3c. `Typical` Bulk messages up to 500 contacts should be queued for delivery within 5 seconds, enabling event organizers to send updates quickly.

**4. Quality Requirements**
- 4a. `Essential` The system should be usable by event planners with no prior experience in using CLI applications, ensuring accessibility for all users.
- 4b. `Typical` All command-line options should have detailed help documentation accessible from within the application, providing guidance for users.
- 4c. `Novel` The interface must support fast-typing users, minimizing mouse interactions to enhance productivity.

**5. Project Scope**
- 5a. `Typical` The product is not required to handle the printing of physical contact lists, focusing instead on digital management.
- 5b. `Novel` Integration with third-party calendar apps (e.g. Google Calendar) is planned for future versions but is out of scope for this release, allowing for future enhancements.

**6. Others**
- 6a. `Essential` The system should avoid any discriminatory language or culturally sensitive imagery in user messages or templates, ensuring a respectful environment for all users.
- 6b. `Novel` The contact mapping feature should not use any personal data without user consent, ensuring compliance with privacy regulations like GDPR.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **MSS:** Also known as Main Success Story. It is the scenario that a user should abide by when using the programme
* **API:** Also known as Application Programming Interface. It is the set of rules that allow different software to communicate with each other.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
