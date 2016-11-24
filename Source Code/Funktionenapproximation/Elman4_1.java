package elman_4;

public class Elman4_1 {
	
	public static void main(String[] args) {

		final double etha = 0.7;
		final int anzahl_epoch = 100;
		final int anzahl_muster = 100;
		
		// Input
		double[] u = new double[anzahl_muster];
		for(int i=0; i<u.length; i++) {
			u[i] = Math.random();
		}
		// gewuenschter Output
		double[] d = new double[anzahl_muster];
		for(double i=0; i<3; i++) {
			d[(int)i] = function(i/10);
		}
		// Output Hiddenneuron
		double[] x = new double[anzahl_muster];
		for(int i=0; i<3; i++) {
			x[i] = Math.random();
		}
		// Output Outputneuron
		double[] y = new double[anzahl_muster];
		y[0] = 1.0;
		y[1] = 0.9704374249051358;
		y[2] = 0.9322681668123085;

		/******************************
		 *                            *
		 * Gewichte initialisieren    *
		 *                            *
		 ******************************/
		double wu1 = 0.5;
		double wx1 = 0.5;
		double wx2 = 0.5;
		double wx3 = 0.5;
		double wy1 = 0.5;
		
		double v = 0.0;

		for(int i = 0; i < anzahl_epoch; i++) {
			
			for(int n = 3; n < anzahl_muster; n++) {
				
				// Berechnung gewuenschter Output
				d[n]  = function((double)n/10);
				
				// Berechnung Output Hiddenneuron
				v = wx1 * x[n-1] + wx2 * x[n-2] + wx3 * x[n-3] + wu1 * u[n]; // x, y ??
				x[n] = sigmoid(v);
				
				// Berechnung Output Outputneuron
				y[n] = wy1 * x[n];
				
				// Update der Gewichte
				wu1 = wu1 + etha * (d[n]-y[n]) * wy1 * sigmoidStrich(u[n]);
				
				wx1 = wx1 + etha * kontext1(d[n], y[n], wy1, kontext2(x[n], wx1, v));
				wx2 = wx2 + etha * kontext1(d[n], y[n], wy1, kontext2(x[n], wx2, v));
				wx3 = wx3 + etha * kontext1(d[n], y[n], wy1, kontext2(x[n], wx3, v));
				
				wy1 = wy1 + etha * (d[n]-y[n]) * x[n];
				
			}
		}
			
		
		//Ausgabe der Werte
		System.out.println("wu1: " + wu1 + 
					  "\nwx1: " + wx1 + "\t wx2: " + wx2 + "\t wx3: " + wx3 + "\t wy1:  " + wy1);
		System.out.println("x(n): " + x[x.length-1] + "\t x(n-1): " + x[x.length-2] + "\t x(n-2): " + x[x.length-3] + "\t x(n-3): " + x[x.length-4]);
		System.out.println("y(n): " + y[y.length-1] + "\t y(n-1): " + y[y.length-2] + "\t y(n-2): " + y[y.length-3] + "\t y(n-3): " + y[y.length-4]);
		System.out.println("d(n): " + d[d.length-1] + "\t d(n-1): " + d[d.length-2] + "\t d(n-2): " + d[d.length-3] + "\t d(n-3): " + d[d.length-4]);
		System.out.println();
		
		//Ausgabe gewuenschter Output
		for(int n = 0; n  < anzahl_muster; n++) {
			System.out.print(d[n] + ", ");
		}
		System.out.println();
		//Ausgabe Netzoutput
		for(int n = 0; n  < anzahl_muster; n++) {
			System.out.print(y[n] + ", ");
		}
		System.out.println();
		double error = 0;
		//Ausgabe Fehler > 0.1
		for(int n = 0; n  < anzahl_muster; n++) {
			double e = Math.abs(d[n] - y[n]);
			error += Math.pow(e, 2);
			if(e > 0.1) {
				System.out.println("Fehler: " + e);
			}
		}
		//Ausgabe Gesamtfehler
		System.out.println("E = " + 0.5*error);
	}
	
	
	/******************************************************************
	 * function E^(-x/4) Cos[x]
	 * functionStrich -(1/4) E^(-x/4) Cos[x] - E^(-x/4) Sin[x]
	 * @param x
	 * @return
	 ******************************************************************/
	public static double function(double x) {
		return Math.exp(-x/4) * Math.cos(x);
	}
	public static double functionStrich(double x) {
		return -1/4 * Math.exp(-x/4) * Math.cos(x) - Math.exp(-x/4) * Math.sin(x);
	}
	
	/******************************************************************
	 * sigmoid 1/(1+E^(-v))
	 * sigmoidStrich E^(-v)/(1+E^(-v))^2
	 ******************************************************************/
	public static double sigmoid(double v){
		return 1/(1 + Math.exp(-v));
	}
	public static double sigmoidStrich(double v) {
		return (Math.exp(-v))/(Math.pow((1 + Math.exp(-v)), 2));
	}
	
	public static double output(double x, double d, double y) {
		return x * (d-y);
	}
	
	public static double hidden(double u, double w, double x, double d, double y) {
		return (d-y) * w * sigmoidStrich(u);
	}
	
	public static double kontext1(double d, double y, double w, double kont2) {
		return (d-y) * w * sigmoidStrich(kont2);
	}

	public static double kontext2(double x, double wx, double v) {
		return x + wx * sigmoidStrich(v) * x;
	}
}
