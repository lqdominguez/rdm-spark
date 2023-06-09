package net.sf.jclec.orderarray;

import net.sf.jclec.IIndividual;
import net.sf.jclec.ISpecies;
import net.sf.jclec.util.intset.IIntegerSet;

/**
 * Species for OrderArrayIndividual
 *  
 * @author Alberto Cano
 * @author Jose Maria Luna
 * @author Juan Luis Olmo
 * @author Amelia Zafra
 * @author Sebastian Ventura
 */

public interface IOrderArraySpecies extends ISpecies
{
	/**
	 * Factory method.
	 * 
	 * @param genotype Individual genotype.
	 * 
	 * @return A new instance of represented class
	 */
	
	public IIndividual createIndividual(int [] genotype);
	
	/**
	 * Informs about individual genotype length.
	 * 
	 * @return getGenotypeSchema().length
	 */
	
	public int getGenotypeLength();
	
	/**
	 * @return This genotype schema
	 */
	
	public IIntegerSet [] getGenotypeSchema();
}