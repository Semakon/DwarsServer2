package com.semakon.dwarsserver2.model.database;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.rankings.Ranking;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class XMLdatabase implements Database {

    private static final String USERS_FILE = "src\\com\\semakon\\dwarsserver2\\data\\users.xml";

    private static XMLdatabase instance;

    private XMLdatabase() {
        // private to avoid instantiation
    }

    public static XMLdatabase getInstance() {
        if (instance == null) {
            instance = new XMLdatabase();
        }
        return instance;
    }

    @Override
    public void saveUsers(List<User> users) {

    }

    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try {
            // Build a document (root of XML tree)
            File file = new File(USERS_FILE);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Debug
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("user");

            // Loop over user nodes
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                // Determine the node is an element
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get user attributes
                    int id = Integer.parseInt(element.getAttribute("id"));
                    String name = element.getElementsByTagName("name")
                            .item(0)
                            .getTextContent();

                    // Add user to user list
                    User user = new User(id, name);
                    users.add(user);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void saveRankings(List<Ranking> rankings) {

    }

    @Override
    public List<Ranking> loadRankings() {
        return null;
    }

}
