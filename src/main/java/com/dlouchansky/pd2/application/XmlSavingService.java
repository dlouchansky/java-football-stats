package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.service.xml.data.XmlGame;

import java.io.File;

public interface XmlSavingService {

    boolean addXml(File file);

}
