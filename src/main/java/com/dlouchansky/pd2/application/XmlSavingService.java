package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.service.xml.data.XmlGame;

import java.io.File;

public interface XmlSavingService {

    XmlGame parseFile(File file);

    Integer saveGame(XmlGame game);

    Integer saveGame(XmlGame game, Tournament tournament);

}
