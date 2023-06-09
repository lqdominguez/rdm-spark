package net.sf.jclec.selector;

import net.sf.jclec.ISystem;
import net.sf.jclec.IIndividual;

import net.sf.jclec.fitness.IValueFitness;

/*
 * Nombre: JerarquiasSelector
 * Autor: Rafael AyllA?n Iglesias
 * Tipo: Clase publica
 * Extiende: La clase RouletteSelector y la interfaz IIndividual
 * Implementa: Nada
 * Variables de la clase: serialVersionUID (generado por eclipse) 
 * Metodos: Protegidos: prepareSelection 
 *          Publicos: Ninguno
 * Objetivo de la clase: Esta clase pretende implementar el selector mediante el 
 * 						 uso de jerarquias, con lo cual en esta clase en su metodo 
 * 						 prepareSelection se inicianilaran los datos de la ruleta 
 *                       la cual se usara para realizar la seleccion.
 *                       
 */

/**
 * @author Rafael AyllA?n Iglesias
 * @author SebastiA?n Ventura 
 */

public class HierarchicalSelector extends RouletteSelector 
{
	/////////////////////////////////////////////////////////////////
	// -------------------------------------- Serialization constants
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -422501366558313344L;

	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty (default constructor).
	 */
	
	public HierarchicalSelector() 
	{
		super();
	}

	/**
	 * Constructor that contextualize selector
	 * 
	 * @param context Execution context
	 */
	
	public HierarchicalSelector(ISystem context) 
	{
		super(context);
	}

		
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Nombre: prepareSelection
	 * Autor: Rafael AyllA?n Iglesias.
	 * Tipo funcion: Protegida
	 * Valores de entrada: Ninguna
	 * Valores de salida: Ninguna
	 * Funciones que utiliza: Ninguna
	 * Variables:- FitnessOrdenado es un vector ordenado con los fitness de los
	 * 			 individuos de la poblacion
	 *           - min es una variable del algoritmo que viene determinada por 
	 *           el valor que se le introduzca a max
	 *           - max es una variable del algoritmo que el autor de la tecnca
	 *           recommienda poner a 1.1
	 *           - jerarquia es una variable que almacena la jerarquia a la 
	 *           que pertenece el individuo
	 * 	  		 - acc es el acumulado de las partes de la ruleta
	 * 			 - idx es el indice de la ruleta
	 * Objetivo: Preparar las variables para la utilizacion de la ruleta
	 * 
	 */
	
	@Override
	protected void prepareSelection() 
	{

		// Allocates space for roulette
		if ((roulette == null) || (roulette.length != actsrcsz)) {
			roulette = new double[actsrcsz];
		}
		
		//Calculo datos
		int jerarquia = 0;
		double [] FitnessOrdenado;		
		FitnessOrdenado = new double[actsrcsz];
		
		int j=0;
		
		//Obtengo Fitness de todos los individuos de la poblacion
		for (IIndividual ind : actsrc) {
			// Fitness value for actual individual
			double val = ((IValueFitness) ind.getFitness()).getValue();			
			FitnessOrdenado[j]=val;
			j++;			
		}
		
		//Ordeno el vector de Fitness
		 for (int i=1; i<actsrcsz;i++) //primer for para recorrer el array tantas veces como sea su tamaA?o.

		   {
		       for(int k=0;k<actsrcsz-1;k++) // segundo for para recorrer el array y dejar cada vez el valor menor hasta abajo
		       {
		           if (FitnessOrdenado[k] > FitnessOrdenado[k+1]) //realiza la comparaciA?n con su siguiente en el array

		           {
		               double temp = FitnessOrdenado[k]; //si se cumple, realiza el cambio
		               FitnessOrdenado[k]= FitnessOrdenado[k+1];
		               FitnessOrdenado[k+1]= temp;
		           }
		       }
		   }
		
		
		//Inicializo variables de la tecnica
		double max=1.1;//Valor recomendado en el algoritmo
		double min= 2 -max;
			
		
		// Sets roulette values		
		double acc = 0.0; int idx = 0;
		for (IIndividual ind : actsrc) {
			// Fitness value for actual individual
			double val = ((IValueFitness) ind.getFitness()).getValue();
			
			//Averiguo jerarquia del Fitness actual
			int k=0;
			for (k=0; k<actsrcsz;k++)
			{
				if(FitnessOrdenado[k] == val)
				{
					jerarquia=k;
					break;					
				}
			}
			
			// Calculate 
			val = min + (max-min)*((jerarquia)/(actsrcsz-1));
			// Update acc 
			acc += val;
			// Set roulette value
			roulette[idx++] = acc;
		}
			
		// Normalize roulette values
		for ( ; idx > 0;) {
			roulette[--idx] /= acc;
		
		}
	}	
	

}