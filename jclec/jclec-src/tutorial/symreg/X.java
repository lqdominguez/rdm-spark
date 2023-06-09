package tutorial.symreg;

import net.sf.jclec.exprtree.fun.Argument;

public class X extends Argument<Double> 
{
	private static final long serialVersionUID = -653953979823993607L;

	public X() 
	{
		super(Double.class, 0);
	}
	
	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof X;
	}	
	
	public String toString()
	{
		return "X";
	}
}