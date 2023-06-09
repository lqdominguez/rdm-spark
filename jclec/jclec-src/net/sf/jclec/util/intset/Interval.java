package net.sf.jclec.util.intset;

import net.sf.jclec.IConfigure;
import net.sf.jclec.util.random.IRandGen;


import org.apache.commons.lang.builder.ToStringBuilder;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

/**
 * Arithmetic interval.
 * 
 * @author Sebastian Ventura
 */

public class Interval implements IIntegerSet, IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */

	private static final long serialVersionUID = 4742142066614454991L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Left extremum */
	
	protected int left;

	/** Right extremum */
	
	protected int right;
	
	/** Interval closure */

	protected Closure closure;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public Interval()
	{
		super();
	}

	/**
	 * Constructor that set interval properties.
	 * 
	 * @param left    Left extremum
	 * @param right   Right extremum
	 * @param closure Interval closure
	 */
	
	public Interval(int left, int right, Closure closure)
	{
		super();
		setClosure(closure);
		setLeft(left);
		setRight(right);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	// Setting and getting properties
	
	/**
	 * Access to interval closure
	 * 
	 * @return Interval closure
	 */
	
	public Closure getClosure() 
	{
		return closure;
	}

	/**
	 * Set interval closure.
	 * 
	 * @param closure New interval closure value
	 */
	
	public void setClosure(Closure closure) 
	{
		this.closure = closure;
	}

	/**
	 * Access to left extremum
	 * 
	 * @return Left extremum
	 */
	
	public int getLeft() 
	{
		return left;
	}

	/**
	 * Set left extremum
	 * 
	 * @param left Left extremum
	 */
	
	public void setLeft(int left) 
	{
		this.left = left;
	}

	/**
	 * Access to right extremum
	 * 
	 * @return Right extremum
	 */
	
	public int getRight() 
	{
		return right;
	}

	/**
	 * Set right extremum
	 * 
	 * @param right Right extremum
	 */

	public void setRight(int right) 
	{
		this.right = right;
	}
	
	// IIntSet interface
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean contains(int value)
	{
		switch(closure)
		{
			case OpenOpen:
			{
				return ((value > left) && (value < right));
			}
			case OpenClosed:
			{
				return ((value > left) && (value <= right));				
			}
			case ClosedOpen:
			{
				return ((value >= left) && (value < right));				
			}
			case ClosedClosed:
			{
				return ((value >= left) && (value <= right));				
			}
		}
		// This point is never reached
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public int size() 
	{
		return (efRight()-efLeft());
	}

	/**
	 * {@inheritDoc}
	 */
	
	public int getRandom(IRandGen randgen)
	{
		return randgen.choose(rndLeft(), rndRight());
	}
	
	// IConfigure interface
	
	/**
	 * Configuration parameters for an interval are:
	 * 
	 * <ul>
	 * <li>
	 * <code>[@left] (int)</code> </p>
	 * Left extremum 
	 * </li>
	 * <li>
	 * <code>[@right] (int)</code> </p>
	 * Right extremum 
	 * </li>
	 * <li>
	 * <code>[@closure] (String)</code> </p>
	 * Interval closure. Supported values are "closed-closed", "closed-open", 
	 * "open-closed" and "open-open". Default value is "closed-closed".   
	 * </li>
	 * </ul>
	 */
	
	public void configure(Configuration configuration)  
	{
		// Get left extremum
		int left = configuration.getInt("[@left]");
		// Set left extremum
		setLeft(left);
		// Get right extremum
		int right = configuration.getInt("[@right]");
		// Set right extremum
		setRight(right);
		// Get closure string
		String closureString = configuration.getString("[@closure]", "closed-closed");
		// Convert closureString
		Closure closure;
		if (closureString.equals("closed-closed")) {
			closure = Closure.ClosedClosed;
		}
		else if (closureString.equals("open-open")) {
			closure = Closure.OpenOpen;
		}
		else if (closureString.equals("closed-open")) {
			closure = Closure.ClosedOpen;
		}
		else if (closureString.equals("open-closed")) {
			closure = Closure.OpenClosed;
		}
		else {
			throw new ConfigurationRuntimeException("Illegal value for interval closure");
		}
		// Set closure
		setClosure(closure);
	}
	
	// java.lang.Oject methods
	
	public boolean equals(Object other)
	{
		if (other instanceof Interval){
			Interval cother = (Interval) other;
			return (closure == cother.closure && left == cother.left && right == cother.right);
		}
		else {
			return false;
		}
	}

	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this);
		tsb.append(closure);
		tsb.append("left",left);
		tsb.append("right",right);
		return tsb.toString();
	}
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Efective left extremum
	 */
	
	private final int efLeft()
	{
		switch(closure)
		{
			case OpenOpen:
			case OpenClosed:
				return (left + 1);				
			case ClosedOpen:
			case ClosedClosed:
				return left;				
		}
		// This point is never reached
		return 0;
	}

	/**
	 * Efective right extremum
	 */
	
	private final int efRight()
	{
		switch(closure)
		{
			case OpenOpen:
			case ClosedOpen:
				return (right - 1);				
			case OpenClosed:
			case ClosedClosed:
				return right;				
		}
		// This point is never reached
		return 0;
	}

	/**
	 * Efective left extremum to use in random() method
	 */
	
	private final int rndLeft()
	{
		switch(closure)
		{
			case OpenOpen:
			case OpenClosed:
				return left + 1;				
			case ClosedOpen:
			case ClosedClosed:
				return left;				
		}
		// This point is never reached
		return 0;
	}

	/**
	 * Efective right extremum to use in random() method
	 */
	
	private final int rndRight()
	{
		switch(closure)
		{
			case OpenOpen:
			case ClosedOpen:
				return right;				
			case OpenClosed:
			case ClosedClosed:
				return right+1;				
		}
		// This point is never reached
		return 0;
	}
	
}
