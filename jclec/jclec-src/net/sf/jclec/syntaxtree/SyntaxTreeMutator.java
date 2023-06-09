package net.sf.jclec.syntaxtree;

import net.sf.jclec.IConfigure;
import net.sf.jclec.ISpecies;

import net.sf.jclec.base.AbstractMutator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

/**
 * Abstract mutator for SyntaxTreeIndividual and its subclasses.
 * 
 * @author Sebastian Ventura
 */

public class SyntaxTreeMutator extends AbstractMutator implements IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 450351162139336504L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Individual species */
	
	protected transient SyntaxTreeSpecies species;

	/** Individuals schema */
	
	protected transient SyntaxTreeSchema schema;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Base operation for this expression tree mutator */
	
	protected IMutateSyntaxTree baseOp;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty (default) constructor
	 */
	
	public SyntaxTreeMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setters and getters

	public IMutateSyntaxTree getBaseOp() 
	{
		return baseOp;
	}

	public void setBaseOp(IMutateSyntaxTree baseOp) 
	{
		this.baseOp = baseOp;
	}

	// IConfigure interface
	
	@Override
	@SuppressWarnings("unchecked")
	public void configure(Configuration settings) 
	{
		// Set base mutator name
		try {
			// Base mutator classname
			String baseOpClassname = 
				settings.getString("base-op[@type]");
			// Evaluator class
			Class<? extends IMutateSyntaxTree> baseOpClass = 
				(Class<? extends IMutateSyntaxTree>) Class.forName(baseOpClassname);
			// Evaluator instance
			IMutateSyntaxTree baseOp = baseOpClass.newInstance();
			// Configure species
			if (baseOp instanceof IConfigure) {
				((IConfigure) baseOp).configure(settings.subset("base-op"));
			}
			// Set species
			setBaseOp(baseOp);
		} 
		catch (ClassNotFoundException e) {
			throw new ConfigurationRuntimeException("Illegal operator classname");
		} 
		catch (InstantiationException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of operator", e);
		} 
		catch (IllegalAccessException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of operator", e);
		}
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	// AbstractMutator methods

	@Override
	protected void prepareMutation() 
	{
		ISpecies species = context.getSpecies();
		if (species instanceof SyntaxTreeSpecies) {
			// Type conversion 
			this.species = (SyntaxTreeSpecies) species;
			// Sets genotype schema
			this.schema = ((SyntaxTreeSpecies) species).getGenotypeSchema();
		}
		else {
			throw new IllegalStateException("Invalid species in context");
		}
	}

	@Override
	protected void mutateNext() 
	{
		// Actual individual
		SyntaxTree parentGenotype = 
			((SyntaxTreeIndividual) parentsBuffer.get(parentsCounter)).getGenotype();
		// Mutate target
		SyntaxTree sonGenotype =  baseOp.mutateSyntaxTree(parentGenotype, schema, randgen);
		sonsBuffer.add(species.createIndividual(sonGenotype));
	}
}
