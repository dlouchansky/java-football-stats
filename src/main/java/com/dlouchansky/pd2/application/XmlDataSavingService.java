package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.service.xml.data.XmlGame;

public interface XmlDataSavingService {

    Integer saveGame(XmlGame game, Tournament tournament);

}
