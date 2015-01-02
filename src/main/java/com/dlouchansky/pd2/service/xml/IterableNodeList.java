package com.dlouchansky.pd2.service.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Iterator;

public class IterableNodeList implements Iterable<Element> {
    private NodeList nodeList;
    private Integer current;

    public IterableNodeList(NodeList nodeList) {
        this.nodeList = nodeList;
        this.current = 0;
    }

    @Override
    public Iterator<Element> iterator() {
        return new Iterator<Element>() {
            @Override
            public boolean hasNext() {
                return nodeList.getLength() > current;
            }

            @Override
            public Element next() {
                return (Element)nodeList.item(current++);
            }
        };
    }
}
