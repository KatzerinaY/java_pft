package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

    @Parameter(names = "-c",description = "Group count")
    public int count;
    @Parameter(names = "-f",description = "Target file")
    public String file;

    public static void main(String[] args) throws IOException {
        ContactDataGenerator generator = new ContactDataGenerator();
        JCommander jCommander = new  JCommander(generator);
        try {
            jCommander.parse(args);
        }catch (ParameterException ex){
            jCommander.usage();
            return;
        }
        generator.run();
    }

    private void run() throws IOException {
        List<ContactData> contacts = generateContacts(count);
        String ext = Files.getFileExtension(file);
        if (ext.equals("json")){
            saveJson(contacts,new File(file));
        } else {
            System.out.println("Unrecognized format");
        }

    }

    private void saveJson(List<ContactData> contacts, File file) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()  // nice format
                .excludeFieldsWithoutExposeAnnotation() // only fields with @Expose
                .create();

        String json = gson.toJson(contacts);

        Writer writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    private List<ContactData> generateContacts(int count) {
        List<ContactData>contacts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            contacts.add(new ContactData().withFirstname(String.format("Firstname %s",i))
                    .withLastname(String.format("Lastname %s",i)).withAddress(String.format("Address %s",i))
                    .withPhoneHome(String.format("+222333%s",i)).withEmail(String.format("jm%sSmith@m.ru",i)));
        }
        return contacts;
    }
}
