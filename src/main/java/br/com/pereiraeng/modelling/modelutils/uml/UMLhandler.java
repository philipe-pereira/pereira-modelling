package br.com.pereiraeng.modelling.modelutils.uml;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import br.com.pereiraeng.io.IOutils;
import br.com.pereiraeng.xml.XMLadapter;

public class UMLhandler extends XMLadapter {

	private Collection<UMLobject> umls;

	public static Collection<UMLobject> read(File file) {
		UMLhandler xmn = new UMLhandler();
		xmn.parse(file);
		return xmn.getUMLs();
	}

	private Collection<UMLobject> getUMLs() {
		return umls;
	}

	private static UMLobject getObj(String name, Collection<UMLobject> objs) {
		for (UMLobject o : objs)
			if (o.getNome().equals(name))
				return o;
		return null;
	}

	@Override
	public void startDocument() throws SAXException {
		umls = new LinkedList<UMLobject>();
	}

	private transient UMLobject uml;

	@Override
	public void startElement(String qName, Attributes atts) {
		switch (qName) {
		case "table":
			uml = new UMLobject(atts.getValue("name"), atts.getValue("description"));
			umls.add(uml);
			break;
		case "field":
			uml.add(new UMLfield(UMLfield.valueOf(atts.getValue("type")), atts.getValue("name"),
					atts.getValue("description")));
			break;
		case "relation":
			new UMLrelation(getObj(atts.getValue("table1"), umls).get(atts.getValue("field1"), false),
					getObj(atts.getValue("table2"), umls).get(atts.getValue("field2"), false),
					atts.getValue("description"));
			break;
		}
	}

	@Override
	public void characters(String s) {
	}

	@Override
	public void endElement(String qName) {
		switch (qName) {
		case "table":
			uml = null;
			break;
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

	// ------------------------- WRITER -------------------------

	public static void write(File file, String name, Collection<? extends UMLobject> objs) {
		String out = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<database name=\"" + name + "\">\n";

		out += "\t<tables>\n";
		for (UMLobject obj : objs)
			out += obj.toXML();

		out += "\t</tables>\n\t<relations>\n";

		for (UMLobject obj : objs)
			out += obj.toXMLrel();
		out += "\t</relations>\n";

		out += "</database>";

		IOutils.writeFile(file, out);
	}
}
