public class Animation {
	private int index;
	private Block block;
	private boolean swapAni;
	
	public Animation(int index, Block block, boolean swapAni) {
		this.index = index;
		this.block = block;
		this.swapAni = swapAni;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public boolean getSwapAni() {
		return swapAni;
	}
}