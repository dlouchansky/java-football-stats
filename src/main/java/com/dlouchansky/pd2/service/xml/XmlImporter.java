package com.dlouchansky.pd2.service.xml;


import com.dlouchansky.pd2.service.xml.data.XmlGame;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public interface XmlImporter {

    void init(File xmlFile) throws ParserConfigurationException, SAXException, IOException;

    XmlGame parseGame() throws ParseException;
}
