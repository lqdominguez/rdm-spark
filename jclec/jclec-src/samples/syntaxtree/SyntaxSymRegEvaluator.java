/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package samples.syntaxtree;

import java.util.Comparator;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractEvaluator;
import net.sf.jclec.exprtree.ExprTree;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.fitness.ValueFitnessComparator;
import net.sf.jclec.syntaxtree.SyntaxTree;
import net.sf.jclec.syntaxtree.SyntaxTreeIndividual;


/**
 *
 * @author Luis A. Quintero Domínguez
 */
public class SyntaxSymRegEvaluator
  extends AbstractEvaluator
{
  private static final long serialVersionUID = -2279944053662012839L;
  ExprTreeFunction function = new ExprTreeFunction();
  Double[] xvalues = { Double.valueOf(-2.0D), Double.valueOf(-1.0D), Double.valueOf(0.0D), Double.valueOf(1.0D), Double.valueOf(2.0D) };
  Double[] yvalues = { Double.valueOf(10.0D), Double.valueOf(0.0D), Double.valueOf(0.0D), Double.valueOf(4.0D), Double.valueOf(30.0D) };
  Comparator<IFitness> COMPARATOR = new ValueFitnessComparator(true);
  
  protected void evaluate(IIndividual ind)
  {
    ExprTree genotype = ((SyntaxTree)((SyntaxTreeIndividual)ind).getGenotype()).getExprTree();
    
    this.function.setCode(genotype);
    
    Double[] arg = new Double[1];
    
    double rms = 0.0D;
    for (int i = 0; i < 5; i++)
    {
      arg[0] = this.xvalues[i];
      
      Double y = (Double)this.function.execute(arg);
      
      Double diff = Double.valueOf(this.yvalues[i].doubleValue() - y.doubleValue());
      rms += diff.doubleValue() * diff.doubleValue();
    }
    rms = Math.sqrt(rms);
    
    ind.setFitness(new SimpleValueFitness(rms));
  }
  
  public Comparator<IFitness> getComparator()
  {
    return this.COMPARATOR;
  }
}
