import java.awt.Color;

public class SeamCarver {

	private int W, H; //width and height of picture
	private Picture picture;
	private double[][] energy; //energy for each pixels
	private double[][] totE; //total energy from start to this pixel
	private int[][] parentOffset; //offset (-1/0/1) of parent

	// create a seam carver object based on the given picture
	public SeamCarver(final Picture picture)  {  
		this.picture = new Picture(picture);

		this.W = picture.width();
		this.H = picture.height();
		this.energy = new double[W][H];
		this.totE = new double[W][H];
		this.parentOffset = new int[W][H];
		for (int w = 0; w < W; ++w) 
			for (int h = 0; h < H; ++h)
				energy[w][h] = calcEnergy(w, h);
	}
	// current picture
	public Picture picture() { return picture; }                     
	// width of current picture
	public     int width() { return W; }        
	// height of current picture
	public     int height() { return H; }
	// energy of pixel at column x and row y
	public  double energy(int x, int y) {
		if (x < 0 || x >= W || y < 0 || y >= H) 
			throw new IndexOutOfBoundsException();
		return energy[x][y];
	}

	private  double calcEnergy(int x, int y) { 
		if (x == 0 || x == W-1 || y == 0 || y == H-1)
			return (double) 195075;

		Color cl = picture.get(x-1, y); //left
		Color cr = picture.get(x+1, y); //right
		Color ct = picture.get(x, y-1); //top
		Color cb = picture.get(x, y+1); //bottom
		return (double) 
				Math.pow((cl.getRed()   - cr.getRed()  ), 2) +
				Math.pow((cl.getGreen() - cr.getGreen()), 2) +
				Math.pow((cl.getBlue()  - cr.getBlue() ), 2) +
				Math.pow((ct.getRed()   - cb.getRed()  ), 2) +
				Math.pow((ct.getGreen() - cb.getGreen()), 2) +
				Math.pow((ct.getBlue()  - cb.getBlue() ), 2);
	}

	public   int[] findHorizontalSeam() {
		int[] seam = new int[W];

		//first col	
		for (int h = 0; h < H; ++h) 
			totE[0][h] = energy[0][h];

		for (int w = 1; w < W; ++w) {
			for (int h = 0; h < H; ++h) {
				int j = 0;
				if (h > 0 && totE[w-1][h-1] < totE[w-1][h+j])
					j = -1;
				if (h < H-1 && totE[w-1][h+1] < totE[w-1][h+j])
					j =  1;
				parentOffset[w][h] = j;
				totE[w][h] = totE[w-1][h+j] + energy[w][h];
			}
		}	

		//find the pixel in the last row with minimum totE
		int minH = 0;
		double minE = totE[W-1][0];
		for (int h = 1; h < H; ++h)
			if (totE[W-1][h] < minE) {
				minE = totE[W-1][h];
				minH = h;
			}

		//construct seam from minIdx
		seam = new int[W];
		int w = W-1;
		seam[W-1] = minH;
		while (w > 0) {
			minH = minH + parentOffset[w][minH];
			seam[--w] = minH;
		}
		if(!isLegalHSeam(seam)) {
			throw new RuntimeException("Calculated horizontal seam is wrong!");
		} 	
		return seam;
	}
	// sequence of indices for vertical seam
	public   int[] findVerticalSeam() {
		int[] seam = new int[H];
		//first row	
		for (int w = 0; w < W; ++w) 
			totE[w][0] = energy[w][0];

		for (int h = 1; h < H; ++h) {
			for (int w = 0; w < W; ++w) {
				int j = 0;
				if (w > 0 && totE[w-1][h-1] < totE[w+j][h-1])
					j = -1;
				if (w < W-1 && totE[w+1][h-1] < totE[w+j][h-1])
					j =  1;
				parentOffset[w][h] = j;
				totE[w][h] = totE[w+j][h-1] + energy[w][h];
			}
		}

		//find the pixel in the last row with minimum totE
		int minW = 0;
		double minE = totE[0][H-1];
		for (int w = 1; w < W; ++w)
			if (totE[w][H-1] < minE) {
				minE = totE[w][H-1];
				minW = w;
			}

		//construct seam from minIdx
		seam = new int[H];
		int h = H-1;
		seam[H-1] = minW;
		while (h > 0) {
			minW = minW + parentOffset[minW][h];
			seam[--h] = minW;
		}
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
		Picture newpic = new Picture(W, --H);

		for (int h = 0; h < H; ++h)
			for (int w = 0; w < W; ++w) {
				Color c;
				if ( h < seam[w]) c = picture.get(w, h);
				else c = picture.get(w, h+1);
				newpic.set(w, h, c);
			}
		picture = newpic;
	}
	// remove vertical seam from current picture
	public    void removeVerticalSeam(int[] seam) {
		if (seam == null) 
			throw new NullPointerException();
		if (W <= 1) 
			throw new IllegalArgumentException("width <= 1");
		if (!isLegalVSeam(seam)) 
			throw new IllegalArgumentException("Illegal vertical seam");
		Picture newpic = new Picture(--W, H);
		for (int h = 0; h < H; ++h)
			for (int w = 0; w < W; ++w) {
				Color c;
				if ( w < seam[h]) c = picture.get(w, h);
				else c = picture.get(w+1, h);
				newpic.set(w, h, c);
			}
		picture = newpic;
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
}
