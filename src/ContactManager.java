import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.Serializable;

class Contact implements Serializable {
    String name, phoneNumber, emailAddress;

    public Contact(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + emailAddress;
    }
}

public class ContactManager {
    private static final String FILE_NAME = "contacts.txt";
    private ArrayList<Contact> contacts;

    public ContactManager() {
        loadContactsFromFile();
    }

    private void loadContactsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            //noinspection unchecked
            contacts = (ArrayList<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            contacts = new ArrayList<>();
        }
    }

    private void saveContactsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addContact(String name, String phoneNumber, String emailAddress) {
        String PHONE_NUMBER_REGEX = "\\d{10}";
        String EMAIL_REGEX = "\\S+@\\S+\\.\\S+";
        String NAME_REGEX = "\\D+";

        if (!name.matches(NAME_REGEX) || !phoneNumber.matches(PHONE_NUMBER_REGEX) || !emailAddress.matches(EMAIL_REGEX)) {
            System.out.println("Invalid input. Please check your name, phone number, or email address.");
            return;
        }

        contacts.add(new Contact(name, phoneNumber, emailAddress));
        saveContactsToFile();
        System.out.println("Contact added successfully.");
    }

    public void viewContacts() {
        contacts.forEach(System.out::println);
        if (contacts.isEmpty()) System.out.println("No contacts found.");
    }

    public void editContact(int index, String name, String phoneNumber, String emailAddress) {
        if (index >= 0 && index < contacts.size()) {
            Contact contact = contacts.get(index);
            contact.name = name;
            contact.phoneNumber = phoneNumber;
            contact.emailAddress = emailAddress;
            saveContactsToFile();
            System.out.println("Contact edited successfully!");
        } else {
            System.out.println("Invalid contact index.");
        }
    }

    public void deleteContact(String nameToDelete) {
        Iterator<Contact> iterator = contacts.iterator();
        boolean contactFound = false;

        while (iterator.hasNext()) {
            Contact contact = iterator.next();
            if (contact.name.equalsIgnoreCase(nameToDelete)) {
                iterator.remove();
                saveContactsToFile();
                System.out.println("Contact deleted!");
                contactFound = true;
                break;
            }
        }

        if (!contactFound) {
            System.out.println("Contact not found with the specified name.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactManager contactManager = new ContactManager();

        while (true) {
            System.out.println("\nContact Manager Menu:\n1. Add a new contact\n2. View contacts\n3. Edit a contact\n4. Delete a contact\n5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter phone number: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Enter email address: ");
                    String emailAddress = scanner.nextLine();
                    contactManager.addContact(name, phoneNumber, emailAddress);
                }
                case 2 -> contactManager.viewContacts();
                case 3 -> {
                    System.out.print("Enter the index of the contact to edit: ");
                    int editIndex = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new phone number: ");
                    String newPhoneNumber = scanner.nextLine();
                    System.out.print("Enter new email address: ");
                    String newEmailAddress = scanner.nextLine();
                    contactManager.editContact(editIndex, newName, newPhoneNumber, newEmailAddress);
                }
                case 4 -> {
                    System.out.print("Enter the name of the contact to delete: ");
                    String nameToDelete = scanner.nextLine();
                    contactManager.deleteContact(nameToDelete);
                }
                case 5 -> {
                    System.out.println("Exiting Contact Manager. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
}
