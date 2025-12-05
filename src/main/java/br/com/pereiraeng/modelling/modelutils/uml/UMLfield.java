package br.com.pereiraeng.modelling.modelutils.uml;

import java.lang.reflect.Field;
// import java.sql.JDBCType; TODO só para Java 1.8
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import br.com.pereiraeng.core.DisplayableFields;


/**
 * Classe dos objetos que representam os campos de um {@link UMLobject objeto
 * UML}
 * 
 * @author Philipe PEREIRA
 *
 */
public class UMLfield implements DisplayableFields {

	public static int valueOf(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		switch (value) {
		case "char_":
			return Types.CHAR;
		case "int_":
			return Types.INTEGER;
		case "float_":
			return Types.FLOAT;
		case "varchar_":
			return Types.VARCHAR;
		case "bit_":
			return Types.BIT;
		case "smallint_":
			return Types.SMALLINT;
		case "datetime_":
			return Types.DATE;
		case "sysname_":
		case "image_":
			return Types.BLOB;
		case "nvarchar_":
			return Types.NVARCHAR;
		case "numeric_":
			return Types.NUMERIC;
		case "real_":
			return Types.REAL;
		default:
			return -1;
		}
	}

	private int type;

	private String campo, descricao;

	private UMLobject object;

	private List<UMLrelation> relations;

	public UMLfield(int type, String campo, String descricao) {
		this.type = type;
		this.campo = campo;
		this.descricao = descricao;
	}

	public int getType() {
		return type;
	}

	public String getCampo() {
		return campo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setUMLobject(UMLobject object) {
		this.object = object;
	}

	public UMLobject getObject() {
		return object;
	}

	public void addRelation(UMLrelation relation) {
		if (this.relations == null)
			this.relations = new LinkedList<>();
		this.relations.add(relation);
	}

	public List<UMLrelation> getRelations() {
		return this.relations;
	}

	@Override
	public String toString() {
		return this.campo;
	}

	// ------------------- DISPLAYABLE FIELDS -------------------

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	public String getFieldName(int index) {
		switch (index) {
		case 0:
			return "Campo";
		case 1:
			return "Descrição";
		default:
			return "Tipo";
		}
	}

	@Override
	public Object getField(int index) {
		switch (index) {
		case 0:
			return campo;
		case 1:
			return descricao;
		default:
			// return JDBCType.valueOf(type).getName(); TODO só para Java 1.8
			try {
				Class<?> c = Class.forName("java.sql.Types");
				Field[] fields = c.getFields();
				for (Field f : fields) {
					f.setAccessible(true);
					if (type == (int) f.get(null))
						return f.getName();
				}
			} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public String getXML() {
		String out = "\t\t\t<field name=\"" + campo;
		if (descricao != null)
			out += "\" description=\"" + descricao.replace("'", "&apos;").replace("\"", "&quot;");
		out += "\" type=\"" + type + "\"/>\n";
		return out;
	}
}
