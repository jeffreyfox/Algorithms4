import java.awt.Color;

public class SeamCarver {

	private int W, H; //width and height of picture
	private Color[][] c; //colors of each pixel
	private double[][] E; //energy for each pixels

	// create a seam carver object based on the given picture
	public SeamCarver(final Picture picture)  {  

		W = picture.width();
		H = picture.height();
		c = new Color[H][W]; //row major
		E = new double[H][W]; 		

		//first pass init color matrix
		for (int ir = 0; ir < H; ++ir)
			for (int jc = 0; jc < W; ++jc)
				c[ir][jc] = picture.get(jc, ir);
		//need a second pass to calculate energy
		for (int ir = 0; ir < H; ++ir)
			for (int jc = 0; jc < W; ++jc)				
				E[ir][jc] = calcEnergy(ir, jc);
	}

	// current picture
	public Picture picture() { 
		Picture picture = new Picture(W, H);
		for (int ir = 0; ir < H; ++ir)
			for (int jc = 0; jc < W; ++jc)
				picture.set(jc, ir, c[ir][jc]);
		return picture;
	}
	// width of current picture
	public     int width() { return W; }        
	// height of current picture
	public     int height() { return H; }
	// energy of pixel at column x and row y
	public  double energy(int x, int y) {
		if (x < 0 || x >= W || y < 0 || y >= H) 
			throw new IndexOutOfBoundsException();
		return E[y][x]; //opposite order!
	}

	// sequence of indices for vertical seam
	public   int[] findVerticalSeam() {
		double[][] Ea; // accumulated energy from start to this pixel
		int[][] os; //offset (-1/0/1) of parent	
		Ea = new double[H][W];
		os = new int[H][W];		

		int[] seam = new int[H];
		// Ea for the first row
		for (int jc = 0; jc < W; ++jc) 
			Ea[0][jc] = E[0][jc];

		for (int ir = 1; ir < H; ++ir) {
			for (int jc = 0; jc < W; ++jc) {
				int k = 0; //offset
				if (jc > 0   && Ea[ir-1][jc-1] < Ea[ir-1][jc+k])
					k = -1;
				if (jc < W-1 && Ea[ir-1][jc+1] < Ea[ir-1][jc+k])
					k =  1;
				os[ir][jc] = k;
				Ea[ir][jc] = Ea[ir-1][jc+k] + E[ir][jc];
			}
		}

		//find the pixel in the last row with minimum Ea
		int jcMin = 0;
		double EaMin = Ea[H-1][0];
		for (int jc = 1; jc < W; ++jc)
			if (Ea[H-1][jc] < EaMin) {
				EaMin = Ea[H-1][jc];
				jcMin = jc;
			}

		//construct seam from minIdx
		seam = new int[H];		
		seam[H-1] = jcMin;
		int ir = H-1;
		while (ir > 0) {
			jcMin = jcMin + os[ir][jcMin];
			seam[--ir] = jcMin;
		}
		return seam;
	}

	// remove vertical seam from current picture
	public    void removeVerticalSeam(int[] seam) {
		if (seam == null) 
			throw new NullPointerException();
		if (W <= 1) 
			throw new IllegalArgumentException("width <= 1");
		if (!isLegalVSeam(seam)) 
			throw new IllegalArgumentException("Illegal vertical seam");

		for (int ir = 0; ir < H; ++ir) {
			int j = seam[ir];
			//shift pixels after seam line to left by 1
			System.arraycopy(c[ir], j+1, c[ir], j, W-1-j);
			System.arraycopy(E[ir], j+1, E[ir], j, W-1-j);
		}
		W--;
	}
	public   int[] findHorizontalSeam() {		
		transpose();
		int[] seam = findVerticalSeam();
		transpose();
		return seam;
	}

	// remove horizontal seam from current picture
	public    void removeHorizontalSeam(int[] seam) {
		if (seam == null) 
			throw new NullPointerException();
		if (H <= 1) 
			throw new IllegalArgumentException("height <= 1");
		if (!isLegalHSeam(seam)) 
			throw new IllegalArgumentException("Illegal horizontal seam");
		transpose();
		removeVerticalSeam(seam);
		transpose();
	}

	private boolean isLegalHSeam(int[] seam) {
		if (seam.length != W) return false;
		for (int w = 0; w < seam.length; ++w) {
			//out of box
			if (seam[w] < 0 || seam[w] >= H) return false;
			//not adjacent
			if (w > 0 && Math.abs(seam[w] - seam[w-1]) > 1) return false;
			if (w < W-1 && Math.abs(seam[w] - seam[w+1]) > 1) return false;
		}
		return true;		
	}
	private boolean isLegalVSeam(int[] seam) {

		if (seam.length != H) return false;
		for (int h = 0; h < seam.length; ++h) {
			//out of box
			if (seam[h] < 0 || seam[h] >= W) return false;
			//not adjacent
			if (h > 0 && Math.abs(seam[h] - seam[h-1]) > 1) return false;
			if (h < H-1 && Math.abs(seam[h] - seam[h+1]) > 1) return false;
		}
		return true;		
	}
	// calculate energy of x(irow), y(jcol)
	private  double calcEnergy(int x, int y) { 
		if (x == 0 || x == H-1 || y == 0 || y == W-1)
			return (double) 195075;

		Color cl = c[x-1][y]; //left
		Color cr = c[x+1][y]; //right
		Color ct = c[x][y-1]; //top
		Color cb = c[x][y+1]; //bottom
		double dxR = Math.pow((cl.getRed()   - cr.getRed()  ), 2);
		double dxG = Math.pow((cl.getGreen() - cr.getGreen()), 2);
		double dxB = Math.pow((cl.getBlue()  - cr.getBlue() ), 2);
		double dyR = Math.pow((ct.getRed()   - cb.getRed()  ), 2);
		double dyG = Math.pow((ct.getGreen() - cb.getGreen()), 2);
		double dyB = Math.pow((ct.getBlue()  - cb.getBlue() ), 2);
		return dxR + dxG + dxB + dyR + dyG + dyB;
	}			

	private void transpose() {
		Color[][] newc = new Color[W][H];	
		double[][] newE = new double[W][H];
		for (int ir = 0; ir < W; ++ir) {
			for (int jc = 0; jc < H; ++jc) {
				newc[ir][jc] = c[jc][ir];
				newE[ir][jc] = E[jc][ir]; 	
			}
		}
		c = newc;
		E = newE;
		//swap H and W
		int tmp = H; H = W; W = tmp;		
	}
}
