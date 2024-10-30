package seedu.address.model.types.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.model.types.event.Event;
import seedu.address.model.types.event.exceptions.DuplicateEventException;
import seedu.address.model.types.event.exceptions.EventNotFoundException;
import seedu.address.model.types.person.Person;

/**
 * Manages the relationship between events and persons in the address book.
 * Provides methods to link persons to events, manage events, and query the event-person relationships.
 */
public class PersonEventManager {

    /** An observable map storing the relationship between events and the list of persons associated with each event. */
    private static ObservableMap<Event, ObservableList<Person>> eventPersonMap;

    /**
     * Initializes the eventPersonMap with events and persons from storage.
     * This method should be called during application startup.
     */
    public static void initialiseHashMap() {
        if (eventPersonMap == null) {
            eventPersonMap = FXCollections.observableHashMap();
            // TODO add events and persons from storage
        }
    }

    /* ============================== Person Methods ============================== */

    /**
     * Checks if a person is linked to a given event.
     *
     * @param person The person to check.
     * @param event The event to check.
     * @return true if the person is linked to the event, false otherwise.
     */
    public static boolean isPersonLinkedToEvent(Person person, Event event) {
        ObservableList<Person> persons = eventPersonMap.get(event);
        return persons != null && persons.contains(person);
    }

    /**
     * Adds a person to the specified event.
     *
     * @param person The person to add.
     * @param event The event to add the person to.
     * @throws EventNotFoundException if the event does not exist in the map.
     */
    public static void addPersonToEvent(Person person, Event event) {
        ObservableList<Person> persons = eventPersonMap.get(event);
        if (persons != null) {
            persons.add(person);
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Removes a person from the specified event.
     *
     * @param person The person to remove.
     * @param event The event to remove the person from.
     * @throws EventNotFoundException if the event does not exist in the map.
     */
    public static void removePersonFromEvent(Person person, Event event) {
        ObservableList<Person> persons = eventPersonMap.get(event);
        if (persons != null) {
            persons.remove(person);
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Removes a person from all events they are associated with.
     *
     * @param person The person to remove from all events.
     */
    public static void removePersonFromAllEvents(Person person) {
        eventPersonMap.values().forEach(persons -> persons.remove(person));
    }

    /**
     * Updates all events by replacing occurrences of the target person with the edited person.
     *
     * @param target The person to be replaced.
     * @param editedPerson The person to replace the target with.
     */
    public static void setPersonForAllEvents(Person target, Person editedPerson) {
        eventPersonMap.values().forEach(persons -> {
            if (persons.contains(target)) {
                persons.remove(target);
                persons.add(editedPerson);
            }
        });
    }

    /* ============================== Event Methods ============================== */

    /**
     * Checks if the given event exists in the map.
     *
     * @param event The event to check.
     * @return true if the event exists, false otherwise.
     */
    public static boolean hasEvent(Event event) {
        return eventPersonMap.containsKey(event);
    }

    /**
     * Adds an event to the map. Throws an exception if the event already exists.
     *
     * @param event The event to add.
     * @throws DuplicateEventException if the event already exists in the map.
     */
    public static void addEvent(Event event) {
        if (eventPersonMap.containsKey(event)) {
            throw new DuplicateEventException();
        }
        ObservableList<Person> personsList = FXCollections.observableArrayList();
        eventPersonMap.put(event, personsList);
    }

    /**
     * Removes an event from the map.
     *
     * @param event The event to remove.
     */
    public static void removeEvent(Event event) {
        eventPersonMap.remove(event);
    }

    /**
     * Replaces the target event with the edited event, retaining the persons linked to the target event.
     *
     * @param target The event to be replaced.
     * @param editedEvent The event to replace the target with.
     */
    public static void setEvent(Event target, Event editedEvent) {
        ObservableList<Person> persons = eventPersonMap.get(target);
        if (persons != null) {
            eventPersonMap.remove(target);
            eventPersonMap.put(editedEvent, persons);
        }
    }

    /**
     * Retrieves an event from the map by matching its name.
     *
     * @param target The event with the name to search for.
     * @return The event with the matching name, or null if not found.
     */
    public static Event getEvent(Event target) {
        return eventPersonMap.keySet().stream()
                .filter(event -> event.isSameEvent(target))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns an unmodifiable view of the eventPersonMap.
     *
     * @return an {@code ObservableMap<Event, ObservableList<Person>>}
     *         representing the relationship between events and persons.
     */
    public static ObservableMap<Event, ObservableList<Person>> getEventPersonMap() {
        return FXCollections.unmodifiableObservableMap(eventPersonMap);
    }
}
