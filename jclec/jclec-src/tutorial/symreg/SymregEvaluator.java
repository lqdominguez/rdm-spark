package tutorial.symreg;

import java.util.Comparator;

import net.sf.jclec.IConfigure;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.exprtree.ExprTree;
import net.sf.jclec.exprtree.ExprTreeIndividual;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.fitness.ValueFitnessComparator;

import org.apache.commons.configuration.Configuration;

/**
 * ExprTreeFunction execution example 
 * 
 * @author Sebastian Ventura
 * @author Alberto Cano
 */

public class SymregEvaluator extends AbstractEvaluator implements IConfigure
{
	private static final long serialVersionUID = 1L;

	private static final Comparator<IFitness> COMPARATOR = new ValueFitnessComparator(true);
	
	private double [] xvalues;
	
	private double [] yvalues;
	
	//////////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Protected methods
	//////////////////////////////////////////////////////////////////////
	
	@Override
	public void configure(Configuration settings)
	{
		String[] x = settings.getString("xvalues").split(",");
		String[] y = settings.getString("yvalues").split(",");
		
		int numberElements = x.length;
		
		xvalues = new double[numberElements];
		yvalues = new double[numberElements];
		
		for(int i = 0; i < numberElements; i++)
		{
			xvalues[i] = Double.parseDouble(x[i]);
			yvalues[i] = Double.parseDouble(y[i]);
		}
	}

	@Override
	protected void evaluate(IIndividual ind) 
	{
		// Individual genotype
		ExprTree genotype = ((ExprTreeIndividual) ind).getGenotype();

		ExprTreeFunction function = new ExprTreeFunction(genotype);

		// Estimated values
		double [] y = new double[xvalues.length];
		
		for(int i = 0; i<xvalues.length; i++)
			y[i] = function.<Double>execute(xvalues[i]);
		
		// Pass all
		double rms = 0.0;
		
		for (int i=0; i<yvalues.length; i++) {
			double diff = y[i] - yvalues[i];
			rms += diff * diff;
		}
		
		rms = Math.sqrt(rms);
		
		// Set rms as fitness for ind
		ind.setFitness(new SimpleValueFitness(rms));
	}

	@Override
	public Comparator<IFitness> getComparator() {
		return COMPARATOR;
	}
}