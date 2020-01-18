import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class SortPanel extends JPanel implements ActionListener {
	
	public Timer timer = new Timer(1, this);
	private int total;
	private Block allBlocks[];
	private Block sortArray[];
	private List<Animation> animations;
	private boolean isStopped;
	
	public SortPanel() {
		// Setting the size of the SortPanel
		Dimension dim = getPreferredSize();
		dim.width = 900;
		setPreferredSize(dim);
		
		// Giving the panel an etched border and dark gray background 
		setBorder(BorderFactory.createEtchedBorder());
		setBackground(Color.DARK_GRAY);
		
		total = 150;
		randomSet();
		animations = new ArrayList<Animation>();
		go();
		
		timer.start();
	}
	
	// This function is what paints all the blocks inside the allBlocks array
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;

		double canvasWidth = 900.0d;
		double canvasHeight = 700.0d;
		
		for(int i = 0; i < total; i++) {
			Rectangle2D.Double rect = new Rectangle2D.Double(i*(canvasWidth/total), canvasHeight - allBlocks[i].getValue(), canvasWidth/total, allBlocks[i].getValue());

			g2.setColor(allBlocks[i].getColor());
			g2.fill(rect);

			g2.setColor(Color.black);
			g2.draw(rect);
		}
	}
	
	// This function is called as long as the timer is on.  If the timer delay is 1 ms, then this function is called every 1 ms.
	public void actionPerformed(ActionEvent e) {
		/*
			The animations list is what gives us a visual of what is happening.  It is a List of Animaiton objects, which each contain information
			about the index, block, and whether or not a swap took place.
			
			When the list is empty, we see no change.  When there are items present however, we get the first animation in the list.
			
			If its swapAni boolean value is false, it means that only a block highlight is happening.  So, we just change the block data 
			in allBlocks at the recorded index.  Once this is done, we remove that animation from the list.  We do this until the list is empty.  
			
			If its swapAni value is true, we do the same as above but twice.  This is to make swaps more natural.  If we were to do it one block
			at a time, one block would seemingly disappear and then reappear in a different spot, which looks terrible.
		*/
		if(!animations.isEmpty() && isStopped == false) {
			Animation a = animations.get(0);
			if(a.getSwapAni()) {
				allBlocks[a.getIndex()] = a.getBlock();
				animations.remove(0);
				
				a = animations.get(0);
				allBlocks[a.getIndex()] = a.getBlock();
				animations.remove(0);
			}
			else {
				allBlocks[a.getIndex()] = a.getBlock();
				animations.remove(0);
			}
		}
		
		repaint();
	}
	
	// Gets the index of the selected algorithm in the menuPanel, then executes the correct algorithm
	public void start(int index) {
		stop();
		
		// Clearing out the animations list and making a fresh one
		animations = null;
		animations = new ArrayList<Animation>();
		
		// We use a sortArray because allBlocks can only update through animations
		sortArray = null;
		sortArray = new Block[total];
		
		// Copy the content from allBlocks into sortArray
		for(int i = 0; i < total; i++) {
			sortArray[i] = new Block(allBlocks[i].getValue(), Color.white);
		}
		
		go();
		
		if(index == 0) {
			bubbleSort();
		}
		else if(index == 1) {
			selectionSort();
		}
		else if(index == 2) {
			cocktailSort();
		}
		else if(index == 3) {
			insertionSort();
		}
		else if(index == 4) {
			shellSort();
		}
		else if(index == 5) {
			heapSort();
		}
		else if(index == 6) {
			mergeSort(0, total-1);
			
			for(int i = 0; i < total; i++) {
				sortArray[i].setColor(Color.green);
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
			}
		}
		else if(index == 7) {
			quickSort(0, total-1);
		}
	}
	
	// stop and go functions to control the flow of animations
	public void stop() {
		isStopped = true;
	}
	
	public void go() {
		isStopped = false;
	}
	
	// Creates a set for allBlocks that is random
	public void randomSet() {
		allBlocks = null;
		allBlocks = new Block[total];
		
		animations = null;
		animations = new ArrayList<Animation>();
		
		Random random = new Random();
		for(int i = 0; i < total; i++) {
			allBlocks[i] = new Block((random.nextDouble() * 620.0d) + 55.0d);
		}
		
		stop();
	}
	
	// Creates a set for allBlocks that is in ascending order
	public void ascSet() {
		allBlocks = null;
		allBlocks = new Block[total];
		
		randomSet();
		sortAsc();
		
		stop();
	}
	
	// Creates a set for allBlocks that is in descending order
	public void descSet() {
		allBlocks = null;
		allBlocks = new Block[total];
		
		randomSet();
		sortDesc();
		
		stop();
	}

	// Uses a simple bubble sort (because 300 is the max, bubble is fast enough)
	public void sortAsc() {
		boolean isSorted = true;
		
		for(int i = 0; i < total - 1; i++) {
			for(int j = total - 1; j > i; j--) {
				if(allBlocks[j].getValue() < allBlocks[j - 1].getValue()) {
					swap(allBlocks, j, j-1);
					isSorted = false;
				}
			}
			
			if(isSorted) {
				break;
			}

			isSorted = true;
		}
	}
	
	// Reverse bubble sort
	public void sortDesc() {
		boolean isSorted = true;
		
		for(int i = 0; i < total - 1; i++) {
			for(int j = total - 1; j > i; j--) {
				if(allBlocks[j].getValue() > allBlocks[j - 1].getValue()) {
					swap(allBlocks, j, j-1);
					isSorted = false;
				}
			}
			
			if(isSorted) {
				break;
			}

			isSorted = true;
		}
	}

	// This is for swapping the locations of two blocks
	public void swap(Block[] arr, int i, int j) {
		Block tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	
	/*
	 	Below is where animations are created.
	 	
	 	When we create an animation, we need to send an integer value for the index, a Block for block data,
	 	and a boolean for if a swap occurred.
		
		Because we have to create new blocks for each animation (I have tried sending blocks from sortArray, but things break when doing that),
		the code can get a little messy.
	*/
	
	public void bubbleSort() {
		boolean isSorted = true;

		for(int i = total - 1; i > 0; i--) {
			for(int j = 0; j < i; j++) {
				
				sortArray[j].setColor(Color.red);
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				
				if(sortArray[j].getValue() > sortArray[j + 1].getValue()) {
					swap(sortArray, j, j+1);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
					animations.add(new Animation(j+1, new Block(sortArray[j+1].getValue(), sortArray[j+1].getColor()), false));
					
					isSorted = false;
				}
				else {
					sortArray[j].setColor(Color.white);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				}
			}
			
			if(isSorted || i == 1) {
				for(int k = i; k >= 0; k--) {
					sortArray[k].setColor(Color.green);
					animations.add(new Animation(k, new Block(sortArray[k].getValue(), sortArray[k].getColor()), false));
				}
				break;
			}
			else {
				sortArray[i].setColor(Color.green);
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
			}
			
			isSorted = true;
		}
	}
	
	public void selectionSort() {
		for(int i = total - 1; i > 0; i--) {
			
			double max = sortArray[0].getValue();
			int maxLocation = 0;
			
			sortArray[maxLocation].setColor(Color.red);
			animations.add(new Animation(maxLocation, new Block(sortArray[maxLocation].getValue(), sortArray[maxLocation].getColor()), false));
			
			for(int j = 1; j <= i; j++) {
				
				sortArray[j].setColor(Color.blue);
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				
				if(sortArray[j].getValue() > max) {
					sortArray[maxLocation].setColor(Color.white);
					animations.add(new Animation(maxLocation, new Block(sortArray[maxLocation].getValue(), sortArray[maxLocation].getColor()), false));
					
					sortArray[j].setColor(Color.red);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
					
					max = sortArray[j].getValue();
					maxLocation = j;
				}
				else {
					sortArray[j].setColor(Color.white);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				}
			}
			
			swap(sortArray, maxLocation, i);
			
			sortArray[i].setColor(Color.green);
			animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
			
			if(maxLocation != i) {
				sortArray[maxLocation].setColor(Color.white);
				animations.add(new Animation(maxLocation, new Block(sortArray[maxLocation].getValue(), sortArray[maxLocation].getColor()), false));
			}
			
			if(i == 1) {
				sortArray[0].setColor(Color.green);
				animations.add(new Animation(0, new Block(sortArray[0].getValue(), sortArray[0].getColor()), false));
			}
		}
	}
	
	public void cocktailSort() {
		boolean isSorted = false;
		int highLimit = total - 1;
		int lowLimit = 0;
		
		while(!isSorted) {
			isSorted = true;
			
			for(int i = lowLimit; i < highLimit; i++) {
				sortArray[i].setColor(Color.red);
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				
				if(sortArray[i].getValue() > sortArray[i+1].getValue()) {
					swap(sortArray, i, i+1);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
					animations.add(new Animation(i+1, new Block(sortArray[i+1].getValue(), sortArray[i+1].getColor()), false));
					
					isSorted = false;
				}
				else {
					sortArray[i].setColor(Color.white);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				}
				
			}
			
			sortArray[highLimit].setColor(Color.green);
			animations.add(new Animation(highLimit, new Block(sortArray[highLimit].getValue(), sortArray[highLimit].getColor()), false));
			
			highLimit--;
			
			for(int i = highLimit; i > lowLimit; i--) {
				sortArray[i].setColor(Color.red);
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				
				if(sortArray[i].getValue() < sortArray[i-1].getValue()) {
					swap(sortArray, i, i-1);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
					animations.add(new Animation(i-1, new Block(sortArray[i-1].getValue(), sortArray[i-1].getColor()), false));
					
					isSorted = false;
				}
				else {
					sortArray[i].setColor(Color.white);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				}
			}
			
			if(isSorted) {
				for(int i = lowLimit; i <= highLimit; i++) {
					sortArray[i].setColor(Color.green);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				}
			}
			else {
				sortArray[lowLimit].setColor(Color.green);
				animations.add(new Animation(lowLimit, new Block(sortArray[lowLimit].getValue(), sortArray[lowLimit].getColor()), false));
			}
			
			lowLimit++;
		}
	}
	
	public void insertionSort() {
		for(int i = 1; i <= total - 1; i++) {
			for(int j = i; j > 0; j--) {
				sortArray[j].setColor(Color.red);
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				
				if(sortArray[j].getValue() < sortArray[j-1].getValue()) {
					swap(sortArray, j, j-1);
					
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
					animations.add(new Animation(j-1, new Block(sortArray[j-1].getValue(), sortArray[j-1].getColor()), false));
				}
				else {
					sortArray[j].setColor(Color.white);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
					sortArray[j-1].setColor(Color.white);
					animations.add(new Animation(j-1, new Block(sortArray[j-1].getValue(), sortArray[j-1].getColor()), false));
					break;
				}
				
				if(j == 1) {
					sortArray[j-1].setColor(Color.white);
					animations.add(new Animation(j-1, new Block(sortArray[j-1].getValue(), sortArray[j-1].getColor()), false));
				}
			}
		}
		
		for(int i = 0; i < total; i++) {
			sortArray[i].setColor(Color.green);
			animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
		}
	}
	
	public void shellSort() {
		int gap = total/2;
		
		while(gap > 0) {
	        for(int i = gap; i < total; i++) {
	        	sortArray[i].setColor(Color.red);
	        	animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
	            
	            sortArray[i-gap].setColor(Color.blue);
	        	animations.add(new Animation(i-gap, new Block(sortArray[i-gap].getValue(), sortArray[i-gap].getColor()), false));
	            
	            int j;
	            for(j = i; j >= gap && sortArray[j - gap].getValue() > sortArray[j].getValue(); j -= gap) {
	            	if(sortArray[j-gap].getColor() != Color.blue) {
	            		sortArray[j-gap].setColor(Color.blue);
	    	        	animations.add(new Animation(j-gap, new Block(sortArray[j-gap].getValue(), sortArray[j-gap].getColor()), false));
	            	}
	            	
	            	swap(sortArray, j, j-gap);
	            	
	            	animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
	            	animations.add(new Animation(j-gap, new Block(sortArray[j-gap].getValue(), sortArray[j-gap].getColor()), false));

	            	sortArray[j].setColor(Color.white);
	            	animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
	            }
	            
	            sortArray[i-gap].setColor(Color.white);
		        animations.add(new Animation(i-gap, new Block(sortArray[i-gap].getValue(), sortArray[i-gap].getColor()), true));
		            

		        sortArray[j].setColor(Color.white);
		        animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
	            
	        }
	        gap = gap/2;
	    }
		
		for(int i = 0; i < total; i++) {
			sortArray[i].setColor(Color.green);
            animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
		}
	}
	
	public void heapSort() {
		heapify(total);
		
		int lastIndex = total - 1;
		while(lastIndex > 0) {
			sortArray[lastIndex].setColor(Color.red);
            animations.add(new Animation(lastIndex, new Block(sortArray[lastIndex].getValue(), sortArray[lastIndex].getColor()), true));
            
            sortArray[0].setColor(Color.blue);
            animations.add(new Animation(0, new Block(sortArray[0].getValue(), sortArray[0].getColor()), false));
			
			swap(sortArray, 0, lastIndex);
			
			sortArray[lastIndex].setColor(Color.green);
			animations.add(new Animation(lastIndex, new Block(sortArray[lastIndex].getValue(), sortArray[lastIndex].getColor()), true));
			animations.add(new Animation(0, new Block(sortArray[0].getValue(), sortArray[0].getColor()), false));
			
			if(lastIndex == 1) {
				sortArray[0].setColor(Color.green);
				animations.add(new Animation(0, new Block(sortArray[0].getValue(), sortArray[0].getColor()), false));
				break;
			}
			else {
				lastIndex--;
				siftDown(0, lastIndex);
			}
		}
	}
	
	public void heapify(int n) {
		for(int index = n-1; index >= 0; index--) {
			siftDown(index, n-1);
		}
	}
	
	public void siftDown(int index, int last) {
		int root = index;
		int child = (2*root) + 1;
		
		while(last >= child) {
			int swapIndex = root;
			
			if(sortArray[swapIndex].getColor() != Color.red) {
				sortArray[swapIndex].setColor(Color.red);
				animations.add(new Animation(swapIndex, new Block(sortArray[swapIndex].getValue(), sortArray[swapIndex].getColor()), false));
			}
			
			if(sortArray[swapIndex].getValue() < sortArray[child].getValue()) {
				swapIndex = child;
			}
			
			if(child + 1 <= last && sortArray[swapIndex].getValue() < sortArray[child + 1].getValue()) {
				swapIndex = child + 1;
			}
			
			if(swapIndex != root) {
				sortArray[swapIndex].setColor(Color.blue);
				animations.add(new Animation(swapIndex, new Block(sortArray[swapIndex].getValue(), sortArray[swapIndex].getColor()), false));
				
				swap(sortArray, root, swapIndex);
				
				animations.add(new Animation(root, new Block(sortArray[root].getValue(), sortArray[root].getColor()), true));
				animations.add(new Animation(swapIndex, new Block(sortArray[swapIndex].getValue(), sortArray[swapIndex].getColor()), false));
				
				sortArray[root].setColor(Color.white);
				animations.add(new Animation(root, new Block(sortArray[root].getValue(), sortArray[root].getColor()), false));
				
				root = swapIndex;
				child = (2*root) + 1;
				
				if(last <= child) {
					sortArray[swapIndex].setColor(Color.white);
					animations.add(new Animation(swapIndex, new Block(sortArray[swapIndex].getValue(), sortArray[swapIndex].getColor()), false));
				}
			}
			else {
				sortArray[swapIndex].setColor(Color.white);
				animations.add(new Animation(swapIndex, new Block(sortArray[swapIndex].getValue(), sortArray[swapIndex].getColor()), false));
				break;
			}
		}
	}
	
	public void mergeSort(int left, int right) {
		if(left < right) {
			int mid = (left+right)/2;
			mergeSort(left, mid);
			mergeSort(mid+1, right);
			
			merge(left, mid, right);
		}
	}
	
	public void merge(int left, int mid, int right) {
		int leftTotal = mid - left + 1;
		int rightTotal = right - mid;
		
		Block[] leftArray = new Block[leftTotal];
		Block[] rightArray = new Block[rightTotal];
		
		for(int i = 0; i < leftTotal; i++) {
			leftArray[i] = sortArray[left + i];
		}
		
		for(int i = 0; i < rightTotal; i++) {
			rightArray[i] = sortArray[mid + 1 + i];
		}
		
		int i = 0;
		int j = 0;
		int k = left;
		
		while(i != leftTotal && j != rightTotal) {

			if(leftArray[i].getValue() < rightArray[j].getValue()) {
				sortArray[k] = leftArray[i];
				
				animations.add(new Animation(k, new Block(sortArray[k].getValue(), sortArray[k].getColor()), false));
				
				i++;
			}
			else {
				sortArray[k] = rightArray[j];
				
				animations.add(new Animation(k, new Block(sortArray[k].getValue(), sortArray[k].getColor()), false));

				j++;
			}
			
			k++;
		}
		
		while(i != leftTotal) {
			sortArray[k] = leftArray[i];
			animations.add(new Animation(k, new Block(sortArray[k].getValue(), sortArray[k].getColor()), false));
			k++;
			i++;
		}
		
		while(j != rightTotal) {
			sortArray[k] = rightArray[j];
			animations.add(new Animation(k, new Block(sortArray[k].getValue(), sortArray[k].getColor()), false));
			k++;
			j++;
		}
	}
	
	public void quickSort(int low, int high) {
		if(low < high) {
			int p = partition(low, high);
			quickSort(low, p);
			quickSort(p + 1, high);
		}
		else {
			sortArray[low].setColor(Color.green);
			animations.add(new Animation(low, new Block(sortArray[low].getValue(), sortArray[low].getColor()), false));
		}
	}
	
	public int partition(int low, int high) {
		int mid = (low + high)/2;
		double pivot = sortArray[mid].getValue();
		int i = low;
		int j = high;
		
		sortArray[mid].setColor(Color.red);
		animations.add(new Animation(mid, new Block(sortArray[mid].getValue(), sortArray[mid].getColor()), false));
		
		sortArray[i].setColor(Color.blue);
		animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
		
		sortArray[j].setColor(Color.blue);
		animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
		
		while(i < j) {
			while(sortArray[j].getValue() > pivot && j != i) {
				sortArray[j].setColor(Color.white);
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), true));
				j--;
				if(j != mid) {
					sortArray[j].setColor(Color.blue);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				}
				else {
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				}
			}
			
			while(sortArray[i].getValue() < pivot && i != j) {
				sortArray[i].setColor(Color.white);
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
				i++;
				if(i != mid) {
					sortArray[i].setColor(Color.blue);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				}
				else {
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				}
				
			}

			if(i < j) {
				swap(sortArray, i, j);
				
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				
				if(i == mid ) {
					sortArray[i].setColor(Color.white);
					animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), false));
				}
				else if(j == mid) {
					sortArray[j].setColor(Color.white);
					animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
				}
			}
			else {
				sortArray[i].setColor(Color.white);
				sortArray[j].setColor(Color.white);
				animations.add(new Animation(i, new Block(sortArray[i].getValue(), sortArray[i].getColor()), true));
				animations.add(new Animation(j, new Block(sortArray[j].getValue(), sortArray[j].getColor()), false));
			}
		}
		
		if(j == high) {
			return j - 1;
		}

		return j;
	}

	public void setTotal(int total) {
		stop();
		this.total = total;
		randomSet();
		go();
	}
	
	public void setSpeed(int speed) {
		timer.setDelay(speed);
	}
} 