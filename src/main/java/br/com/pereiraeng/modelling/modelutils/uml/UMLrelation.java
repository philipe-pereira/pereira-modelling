package br.com.pereiraeng.modelling.modelutils.uml;

public class UMLrelation {

	private final UMLfield field1, field2;

	private final String desc;

	public UMLrelation(UMLfield u1, UMLfield u2, String desc) {
		this.field1 = u1;
		this.field2 = u2;
		u1.addRelation(this);
		u2.addRelation(this);
		this.desc = desc;
	}

	public UMLfield getOtherField(UMLfield field) {
		return field1 == field ? field2 : field1;
	}

	public String toXML() {
		String out = "\t\t<relation table1=\"" + field1.getObject().getNome() + "\" field1=\"" + field1.getCampo()
				+ "\" table2=\"" + field2.getObject().getNome() + "\" field2=\"" + field2.getCampo() + "\"";
		if (desc != null)
			out += " description=\"" + desc + "\"";
		return out + "/>\n";
	}
}
