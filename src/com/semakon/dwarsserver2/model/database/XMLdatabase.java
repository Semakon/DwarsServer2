package com.semakon.dwarsserver2.model.database;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.rankings.Ranking;
import com.semakon.dwarsserver2.model.rankings.RankingItem;
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
    private static final String RANKINGS_FILE = "src\\com\\semakon\\dwarsserver2\\data\\rankings.xml";

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
    public List<Ranking> loadRankings(List<User> users) {
        List<Ranking> rankings = new ArrayList<>();

        try {
            // Build a document (root of XML tree)
            File file = new File(RANKINGS_FILE);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("ranking");

            // Loop over ranking nodes
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);

                // Determine the node is an element
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get ranking attributes
                    int rid = Integer.parseInt(element.getAttribute("id"));
                    String name = element.getElementsByTagName("name")
                            .item(0)
                            .getTextContent();
                    String description = element.getElementsByTagName("description")
                            .item(0)
                            .getTextContent();
                    NodeList items = element.getElementsByTagName("item");

                    // Initialize ranking item list
                    List<RankingItem> rankingItems = new ArrayList<>();

                    // Loop over item nodes
                    for (int i = 0; i < items.getLength(); i++) {
                        Node iNode = items.item(i);

                        // Determine the node is an element
                        if (iNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element iElement = (Element) iNode;

                            // Get ranking item attributes
                            int iid = Integer.parseInt(iElement.getAttribute("id"));
                            int uid = Integer.parseInt(iElement.getElementsByTagName("user-id")
                                    .item(0)
                                    .getTextContent());
                            int points = Integer.parseInt(iElement.getElementsByTagName("points")
                                    .item(0)
                                    .getTextContent());

                            // Get User object from uid
                            User user = null;
                            for (User u : users) {
                                if (u.getId() == uid) {
                                    user = u;
                                    break;
                                }
                            }
                            if (user == null) {
                                System.err.println("Uid does not correspond with any user in user list");
                                // TODO: throw exception: uid does not correspond with any user in user list
                            }

                            // Create ranking item from attributes and add it to the ranking
                            RankingItem rItem = new RankingItem(iid, user, points);
                            rankingItems.add(rItem);
                        }
                    }

                    // Create ranking and put ranking items in it, finally add it to rankings list
                    Ranking ranking = new Ranking(rid, name, description);
                    ranking.setRankingItems(rankingItems);
                    rankings.add(ranking);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return rankings;
    }

}
