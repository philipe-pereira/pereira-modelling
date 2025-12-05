package br.com.pereiraeng.modelling.modelutils.graph.elt;

import br.com.pereiraeng.graph.numbered.VertexN;

/**
 * <p>
 * Nó de sistema de transferência de potência elétrica que representa uma parte
 * de um circuito elétrico que está num mesmo potencial, podendo, assim ser
 * caracterizado num dado momento, por um valor de nível de tensão.
 * </p>
 * <p>
 * Tem como característica possuir uma amplitude de referência (valor de base) e
 * uma frequência fixa (DC, 50 ou 60 Hz, exceto no regime transitório), não
 * portando, portanto, informação.
 * </p>
 * 
 * @author Philipe PEREIRA
 *
 */
public interface PNoeud extends ENoeud, VertexN {

}
