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

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

/**
 *
 * @author Luis A. Quintero Domínguez
 */
public class Minus
  extends AbstractPrimitive
{
  private static Class<?>[] ARGTYPES = { Double.class, Double.class };
  private static Class<?> RETURNTYPE = Double.class;
  
  public Minus()
  {
    super(new Class[] { Double.class, Double.class }, Double.class);
  }
  
  public void evaluate(ExprTreeFunction context)
  {
    Double arg1 = (Double)pop(context);
    Double arg2 = (Double)pop(context);
    push(context, Double.valueOf(arg1.doubleValue() - arg2.doubleValue()));
  }
  
  public String toString()
  {
    return "-";
  }
}