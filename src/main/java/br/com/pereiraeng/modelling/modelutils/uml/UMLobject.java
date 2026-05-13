package br.com.pereiraeng.modelling.modelutils.uml;

import java.util.ArrayList;
import java.util.List;

import br.com.pereiraeng.core.EditableFields;
import br.com.pereiraeng.core.StringUtils;

/**
 * Classe dos objetos que representam um objeto UML, contendo diferentes
 * {@link UMLfield campos}
 * 
 * @author Philipe PEREIRA
 *
 */
public class UMLobject extends ArrayList<UMLfield> implements EditableFields {
	private static final long serialVersionUID = 1L;

	protected String nome;

	private String descricao;

	protected boolean drawable = false;

	public UMLobject(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	/**
	 * 
	 * @param field
	 * @param strong <code>true</code> para a busca na força (tem de sair que um
	 *               resultado, que é obtido a partir da menor
	 *               {@link StringUtils#levenshteinDistance(String, String)
	 *               distância de Levenshtein} entre os campos
	 * @return
	 */
	public UMLfield get(String field, boolean strong) {
		UMLfield out = null;
		for (UMLfield o : this) {
			String f = o.getCampo();
			if (strong) {
				if (field.startsWith(f)) {
					out = o;
					break;
				}
			} else {
				if (f.equals(field)) {
					out = o;
					break;
				}
			}
		}
		if (strong && out == null) {
			int dist = Integer.MAX_VALUE;
			for (UMLfield o : this) {
				int d = StringUtils.levenshteinDistance(o.getCampo(), field);
				if (d < dist) {
					dist = d;
					out = o;
				}
			}
		}
		return out;
	}

	protected transient int w = 0;

	@Override
	public boolean add(UMLfield field) {
		field.setUMLobject(this);
		w = Math.max(field.getCampo().length(), w);
		return super.add(field);
	}

	// ----------------- EDITABLE FIELDS -----------------

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public String getFieldName(int index) {
		return index == 0 ? "Nome" : "Mostrar?";
	}

	@Override
	public Object getField(int index) {
		return index == 0 ? this.nome : this.drawable;
	}

	@Override
	public void setField(int index, Object obj) {
		if (index == 1)
			this.drawable = (boolean) obj;
	}

	// ------------------------- XML -------------------------

	public String toXML() {
		StringBuilder out = new StringBuilder("\t\t<table name=\"");
		out.append(nome);
		if (descricao != null) {
			out.append("\" description=\"");
			out.append(descricao.replace("'", "&apos;").replace("\"", "&quot;"));
		}
		out.append("\">\n");
		for (UMLfield field : this)
			out.append(field.getXML());
		out.append("\t\t</table>\n");
		return out.toString();
	}

	public String toXMLrel() {
		String out = "";
		for (UMLfield field : this) {
			List<UMLrelation> rs = field.getRelations();
			if (rs != null)
				for (UMLrelation r : rs)
					out += r.toXML();
		}
		return out;
	}
}
