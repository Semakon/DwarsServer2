package com.semakon.dwarsserver2.model.database;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.rankings.Ranking;
import com.semakon.dwarsserver2.model.rankings.RankingItem;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class XMLdatabase implements Database {

    // TODO: Use update system instead of complete overwrite for efficiency

    /** XML file paths */
    private static final String USERS_FILE = "src\\com\\semakon\\dwarsserver2\\data\\users.xml";
    private static final String RANKINGS_FILE = "src\\com\\semakon\\dwarsserver2\\data\\rankings.xml";

    /** XML tag names */
    private static final String USERS_TAG = "users";
    private static final String USER_TAG = "user";
    private static final String USER_ID_ATTR = "id";
    private static final String USER_NAME_TAG = "name";

    private static final String RANKINGS_TAG = "rankings";
    private static final String RANKING_TAG = "ranking";
    private static final String RANKING_ID_ATTR = "id";
    private static final String RANKING_NAME_TAG = "name";
    private static final String RANKING_DESC_TAG = "description";
    private static final String RANKING_ITEM_TAG = "item";
    private static final String RANKING_ITEM_ID_ATTR = "id";
    private static final String RANKING_ITEM_USER_ID_TAG = "user-id";
    private static final String RANKING_ITEM_POINTS_TAG = "points";

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
        try {
            // Build document (root of XML tree)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Root element
            Element rootElement = doc.createElement(USERS_TAG);
            doc.appendChild(rootElement);

            // User elements
            for (User u : users) {
                Element userElement = doc.createElement(USER_TAG);
                rootElement.appendChild(userElement);

                // Set id attribute for user element
                Attr attr = doc.createAttribute(USER_ID_ATTR);
                attr.setValue(Integer.toString(u.getId()));
                userElement.setAttributeNode(attr);

                // Name element as child of user element
                Element userName = doc.createElement(USER_NAME_TAG);
                userName.appendChild(doc.createTextNode(u.getName()));
                userElement.appendChild(userName);
            }

            // Write content into XML file
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(USERS_FILE));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
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

            NodeList nList = doc.getElementsByTagName(USER_TAG);

            // Loop over user nodes
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                // Determine the node is an element
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get user attributes
                    int id = Integer.parseInt(element.getAttribute(USER_ID_ATTR));
                    String name = element.getElementsByTagName(USER_NAME_TAG)
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
        try {
            // Build document (root of XML tree)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Root element
            Element rootElement = doc.createElement(RANKINGS_TAG);
            doc.appendChild(rootElement);

            // Ranking elements
            for (Ranking r : rankings) {
                Element rankingElement = doc.createElement(RANKING_TAG);
                rootElement.appendChild(rankingElement);

                // Set id attribute for ranking element
                Attr rankingIdAttr = doc.createAttribute(RANKING_ID_ATTR);
                rankingIdAttr.setValue(Integer.toString(r.getId()));
                rankingElement.setAttributeNode(rankingIdAttr);

                // Name element as child of ranking element
                Element rankingName = doc.createElement(RANKING_NAME_TAG);
                rankingName.appendChild(doc.createTextNode(r.getName()));
                rankingElement.appendChild(rankingName);

                // Description element as child of ranking element
                Element rankingDesc = doc.createElement(RANKING_DESC_TAG);
                rankingDesc.appendChild(doc.createTextNode(r.getDescription()));
                rankingElement.appendChild(rankingDesc);

                // Ranking item elements
                for (RankingItem ri : r.getRankingItems()) {
                    Element rankingItemElement = doc.createElement(RANKING_ITEM_TAG);
                    rankingElement.appendChild(rankingItemElement);

                    // Set id attribute for ranking item element
                    Attr rankingItemIdAttr = doc.createAttribute(RANKING_ITEM_ID_ATTR);
                    rankingItemIdAttr.setValue(Integer.toString(ri.getId()));
                    rankingItemElement.setAttributeNode(rankingItemIdAttr);

                    // User ID element as child of ranking item element
                    Element rankingItemUserId = doc.createElement(RANKING_ITEM_USER_ID_TAG);
                    rankingItemUserId.appendChild(doc.createTextNode(
                            Integer.toString(ri.getUser().getId())));
                    rankingItemElement.appendChild(rankingItemUserId);

                    // Points element as child of ranking item element
                    Element rankingItemPoints = doc.createElement(RANKING_ITEM_POINTS_TAG);
                    rankingItemPoints.appendChild(doc.createTextNode(
                            Integer.toString(ri.getPoints())));
                    rankingItemElement.appendChild(rankingItemPoints);
                }
            }

            // Write content into XML file
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(RANKINGS_FILE));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
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

            NodeList nList = doc.getElementsByTagName(RANKING_TAG);

            // Loop over ranking nodes
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);

                // Determine the node is an element
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Get ranking attributes
                    int rid = Integer.parseInt(element.getAttribute(RANKING_ID_ATTR));
                    String name = element.getElementsByTagName(RANKING_NAME_TAG)
                            .item(0)
                            .getTextContent();
                    String description = element.getElementsByTagName(RANKING_DESC_TAG)
                            .item(0)
                            .getTextContent();
                    NodeList items = element.getElementsByTagName(RANKING_ITEM_TAG);

                    // Initialize ranking item list
                    List<RankingItem> rankingItems = new ArrayList<>();

                    // Loop over item nodes
                    for (int i = 0; i < items.getLength(); i++) {
                        Node iNode = items.item(i);

                        // Determine the node is an element
                        if (iNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element iElement = (Element) iNode;

                            // Get ranking item attributes
                            int iid = Integer.parseInt(iElement
                                    .getAttribute(RANKING_ITEM_ID_ATTR));
                            int uid = Integer.parseInt(iElement
                                    .getElementsByTagName(RANKING_ITEM_USER_ID_TAG)
                                    .item(0)
                                    .getTextContent());
                            int points = Integer.parseInt(iElement
                                    .getElementsByTagName(RANKING_ITEM_POINTS_TAG)
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
                                // Uid is not in user list, so this ranking item is discarded
                                System.err.println(
                                        "Uid (" + uid + ") does not correspond with any user in user list");
                                break;
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
