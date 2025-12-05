package br.com.pereiraeng.modelling.modelutils.uml;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.List;

import br.com.pereiraeng.core.EditableFields;
import br.com.pereiraeng.core.StringUtils;
import br.com.pereiraeng.swing.LeafOM;
import br.com.pereiraeng.swing.MonoSpacedFont;
import br.com.pereiraeng.swing.interfaces.Click;
import br.com.pereiraeng.swing.interfaces.DesM;
import br.com.pereiraeng.swing.interfaces.WL;

/**
 * Classe dos objetos que representam um objeto UML, contendo diferentes
 * {@link UMLfield campos}
 * 
 * @author Philipe PEREIRA
 *
 */
public class UMLobject extends ArrayList<UMLfield> implements DesM, Click, EditableFields {
	private static final long serialVersionUID = 1L;

	private static final MonoSpacedFont FONT = MonoSpacedFont.CN12;

	private String nome;

	private String descricao;

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
	 *               {@link StringUtils#levenshteinDistance(String, String) distância de
	 *               Levenshtein} entre os campos
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

	@Override
	public boolean add(UMLfield field) {
		field.setUMLobject(this);
		w = Math.max(field.getCampo().length(), w);
		return super.add(field);
	}

	// ------------------- DRAWER -------------------

	private transient int w = 0;

	private boolean drawable = false;

	private Color highlight;

	private WL wl;

	private Point2D.Float loc = new Point2D.Float((float) Math.random(), (float) Math.random());

	public int getW() {
		return w;
	}

	public void setLoc(float x, float y) {
		loc.setLocation(x, y);
	}

	public void setHighlight(Color highlight) {
		this.highlight = highlight;
	}

	@Override
	public void setDrawable(boolean drawable) {
		this.drawable = drawable;
	}

	@Override
	public boolean isDrawable() {
		return drawable;
	}

	@Override
	public void drawObject(Graphics2D g) {
		g.setFont(FONT.getFont());
		Point p = LeafOM.getTranformedPoint(loc.x, loc.y, wl);

		// nome
		g.drawString(nome, p.x, p.y);

		// caixa
		if (highlight != null) {
			g.setColor(highlight);
			g.fillRect(p.x, p.y, w * FONT.getWidth(), FONT.getHeight() * this.size());
			g.setColor(Color.BLACK);
		}
		g.drawRect(p.x, p.y, w * FONT.getWidth(), FONT.getHeight() * this.size());

		// campos
		for (int i = 0; i < this.size(); i++) {
			UMLfield o = this.get(i);
			g.drawString(o.getCampo(), p.x, p.y + FONT.getHeight() + i * FONT.getHeight());

			List<UMLrelation> rs = o.getRelations();
			if (rs != null)
				for (UMLrelation r : rs)
					draw(g, wl, o, r, highlight);
		}
	}

	private static void draw(Graphics2D g, WL wl, UMLfield f1, UMLrelation r, Color highlight) {
		UMLfield f2 = r.getOtherField(f1);

		UMLobject o1 = f1.getObject();
		UMLobject o2 = f2.getObject();

		if (!o1.isDrawable() || !o2.isDrawable())
			return;

		Point2D.Float l = o1.getMin();
		Point p1 = LeafOM.getTranformedPoint(l.x, l.y, wl);
		int i1 = o1.indexOf(f1);
		p1.y = p1.y + FONT.getHeight() / 2 + i1 * FONT.getHeight();

		l = o2.getMin();
		Point p2 = LeafOM.getTranformedPoint(l.x, l.y, wl);
		int i2 = o2.indexOf(f2);
		p2.y = p2.y + FONT.getHeight() / 2 + i2 * FONT.getHeight();

		if (p1.x > p2.x)
			p2.x += o2.getW() * FONT.getWidth();
		else
			p1.x += o1.getW() * FONT.getWidth();

		// só desenha metada do caminho: a outra metada que desenha é o outro campo (que
		// também tem uma referência, apontanda para esta)
		if (highlight != null)
			g.setColor(highlight);

		g.drawLine(p1.x, p1.y, (p2.x + p1.x) / 2, p1.y);
		g.drawLine((p2.x + p1.x) / 2, p1.y, (p2.x + p1.x) / 2, (p2.y + p1.y) / 2);

		if (highlight != null)
			g.setColor(Color.BLACK);
	}

	@Override
	public void setWL(WL wl) {
		this.wl = wl;
	}

	@Override
	public boolean wasDrawn() {
		return true;
	}

	@Override
	public Float getMin() {
		return loc;
	}

	@Override
	public Float getMax() {
		return loc;
	}

	@Override
	public boolean isOn(int x, int y) {
		return getClickableArea().contains(x, y);
	}

	@Override
	public Area getClickableArea() {
		Point p = LeafOM.getTranformedPoint(loc.x, loc.y, wl);
		Rectangle2D.Float rect = new Rectangle2D.Float(p.x, p.y, w * FONT.getWidth(), this.size() * FONT.getHeight());
		return new Area(rect);
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
		String out = "\t\t<table name=\"" + nome;
		if (descricao != null)
			out += "\" description=\"" + descricao.replace("'", "&apos;").replace("\"", "&quot;");
		out += "\">\n";
		for (UMLfield field : this)
			out += field.getXML();
		out += "\t\t</table>\n";
		return out;
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
